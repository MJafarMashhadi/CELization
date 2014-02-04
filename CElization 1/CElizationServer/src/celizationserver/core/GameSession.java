/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationserver.core;

import celization.CElization;
import celization.NaturalResources;
import celizationrequests.authentication.AuthenticationRequest;
import celizationrequests.authentication.AuthenticationReportPacket;
import celizationrequests.authentication.LogoutPacket;
import celizationrequests.chat.ChatErrorPacket;
import celizationrequests.chat.ChatMessagePacket;
import celizationrequests.chat.OnlineListRequest;
import celizationrequests.chat.OnlineListResponse;
import celizationrequests.information.GetInformationPacket;
import celizationrequests.turnaction.ClearToSendNewTurnsAction;
import celizationrequests.turnaction.TurnActionsRequest;
import celizationrequests.turnaction.TurnEvents;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author mjafar
 */
public class GameSession implements Serializable {
    private static final long serialVersionUID = 4936046175409539978L;

    // Game data
    protected String gameName;
    protected CElization game;
    protected int listeningPort;
    //
    private transient Timer turnTimeLimitTimer;
    private transient TurnTimeLimitTask turnTimeLimit;
    // Network data - should not be srialized
    protected boolean closeConnection;
    transient protected HashMap<String, CELizationServerUserConnectionListener> onlineUsers;
    transient protected CELizationServerListenerThread serverListenerThread;
    transient protected ServerSocket serverListenerSocket;
    transient protected TurnManager turnManager;

    public GameSession(String name, CElization game) {
        this.onlineUsers = new HashMap<>();
        this.closeConnection = true;
        gameName = name;
        turnManager = new TurnManager();
        this.game = game;
        resetTimer();
    }

    public void startListening(Integer port) throws IOException {
        serverListenerSocket = new ServerSocket(port.intValue());
        closeConnection = false;
        serverListenerThread = new CELizationServerListenerThread(this);
        serverListenerThread.start();
        listeningPort = serverListenerSocket.getLocalPort();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.onlineUsers = new HashMap<>();
        turnManager = new TurnManager();
        startListening(listeningPort);
        resetTimer();
    }

    public void removeUser(String username) {
        if (onlineUsers.containsKey(username)) {
            onlineUsers.get(username).kill();
        }
        if (game.getUsersList().containsKey(username)) {
            game.getUsersList().remove(username);
            game.getGameMap().removeUser(username);
        }
        System.gc();
    }

    public String getName() {
        return gameName;
    }

    public CElization getGame() {
        return game;
    }

    public int getOnlineCount() {
        return onlineUsers.size();
    }

    public Set<String> getOnlineSet() {
        return onlineUsers.keySet();
    }

    public void stopServer() throws IOException {
        serverListenerThread.stopServer();
    }

    public int getPort() {
        return listeningPort;
    }

    public void sendMessage(String from, String to, String message) throws IOException {
        if (to == null) {
            for (CELizationServerUserConnectionListener user : onlineUsers.values()) {
                user.sendChatMessage(from, message);
            }
        } else {
            if (onlineUsers.containsKey(to)) {
                onlineUsers.get(to).sendChatMessage(from, message);
            } else {
                onlineUsers.get(from).sendChatErrorMessage(to);
            }
        }
    }

    public void kickUser(String username) {
        onlineUsers.get(username).kill();
    }

    protected void sendTurnClearancePacket() {
        ClearToSendNewTurnsAction clearance;
        clearance = new ClearToSendNewTurnsAction();
        try {
            for (CELizationServerUserConnectionListener userConnection : onlineUsers.values()) {
                userConnection.sendObject(clearance);
            }
        } catch (java.util.ConcurrentModificationException e) {
            sendTurnClearancePacket();
        }
    }

    protected void resetTimer() {
        if (turnTimeLimitTimer != null) {
            turnTimeLimitTimer.cancel();
            turnTimeLimitTimer = null;
        }
        turnTimeLimitTimer = new Timer();
        turnTimeLimit = new TurnTimeLimitTask();
        turnTimeLimitTimer.schedule(turnTimeLimit, 0, 60 * 1000);
    }

    public HashMap<String, CELizationServerUserConnectionListener> getOnlineUsers() {
        return onlineUsers;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }
    
    private class TurnTimeLimitTask extends TimerTask {

        @Override
        public void run() {
            sendTurnClearancePacket();
            turnManager.nextTurn();
        }
    }
}
