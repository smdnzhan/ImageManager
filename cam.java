package PCcamera;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class cam extends Thread{
    public static void main(String[] args)  throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        ImageIO.write(webcam.getImage(), "PNG", new File("C:\\Users\\ZDX\\Pictures\\Voto\\hello-world.png"));
    }

    @Override
    public void run() {
        super.run();
    }
}
