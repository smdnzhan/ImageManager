package PCcamera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PixelPanel extends JPanel implements PixelConfig {
    public JFrame jf;
    public Graphics g;
    public PixelActionListener pal;

    public PixelPanel(JFrame jf){
        this.g = jf.getGraphics();
        this.jf = jf;
        this.setOpaque(false);
        this.pal = new PixelActionListener(jf);
        setButtons(jf,pal);
    }

    //根据按钮列表 添加按钮
    public void setButtons(JFrame jf, ActionListener al){
        for (String buttonName : BUTTON_NAMES) {
            JButton jbt = new JButton(buttonName);
            jbt.setFont(createFont());
            jbt.addActionListener(al);
            jbt.setPreferredSize(new Dimension(150, 100));
            this.add(jbt);
        }
    }

    public Font createFont(){
        return new Font(LETTER,Font.BOLD,LETTER_SIZE);
    }

    /*
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //if (pal.pixelArr!=null){ pal.drawPixel(pal.pixelArr, this.g);}
    }

     */
}
