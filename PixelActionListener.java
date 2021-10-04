package PCcamera;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PixelActionListener implements ActionListener,PixelConfig {
    public JPanel jp;
    public Graphics bg;
    public int[][] pixelArr ; //rgb值
    public File input ;
    public BufferedImage bi_img;
    public PixelMouse pm;
    public Mosaic mc ;
    public File output;
    public Linked<BufferedImage> imageList ;
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
            input =jfc.getSelectedFile();
            try {
                pixelArr = getImagePixel(input.getAbsolutePath());//打开图片时缓存图片
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        }
        else if(s.equals("灰度")){ //灰度就是没有色彩，RGB色彩分量全部相等
            if (pixelArr!=null){
                //每个效果都新创建一个缓冲区
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
            for (int i = 0; i < pixelArr.length; i++) {
                for (int j = 0; j < pixelArr[0].length; j++) {
                    int[] c = getAllColor(pixelArr[i][j]);
                    int gray = (c[0]+c[1]+c[2])/3; //平均法取灰度值
                    bg.setColor(new Color(gray,gray,gray));
                    bg.drawRect(i, j, 1,1);
                    //pixelArr[i][j]=gray<<16|gray<<8|gray;
                    //保存像素值变化
                }
            }
            jp.getGraphics().drawImage(bi_img,X0,Y0,null); //画笔的位置就是出现的位置
            imageList.addFirst(bi_img);
        }else
                System.out.println("必须先选定一张图片");
        }
        //亮度调整:三通道加同一个值
        else if (s.equals("亮度")){
            if (pixelArr!=null){
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
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
                        bg.setColor(new Color(red,green,blue));
                        bg.drawLine(i , j , i , j );
                        //pixelArr[i][j]=red<<16|green<<8|blue;
                        //越界问题？
                    }
                }
                jp.getGraphics().drawImage(bi_img,X0,Y0,null);
                imageList.addFirst(bi_img);
            }else
                System.out.println("必须先选择一张图片");
        }
        else if (s.equals("黑白")){ //Q！
            if (pixelArr!=null){
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
                for (int i = 0; i <pixelArr.length ; i++) {
                    for (int j = 0; j <pixelArr[0].length ; j++) {
                        int[]temp = getAllColor(pixelArr[i][j]);
                        int bw = (temp[0]+temp[1]+temp[0])/3;
                        // 三通道平均值大于128 则设为白色 否则设为黑色
                        if (bw>=128){
                            bw=255;
                        }else
                            bw=0;
                        bg.setColor(new Color(bw,bw,bw));
                        bg.drawLine(i , j , i , j );
                        //pixelArr[i][j]=bw<<16|bw<<8|bw;
                    }
                }
                jp.getGraphics().drawImage(bi_img,X0,Y0,null);
                imageList.addFirst(bi_img);
            }
            else
                System.out.println("必须先选择一张图片");
        }
        else if (s.equals("马赛克")) {
            if (pixelArr != null) {
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
                for (int i = 0; i < pixelArr.length; i++) {
                    for (int j = 0; j < pixelArr[i].length; j += MOSAIC_SIZE) {
                        int rgb = pixelArr[i][j];
                        Color color = new Color(rgb);
                        bg.setColor(color);
                        bg.fillRect(i, j, MOSAIC_SIZE, MOSAIC_SIZE);
                    }
                }
                jp.getGraphics().drawImage(bi_img, X0, Y0, null);
                imageList.addFirst(bi_img);
            }else
                System.out.println("必须先选择一张图片");
        }
        else if(s.equals("保存图片")){
            JFileChooser jfc2 = new JFileChooser("C:\\Users\\ZDX\\Pictures\\Voto");
            jfc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("png","jpg","png");
            jfc2.setFileFilter(filter);
            jfc2.showSaveDialog(null);
            File fl=jfc2.getSelectedFile(); //选中的文件夹
            if(fl!=null){
            String path = fl.getAbsolutePath()+"\\"+SAVE_NAME+".png";
            try {
                save(path);
                System.out.println("保存成功");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else
                System.out.println("未选取文件");
        }

        else if(s.equals("上一步")){
            if (imageList.getSize()>1){
            this.imageList.deleteFirst();
            BufferedImage temp = this.imageList.getHead().getT();
            System.out.println("效果列表长度："+imageList.getSize());
            jp.getGraphics().drawImage(temp,X0,Y0,null);

        }
            else
                System.out.println("不能回退");
        }

        else
            System.out.println("其他");
    }

    //画图片
    public int[][] getImagePixel(String s) throws Exception {
        //定义二维数组 存储RGB值
        BufferedImage bi ;
        imageList = new Linked<>() ;
        File file = new File(s);
        if (file.exists()) {
            bi = ImageIO.read(file);
            //获得图片长和宽
            int height = bi.getHeight();
            int width = bi.getWidth();
            int[][] pic = new int[width][height];
            bi_img =new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bg = bi_img.getGraphics();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = bi.getRGB(i, j);
                    pic[i][j] = rgb;
                    bg.setColor(new Color(rgb));
                    bg.drawRect(i, j, 1,1);
                }
            }
            System.out.println("画图成功");
            jp.getGraphics().drawImage(bi_img,X0,Y0,null);
            jp.repaint(); //手动刷新解决图片重叠问题
            imageList.addFirst(bi_img);
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

    //限定最大值
    public int limit(int rgb){
        if (rgb>255){
            rgb = 255;
        }else if(rgb<0){
            rgb = 0;
        }
        return rgb;
    }


    public void save(String s) throws IOException {
        output = new File(s);
        ImageIO.write(bi_img, "png", output);
    }
}
