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
    public JFrame jf;// 1
    public int[][] pixelArr = null; //rgb值
    String path = "C:\\Users\\ZDX\\Pictures\\Voto\\timgNM8XTNF2.jpg";

    //构造方法
    public PixelActionListener(JFrame jf) {
        this.jf = jf;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("打开图片")) {
            try {
                pixelArr = getImagePixel(path, jf.getGraphics());//直接传画笔对象会空指针?
                System.out.println("正常执行");
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        } else
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
                    int[] temp = getAllColor(pic[i][j]);
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
        int[] rgb = new int[3];
        rgb[0] = (image & 0xff0000) >> 16; //对应红色部分 右移16位
        rgb[1] = (image & 0xff00) >> 8; // 绿色部分
        rgb[2] = (image & 0xff); //蓝色部分
        return rgb;
    }

    public void drawPixel(int[][] image, Graphics g) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                int[] temp = getAllColor(image[i][j]);
                g.setColor(new Color(temp[0], temp[1], temp[2]));
                g.drawLine(i + X0, j + Y0, i + X0, j + Y0);
            }
        }
    }
}
