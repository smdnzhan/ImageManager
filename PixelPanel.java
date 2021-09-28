package PCcamera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PixelPanel extends JPanel implements PixelConfig {
    public JFrame jf;
    public Graphics g;
    public PixelActionListener pal;

    public PixelPanel(JFrame jf){
        this.jf = jf;
        this.setOpaque(false);
        this.pal = new PixelActionListener(this);
        setButtons(this,pal);
    }

    //根据按钮列表 添加按钮
    public void setButtons(JPanel jp,ActionListener al){ //此方法不用重写在paint中 因为是组件
        for (String buttonName : BUTTON_NAMES) {
            JButton jbt = new JButton(buttonName);
            jbt.setFont(createFont());
            jbt.addActionListener(al);
            jbt.setPreferredSize(new Dimension(120, 100));
            jp.add(jbt);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        pal.drawPixel(pal.pixelArr,g);
    }

    public Font createFont(){
        return new Font(LETTER,Font.BOLD,LETTER_SIZE);
    }

}
