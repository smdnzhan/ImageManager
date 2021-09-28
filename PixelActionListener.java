package PCcamera;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PixelActionListener implements ActionListener,PixelConfig {
    public JPanel jp;
    public Graphics g; //从JPanel上获取
    public int[][] pixelArr = null; //rgb值

    //构造方法
    public PixelActionListener(JPanel jp) {
        this.jp = jp;
        //this.g = jp.getGraphics(); 1
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("打开图片")) {
            //初始文件夹
            JFileChooser jfc = new JFileChooser("C:\\Users\\ZDX\\Pictures\\Voto");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("选取一张jpg或png图片","jpg","png");
            jfc.setFileFilter(filter);
            jfc.setSize(100,300);
            jfc.showOpenDialog(null);
            //选择的文件路径
            File file =jfc.getSelectedFile();
            try {
                pixelArr = getImagePixel(file, jp.getGraphics());//直接传画笔对象会空指针? this.g X
                System.out.println("正常执行");
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        }
        else if(s.equals("灰度")){ //灰度就是没有色彩，RGB色彩分量全部相等
            if (pixelArr!=null){
            for (int i = 0; i < pixelArr.length; i++) {
                for (int j = 0; j < pixelArr[0].length; j++) {
                    int[] c = getAllColor(pixelArr[i][j]);
                    int gray = (c[0]+c[1]+c[2])/3; //平均法取灰度值
                    jp.getGraphics().setColor(new Color(gray,gray,gray));
                    jp.getGraphics().drawLine(i + X0, j + Y0, i + X0, j + Y0);
                    pixelArr[i][j]=gray<<16+gray<<8+gray;
                }
            }jp.repaint();
        }else
                System.out.println("必须先选定一张图片");
        }
        //亮度调整:三通道加同一个值
        else if (s.equals("亮度")){
            if (pixelArr!=null){
                /*
                JSlider js = new JSlider(SwingConstants.VERTICAL,-20,20,0);
                js.setPaintLabels(true);
                js.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println("当前值:"+js.getValue());
                    }
                });
                jp.add(js,FlowLayout.RIGHT);
                jp.repaint();
                int param = js.getValue();

                 */
                for (int i = 0; i <pixelArr.length ; i++) {
                    for (int j = 0; j <pixelArr[0].length ; j++) {
                        int red = getAllColor(pixelArr[i][j])[0]+SCALE_PARAM;
                        int green = getAllColor(pixelArr[i][j])[1]+SCALE_PARAM;
                        int blue = getAllColor(pixelArr[i][j])[2]+SCALE_PARAM;
                        red = limit(red); //防止越界
                        green = limit(green);
                        blue = limit(blue);
                        jp.getGraphics().setColor(new Color(red,green,blue));
                        jp.getGraphics().drawLine(i + X0, j + Y0, i + X0, j + Y0);
                        pixelArr[i][j]=red<<16+green<<8+blue;
                        //越界问题？
                    }
                }
                jp.repaint();
            }else
                System.out.println("必须先选择一张图片");
        }
        else if (s.equals("黑白")){ //Q！
            if (pixelArr!=null){
                for (int i = 0; i <pixelArr.length ; i++) {
                    for (int j = 0; j <pixelArr[0].length ; j++) {
                        int[]temp = getAllColor(pixelArr[i][j]);
                        int bw = (temp[0]+temp[1]+temp[0])/3;
                        // 三通道平均值大于128 则设为白色 否则设为黑色
                        if (bw>=128){
                            bw=255;
                        }else
                            bw=0;
                        jp.getGraphics().setColor(new Color(bw,bw,bw));
                        jp.getGraphics().drawLine(i + X0, j + Y0, i + X0, j + Y0);
                        pixelArr[i][j]=bw<<16+bw<<8+bw;
                    }
                }
                jp.repaint();
            }
            System.out.println("必须先选择一张图片");
        }
        else
            System.out.println("其他");
    }

    //画图片
    public int[][] getImagePixel(File file, Graphics g) throws Exception {
        //定义二维数组 存储RGB值
        BufferedImage bi = null;
        if (file.exists()) {
            bi = ImageIO.read(file);
            //获得图片长和宽
            int height = bi.getHeight();
            int width = bi.getWidth();
            int[][] pic = new int[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = bi.getRGB(i, j);
                    pic[i][j] = rgb;
                    int[] temp = getAllColor(pic[i][j]); //拆分RGB值
                    g.setColor(new Color(temp[0], temp[1], temp[2]));
                    g.drawLine(i + X0, j + Y0, i + X0, j + Y0);
                }
            }
            System.out.println("画图成功");
            return pic;
        } else
            System.out.println("路径错误");
        return null;
    }

    //根据RGB值 返回三原色值
    public int[] getAllColor(int image) {
        Color c = new Color(image);
        int[] rgb = new int[3];
        rgb[0] = c.getRed();//(image & 0xff0000) >> 16; //对应红色部分 右移16位
        rgb[1] = c.getGreen();//(image & 0xff00) >> 8; // 绿色部分
        rgb[2] = c.getBlue();//(image & 0xff); //蓝色部分
        return rgb;
    }

    public void drawPixel(int[][] image, Graphics g) {
        if (image != null) {
            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[0].length; j++) {
                    int[] temp = getAllColor(image[i][j]);
                    g.setColor(new Color(temp[0], temp[1], temp[2]));
                    g.drawLine(i + X0, j + Y0, i + X0, j + Y0);
                }
            }
        }else
            System.out.println("暂无图片");

    }
    //限定最大值
    public int limit(int rgb){
        if (rgb>255){
            rgb = 255;
        }else if(rgb<0){
            rgb = 0;
        }
        return rgb;
    }
}
