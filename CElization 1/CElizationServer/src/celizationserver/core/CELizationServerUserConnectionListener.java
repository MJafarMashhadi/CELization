package celizationserver.core;

import celization.NaturalResources;
import celizationrequests.authentication.AuthenticationReportPacket;
import celizationrequests.authentication.AuthenticationRequest;
import celizationrequests.authentication.LogoutPacket;
import celizationrequests.chat.ChatErrorPacket;
import celizationrequests.chat.ChatMessagePacket;
import celizationrequests.chat.OnlineListRequest;
import celizationrequests.chat.OnlineListResponse;
import celizationrequests.information.GetInformationPacket;
import celizationrequests.turnaction.TurnActionsRequest;
import celizationrequests.turnaction.TurnEvents;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Listener/Responder for each user
 */
class CELizationServerUserConnectionListener implements Runnable {

    private Socket connection;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean _continue;
    private boolean loggedIn;
    private String username;
    private final GameSession gameSessionInstance;

    public CELizationServerUserConnectionListener(Socket connection, final GameSession gameSessionInstance) {
        this.gameSessionInstance = gameSessionInstance;
        this.connection = connection;
        _continue = true;
        loggedIn = false;
    }

    public synchronized void kill() {
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
            _continue = false;
            loggedIn = false;
        } catch (IOException ex) {
        }
        if (gameSessionInstance.getOnlineUsers().containsKey(username)) {
            gameSessionInstance.getOnlineUsers().remove(username);
            gameSessionInstance.getTurnManager().removeUser(username);
        }
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            inputStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(GameSession.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Couldn't make input/output streams. User connection failed.", "Failed to get streams", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // get and send data to client
        Object input;
        Class inputType;
        while (_continue) {
            try {
                input = inputStream.readObject();
                inputType = input.getClass();
                //
                if (!loggedIn && inputType != AuthenticationRequest.class) {
                    System.err.println("Not logged in. Bad packet recieved. Shutting thread down.");
                    this.kill();
                    break;
                }
                if (inputType == AuthenticationRequest.class) {
                    System.err.println(" >> Login packet");
                    authenticateUser((AuthenticationRequest) input);
                } else if (inputType == ChatMessagePacket.class) {
                    System.err.printf("%s >> %s chat message\n", username, ((ChatMessagePacket) input).getReciever());
                    gameSessionInstance.sendMessage(username, ((ChatMessagePacket) input).getReciever(), ((ChatMessagePacket) input).getMessage());
                } else if (inputType == GetInformationPacket.class) {
                    System.err.printf("%s >> Get information\n", username);
                    sendObject(gameSessionInstance.game.getUsersList().get(username).getGame());
                } else if (inputType == OnlineListRequest.class) {
                    System.err.printf("%s >> Get online users list\n", username);
                    OnlineListResponse response = new OnlineListResponse();
                    response.addAll(gameSessionInstance.getOnlineUsers().keySet());
                    sendObject(response);
                } else if (inputType == TurnActionsRequest.class) {
                    System.err.printf("%s >> Turn actions\n", username);
                    if (gameSessionInstance.getTurnManager().canSendTurnRequest(username)) {
                        gameSessionInstance.game.processRequests(username, (TurnActionsRequest) input);
                        sendObject(new TurnEvents(gameSessionInstance.game.getEvents(username)));
                        gameSessionInstance.getTurnManager().sentAction(username);
                        checkTurnCompletion();
                    } else {
                        System.err.printf("%s is cheating.\n", username);
                        //  It's not your turn dude
                    }
                } else if (inputType == LogoutPacket.class) {
                    System.err.printf("%s >> Logout packet\n", username);
                    gameSessionInstance.getTurnManager().removeUser(username);
                    checkTurnCompletion();
                    loggedIn = false;
                    this.kill();
                    break; // After this return code will run finally block and will close streams
                } else {
                    System.err.printf(">>! Unsupported request [%s] (%s)\n", username, inputType.getCanonicalName());
                    // Unsupported request
                }
            } catch (IOException | ClassNotFoundException ex) {
                // Unsupported request
            }
        }
    }

    private void sendAuthenticationMessage(boolean status) {
        sendObject(new AuthenticationReportPacket(status));
    }

    public void sendChatMessage(String from, String message) {
        sendObject(new ChatMessagePacket(from, username, message));
    }

    public void sendChatErrorMessage(String username) throws IOException {
        sendObject(new ChatErrorPacket(username));
    }

    public void authenticateUser(AuthenticationRequest input) {
        boolean canLogin = gameSessionInstance.game.userAuthenticate(input);
        if (canLogin == false) {
            sendAuthenticationMessage(false);
            this.kill();
        } else {
            loggedIn = true;
            sendAuthenticationMessage(true);
            this.username = ((AuthenticationRequest) input).getUserName();
            gameSessionInstance.getOnlineUsers().put(username, this);
            gameSessionInstance.getTurnManager().addUser(username);
        }
    }

    private void checkTurnCompletion() {
        if (gameSessionInstance.getTurnManager().turnCompleted()) {
            gameSessionInstance.getTurnManager().nextTurn();
            gameSessionInstance.sendTurnClearancePacket();
            gameSessionInstance.resetTimer();
        }
    }

    protected void sendObject(Object response) {
        try {
            outputStream.writeObject(response);
            outputStream.flush();
            System.err.println(username + " << " + response.getClass().getSimpleName());
        } catch (java.net.SocketException e) {
            if (e.getMessage().toLowerCase().contains("broken pipe") || e.getMessage().toLowerCase().contains("connection reset")) {
                kill();
                System.err.println("Connection was already closed by user");
            }
        } catch (IOException e) {
        }
    }
}
