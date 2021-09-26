package PCcamera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * PC版美颜相机：
 * @author chen
 *
 */
public class PixelUI extends JFrame implements PixelConfig {
    public static void main(String[] args) {
        PixelUI ui = new PixelUI();
        ui.showUI();
    }

    //显示操作界面
    public void showUI() {
        JFrame jf = new JFrame();
        PixelPanel pl = new PixelPanel(jf);
        pl.setBackground(Color.GRAY);
        jf.setSize(1200,1000);
        jf.setTitle("图像处理");

        //设置居中显示
        jf.setLocationRelativeTo(null);
        //设置退出进程
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        Graphics g = pl.getGraphics();
        pl.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jf.add(pl);
        //传递画笔 画笔从JPanel上获得
        PixelMouse mouse = new PixelMouse(g,jf,pl);
        PixelActionListener pal = new PixelActionListener(pl);
        //pal.drawPixel(pal.pixelArr,g);
        //添加鼠标监听器
        jf.addMouseListener(mouse);
    }

}
