package celizationserver.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 * @author mjafar
 *
 */
public class SplashScreen extends JFrame {

    private static final long serialVersionUID = -6354410580917648055L;
    private static Image backgroundImage = MapViewerPanel.imageRead(SplashScreen.class.getResourceAsStream("splash.png"));
    private String status;

    public SplashScreen() {
        super("Splash");

        int width = backgroundImage.getWidth(this);
        int height = backgroundImage.getHeight(this);

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setBounds(0, 0, width, height);
        this.setLayout(null);
        this.setResizable(false);
        this.setUndecorated(true);
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
        this.repaint(0, 600, getWidth(), getHeight()-600);
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        g.drawImage(backgroundImage, 0, 0, this);
        g.setColor(Color.WHITE);
        g.drawString(status, 30, 645);
    }
}
