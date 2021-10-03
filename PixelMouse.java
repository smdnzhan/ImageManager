package PCcamera;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


//实现接口:事件处理类
public class PixelMouse extends MouseAdapter implements PixelConfig{
    private Graphics g;
    private JFrame jf;
    private JPanel jp;
    private PixelActionListener pal;
    public Mosaic mc;

    //初始化画笔
    public PixelMouse(Graphics g,JFrame jf,JPanel jp,PixelActionListener pal) {
        this.g = g;
        this.jf = jf;
        this.jp = jp;
        this.pal = pal;
    }

    public void mouseClicked(MouseEvent e) {
       //int x =e.getX();
       //int y= e.getY();
       /*
       if (x<=pal.pixelArr.length&&x>=X0&&y>=Y0&&y<=pal.pixelArr[0].length&&pal.mc.method){
            this.mc = pal.mc;
            pal.mc.click=true;
            System.out.println("点击在图片上");
            System.out.println(pal.mc.click+" "+pal.mc.method);
            pal.mc.drawMosaic(x,y, jp.getGraphics());
            jp.repaint();
       }

        */
    }

    //根据RGB值 返回三原色值

    }


                //取出所有像素值
                //右移：获取三原色
                //把三原色转成color设置给画笔
                //画像素点

