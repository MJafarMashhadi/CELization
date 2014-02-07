package celizationserver.core;

import celizationrequests.CELizationRequest;
import celizationserver.swing.FontsLoader;
import celizationserver.swing.ManagerForm;
import java.awt.Dimension;


import celizationserver.swing.SplashScreen;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Starter {

    public static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private SplashScreen frmSplash;
    private ManagerForm frmManager;
    private CElizationServer server;

    public Starter() {
        /* Create and display the form */
        frmSplash = new SplashScreen();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frmSplash.setStatus("Starting Server...");
                Starter.setCenter(frmSplash);
                frmSplash.setVisible(true);
                frmSplash.requestFocus();
                frmSplash.setStatus("Loading saved data");
            }
        });
        try {
            server = CElizationServer.loadGame();
        } catch (FileNotFoundException ex) {
        } catch (java.net.BindException ex) {
            // It means that another instance of server is running on server
            JOptionPane.showMessageDialog(frmSplash, "Server cannot be started. Another program(probably another instance of\n"
                    + "CElization game server) is listening on port " + CELizationRequest.gamesListListeningPort.toString()
                    + "\nProgram will now exit.",
                    "Another instance is running",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (InvalidClassException ex) {
            // Occures when savefile is from an older version
            JOptionPane.showMessageDialog(frmSplash, "Save file on your computer is from an older verrsion of CElization server and we're afraid you cannot use that anymore.\n"
                    + "So copy it somewhere else and wait for adding support of conversion to newer versions. Right now it has to be deleted or moved somewhere else.\n"
                    + "You can find the save file here:\n" + CElizationServer.saveFileAddress, "Version conflict", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frmSplash, "Couldn't load previously saved game session.\n" + ex.getMessage(),
                    "IOException occured while loading",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }
        if (server == null) {
            server = new CElizationServer();
        }

        frmSplash.setStatus("Loading fonts...");
        new FontsLoader();
        
        frmSplash.setStatus("Creating manager panel...");
        frmManager = new ManagerForm(server);

        frmSplash.setStatus("Completed");

        Starter.setCenter(frmManager);

        frmManager.refresh();

        frmManager.setVisible(true);

        frmSplash.dispose();
    }

    public static <T extends java.awt.Window> void setCenter(T frame) {
        frame.setLocation((screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        new Starter();
    }
}
