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
        jf.setSize(800,800);
        jf.setTitle("图像处理");

        //设置居中显示
        jf.setLocationRelativeTo(null);
        //设置退出进程
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        Graphics g = jf.getGraphics();

        jf.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jf.add(pl,CENTER_ALIGNMENT);
        //传递画笔
        PixelMouse mouse = new PixelMouse(g);
        PixelActionListener pal = new PixelActionListener(jf);

        //添加鼠标监听器
        jf.addMouseListener(mouse);
    }

}
