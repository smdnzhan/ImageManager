package PCcamera;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


//实现接口:事件处理类
public class PixelMouse extends MouseAdapter implements PixelConfig{
    private Graphics g;
    int[][] pixelArr = null;

    //初始化画笔
    public PixelMouse(Graphics g) {
        this.g = g;
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

    }

    //根据RGB值 返回三原色值

    }


                //取出所有像素值
                //右移：获取三原色
                //把三原色转成color设置给画笔
                //画像素点

