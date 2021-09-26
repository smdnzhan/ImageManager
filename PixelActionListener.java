package PCcamera;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    String path = "C:\\Users\\ZDX\\Pictures\\Voto\\timg.jpg";

    //构造方法
    public PixelActionListener(JPanel jp) {
        this.jp = jp;
        //this.g = jp.getGraphics(); 1
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("打开图片")) {
            try {
                pixelArr = getImagePixel(path, jp.getGraphics());//直接传画笔对象会空指针? this.g X
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
        else
            System.out.println("其他");
    }

    //画图片
    public int[][] getImagePixel(String path, Graphics g) throws Exception {
        //定义二维数组 存储RGB值
        File image = new File(path);
        BufferedImage bi = null;
        if (image.exists()) {
            bi = ImageIO.read(image);
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
            System.out.println("重回成功");
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
}
