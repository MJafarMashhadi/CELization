/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package celizationserver.core;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Listener for each game, runs a CELizationServerUserConnectionListener for
 * each connection
 */
class CELizationServerListenerThread extends Thread {
    /**
     * users connections data
     */
    private ExecutorService threadsExecutor = Executors.newCachedThreadPool();
    private final GameSession gameSessionInstance;

    public CELizationServerListenerThread(final GameSession gameSessionInstance) {
        super("Server Listener Thread (" + gameSessionInstance.gameName + ")");
        this.gameSessionInstance = gameSessionInstance;
    }
 
    @Override
    public void run() {
        // Accept connections
        while (!gameSessionInstance.closeConnection) {
            try {
                Socket newConnectionSocket;
                newConnectionSocket = gameSessionInstance.serverListenerSocket.accept();
                threadsExecutor.execute(new CELizationServerUserConnectionListener(newConnectionSocket, gameSessionInstance));
            } catch (java.net.SocketException ex) {
            } catch (IOException ex) {
                Logger.getLogger(GameSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        stopServer();
    }

    public synchronized void stopServer() {
        gameSessionInstance.closeConnection = true;
        Iterator<CELizationServerUserConnectionListener> itr;
        itr = gameSessionInstance.onlineUsers.values().iterator();
        while (itr.hasNext()) {
            itr.next().kill();
            itr = gameSessionInstance.onlineUsers.values().iterator(); // because in kill method onlineUsers
            // map is changed we need to get a new iterator
        }
        try {
            gameSessionInstance.serverListenerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(GameSession.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Failed to close connection.", "Failed to disconnect", JOptionPane.ERROR_MESSAGE);
        }
        try {
            this.interrupt();
            this.join();
        } catch (InterruptedException ex) {
        }
    }

}
