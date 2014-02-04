package celizationserver.swing;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 * @author mjafar
 *
 */
public class SplashScreen extends JFrame {

    private static Integer backGroundLayer = new Integer(0);
    private static Integer firstLayer = new Integer(1);
    private static final long serialVersionUID = -6354410580917648055L;
    private JLayeredPane layers;
    private ImageIcon backgroundImage;
    private JLabel backgroundContainer;
    private JLabel initializeStatus;

    public SplashScreen() {
        super("Splash");
        backgroundImage = new ImageIcon(getClass().getResource("splash.png"));
        int width = backgroundImage.getIconWidth();
        int height = backgroundImage.getIconHeight();

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setBounds(0, 0, width, height);
        this.setLayout(null);
        this.setResizable(false);
        this.setUndecorated(true);

        backgroundContainer = new JLabel();
        initializeStatus = new JLabel();

        backgroundContainer.setIcon(backgroundImage);
        backgroundContainer.setBounds(0, 0, width, height);

        initializeStatus.setBounds(30, 635, width, 40);
        initializeStatus.setForeground(Color.WHITE);

        layers = new JLayeredPane();
        layers.setBounds(0, 0, width, height);
        layers.add(backgroundContainer, backGroundLayer);
        layers.add(initializeStatus, firstLayer);

        this.add(layers);
    }

    public void setStatus(String newStatus) {
        initializeStatus.setText(newStatus);
    }
}
