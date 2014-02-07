/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.backend.net;

import celization.GameState;
import celization.TurnEvent;
import celizationclient.frontend.FormsParent;
import celizationclient.frontend.GameMainFrameController;
import celizationrequests.CELizationRequest;
import celizationrequests.authentication.AuthenticationReportPacket;
import celizationrequests.authentication.AuthenticationRequest;
import celizationrequests.authentication.LogoutPacket;
import celizationrequests.chat.ChatErrorPacket;
import celizationrequests.chat.ChatMessagePacket;
import celizationrequests.information.GetGamesRequestPacket;
import celizationrequests.information.GetGamesResponsePacket;
import celizationrequests.turnaction.ClearToSendNewTurnsAction;
import celizationrequests.turnaction.TurnAction;
import celizationrequests.turnaction.TurnActionsRequest;
import celizationrequests.turnaction.TurnEvents;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author mjafar
 */
public class CElizationClient {

    private Socket connection;
    private ClientListenerThread listenerThread;
    private boolean clearToSendNextTurnActions;
    private TurnActionsRequest turnRequest;
    private String username;
    private static CElizationClient instance;
    private ObjectOutputStream outputStream = null;
    //
    private GameState currentGameState;
    private boolean gameStateUpdated;
    //
    private GameMainFrameController mainFrameController;

    /**
     * Setter for forms parent
     *
     * @param mainFrameController
     */
    public void setMainFrameController(GameMainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

    /**
     * Get instance of class. It's a singleton class
     *
     * @return
     */
    public static CElizationClient getInstance() {
        if (instance == null) {
            instance = new CElizationClient();
        }

        return instance;
    }

    /**
     * push notification meaning that we're able to send new turn actions
     */
    protected void clearToSend() {
        if (mainFrameController != null) {
            clearToSendNextTurnActions = true;
            gameStateUpdated = true;
            mainFrameController.clearToSend();
        }
    }

    /**
     * Private constructor
     */
    private CElizationClient() {
        clearToSendNextTurnActions = true;
        gameStateUpdated = true;
        turnRequest = new TurnActionsRequest();
    }

    /**
     * starts connection to server then starts listening thread to catch all
     * incoming packets
     *
     * @param ip
     * @param port
     * @throws IOException
     */
    public void startConnection(String ip, int port) throws IOException {
        connection = new Socket(ip, port);
        listenerThread = new ClientListenerThread(connection);
        listenerThread.start();
        try {
            outputStream = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(CElizationClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        listenerThread.addResponseListener(new Runnable() {
            @Override
            public void run() {
                int oldTurnNumber;
                if (currentGameState == null) {
                    oldTurnNumber = -1;
                } else {
                    oldTurnNumber = currentGameState.getTurnNumber();
                }
                currentGameState = (GameState) listenerThread.getResponse(GameState.class);
                gameStateUpdated = false;
                if (oldTurnNumber < currentGameState.getTurnNumber()) {
                    mainFrameController.refreshInfo();
                } else {
                    System.err.printf("An old game state(%d <= %d) was recieved with no reason.\n", currentGameState.getTurnNumber(), oldTurnNumber);
                }
            }
        }, GameState.class, false);

        listenerThread.addResponseListener(new Runnable() {
            @Override
            public void run() {
                ClearToSendNewTurnsAction clearance = (ClearToSendNewTurnsAction) listenerThread.getResponse(ClearToSendNewTurnsAction.class);
                System.out.println("Got clearance for turn " + clearance.getThisTurnNumber());
                if (currentGameState == null || (currentGameState != null && currentGameState.getTurnNumber() < clearance.getThisTurnNumber())) {
                    try {
                        requestGameStateUpdate();
                    } catch (IOException ex) {
                        Logger.getLogger(CElizationClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                clearToSend();
            }
        }, ClearToSendNewTurnsAction.class, false);

        listenerThread.addResponseListener(new Runnable() {
            @Override
            public void run() {
                ArrayList<TurnEvent> events = (ArrayList<TurnEvent>) ((TurnEvents) listenerThread.getResponse(TurnEvents.class)).getEvents();
                mainFrameController.setTurnEvents(events);
            }
        }, TurnEvents.class, false);

        listenerThread.addResponseListener(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> usersList = null;
                try {
                    usersList = getOnlineUsers();
                } catch (SocketException ex) {
                    Logger.getLogger(CElizationClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CElizationClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (usersList != null) {
                    mainFrameController.setOnlineList(usersList);
                } else {
                    System.err.println("usersList == null. This should not happen! because it's a listener not a random polling mode checker.");
                }
            }
        }, celizationrequests.chat.OnlineListResponse.class, false);

        listenerThread.addResponseListener(new Runnable() {
            @Override
            public void run() {
                ChatMessagePacket messagePacket = (ChatMessagePacket) listenerThread.getResponse(ChatMessagePacket.class);
                mainFrameController.newMessage(messagePacket.getSender(), messagePacket.getMessage());
                mainFrameController.refreshMessagesCounter();
            }
        }, ChatMessagePacket.class, false);

        listenerThread.addResponseListener(new Runnable() {
            @Override
            public void run() {
                ChatErrorPacket error;
                error = (ChatErrorPacket) listenerThread.getResponse(ChatErrorPacket.class);
                mainFrameController.userWentOffline(error.getUsername());
                mainFrameController.refreshMessagesCounter();
            }
        }, ChatErrorPacket.class, false);
    }

    public void logout() {
        try {
            sendRequest(new LogoutPacket());
            listenerThread.kill();
            outputStream.close();
            connection.close();
        } catch (IOException ex) {
        } finally {
            Platform.exit();
        }
    }

    public boolean login(String username, String password) throws IOException {
        Object response;
        this.username = username;
        sendRequest(new AuthenticationRequest(username, password));
        int t = 0;
        while (!listenerThread.hasResponse(AuthenticationReportPacket.class)) {
            t++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("Wagh Wagh");
            }
            if (t > 100) {
                throw new IOException("Timeout");
            }
        }
        return ((AuthenticationReportPacket) listenerThread.getResponse(AuthenticationReportPacket.class)).isSuccessful();
    }

    /**
     * Send get game state request and wait for response
     *
     * @return
     */
    public celization.GameState getGameState() throws IOException {
        return currentGameState;
    }

    public void requestGameStateUpdate() throws IOException {
        sendRequest(new celizationrequests.information.GetInformationPacket());
    }

    public ArrayList<String> getOnlineUsers() throws IOException {
        return ((celizationrequests.chat.OnlineListResponse) listenerThread.getResponse(celizationrequests.chat.OnlineListResponse.class)).getList();
    }

    public void addTurnAction(TurnAction action) {
        turnRequest.addRequest(action);
    }

    public void sendTurnActions() throws NotReadyForNextTurnException, IOException {
        if (!isClearToSendNextTurnActions()) {
            throw new NotReadyForNextTurnException();
        }
        clearToSendNextTurnActions = false;
        sendRequest(turnRequest);
    }

    /**
     * Starts a new request sending thread
     *
     * @param request
     */
    private void sendRequest(CELizationRequest request) throws IOException {
        outputStream.writeUnshared(request);
        outputStream.flush();
    }

    /**
     * Availability of sending next turn actions
     *
     * @return
     */
    public boolean isClearToSendNextTurnActions() {
        return clearToSendNextTurnActions;
    }

    /**
     * A getter for username
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Connects to server on predefined port and gets Games names and ports
     * associated to them list
     *
     * @param ip
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public HashMap<String, Integer> getGamesList(String ip) throws UnknownHostException, IOException {
        HashMap<String, Integer> retVal;
        retVal = new HashMap<>();
        Object response;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Socket gamesListRetrieverConnection;

        GetGamesRequestPacket request = new GetGamesRequestPacket();
        gamesListRetrieverConnection = new Socket(ip, CELizationRequest.gamesListListeningPort);
        try {
            objectOutputStream = new ObjectOutputStream(gamesListRetrieverConnection.getOutputStream());
            objectInputStream = new ObjectInputStream(gamesListRetrieverConnection.getInputStream());

            try {
                objectOutputStream.writeUnshared(request);
                objectOutputStream.flush();
                response = objectInputStream.readObject();

                if (response.getClass() == GetGamesResponsePacket.class) {
                    retVal = ((GetGamesResponsePacket) response).getGamesList();
                } else {
                    throw new IOException("Unknown Response");
                }
            } catch (IOException ex) {
                Logger.getLogger(CElizationClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
            }
        } catch (IOException ex) {
            Logger.getLogger(CElizationClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("No Response", ex);
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (gamesListRetrieverConnection != null) {
                    gamesListRetrieverConnection.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(CElizationClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            return retVal;
        }
    }

    /**
     * Getter for listener thread
     *
     * @return
     */
    public ClientListenerThread getListenerThread() {
        return listenerThread;
    }

    public void sendChatMessage(String reciever, String text) throws IOException {
        ChatMessagePacket messagePacket;
        messagePacket = new ChatMessagePacket(username, reciever, text);
        sendRequest(messagePacket);
    }
}
