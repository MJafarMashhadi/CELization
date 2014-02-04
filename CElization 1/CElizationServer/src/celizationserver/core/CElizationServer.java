package celizationserver.core;

import celization.UserInfo;
import celization.exceptions.UserExistsException;
import celizationrequests.CELizationRequest;
import celizationrequests.information.GetGamesRequestPacket;
import celizationrequests.information.GetGamesResponsePacket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CELization server application core that stores all game sessions and info
 *
 * @author mjafar
 */
public class CElizationServer implements Serializable {
    private static final long serialVersionUID = -8554634008258696650L;
    transient private GamesListResponderThread gamesListResponder;
    protected static final String saveFileAddress;
    /**
     * Map from ports to Game sessions
     */
    private HashMap<Integer, GameSession> games;

    static {
        saveFileAddress = new StringBuilder()
                .append(System.getProperty("user.home"))
                .append(File.separatorChar)
                .append(".CElization")
                .append(File.separatorChar)
                .append("SaveFile.sav")
                .toString();
    }

    public CElizationServer() {
        this.games = new HashMap<>();
        gamesListResponder = new GamesListResponderThread();
        gamesListResponder.start();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        gamesListResponder = new GamesListResponderThread();
        gamesListResponder.start();
    }

    /**
     * make a new game session
     *
     * @param port TCP port to listen to. if it's 0, java.net will choose a free
     * port automatically
     * @param gameSession a game session
     * @throws IOException Throws when there is a problem in opening and
     * listening to given port
     */
    public void addGame(Integer port, GameSession gameSession) throws IOException {
        gameSession.startListening(port);
        if (port.compareTo(new Integer(0)) == 0) {
            port = gameSession.getPort();
        }
        games.put(port, gameSession);
    }

    /**
     * Register a new user in an existing game session
     *
     * @param info `UserInfo' instance containing username and password and
     * other data about user
     * @param port listening port of the game user is going to be added to
     * @throws UserExistsException when chosen username is repetitive
     */
    public void addUser(UserInfo info, Integer port) throws UserExistsException {
        games.get(port).getGame().userRegister(info);
    }

    /**
     * returns a Set of open ports
     *
     * @return
     */
    public Set<Integer> getGamesPortsSet() {
        return games.keySet();
    }

    /**
     * Get game session by specified port listening to
     *
     * @param port
     * @return
     */
    public GameSession getGame(Integer port) {
        return games.get(port);
    }

    /**
     * Get game session by name
     *
     * @param gameName
     * @return
     */
    public GameSession getGame(String gameName) {
        return getGame(getPort(gameName));
    }

    /**
     * Get game session port by name
     *
     * @param gameName
     * @return
     */
    public Integer getPort(String gameName) {
        for (Integer port : games.keySet()) {
            if (games.get(port).getName().equals(gameName)) {
                return port;
            }
        }

        return null;
    }

    /**
     * Get port of the game session that specified user is playing on
     *
     * @param username
     * @return
     */
    public Integer getUserGamePort(String username) {
        for (Integer port : games.keySet()) {
            if (games.get(port).getGame().isUserInGame(username)) {
                return port;
            }
        }

        return null;
    }

    /**
     * Close all ports and kick all users out of game
     *
     * @throws IOException
     */
    public synchronized void shutDown() throws IOException {
        gamesListResponder.kill();
        Iterator<Integer> itr = games.keySet().iterator();
        while (itr.hasNext()) {
            Integer port = itr.next();
            games.get(port).stopServer();
            itr.remove();
        }
    }

    /**
     * Save all game sessions into a save file
     *
     * @param serverInstance
     * @throws IOException
     */
    public static synchronized void saveGame(CElizationServer serverInstance) throws IOException {
        ObjectOutputStream outputStream;
        File saveFile;

        saveFile = new File(saveFileAddress);
        if (!new File(saveFile.getParent()).exists()) {
            new File(saveFile.getParent()).mkdir();
        }

        outputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
        outputStream.writeObject(serverInstance);
    }

    /**
     * Load game from save file
     *
     * @return
     * @throws IOException
     */
    public static synchronized CElizationServer loadGame() throws IOException {
        ObjectInputStream inputStream;
        File saveFile;
        saveFile = new File(saveFileAddress);
        if (!saveFile.exists()) {
            throw new FileNotFoundException();
        }

        inputStream = new ObjectInputStream(new FileInputStream(saveFile));

        try {
            return (CElizationServer) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    protected class GamesListResponderThread extends Thread {

        protected ServerSocket serverSock;
        protected Socket responderSocket;
        protected ObjectOutputStream oos;
        protected ObjectInputStream ois;
        protected boolean continueListening = true;

        public GamesListResponderThread() {
            super("Games List Responder");
        }

        public void kill() {
            continueListening = false;
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
                if (responderSocket != null) {
                    responderSocket.close();
                }
                if (serverSock != null) {
                    serverSock.close();
                }
            } catch (IOException ex) {
            }
        }

        @Override
        public void run() {
            try {
                while (continueListening) {
                    serverSock = new ServerSocket(CELizationRequest.gamesListListeningPort.intValue());
                    responderSocket = serverSock.accept();
                    if (!continueListening) {
                        return;
                    }
                    oos = new ObjectOutputStream(responderSocket.getOutputStream());
                    ois = new ObjectInputStream(responderSocket.getInputStream());
                    Object input;
                    synchronized (responderSocket) {
                        input = ois.readObject();
                        if (input instanceof GetGamesRequestPacket) {
                            GetGamesResponsePacket response;
                            response = new GetGamesResponsePacket();
                            for (Integer port : games.keySet()) {
                                response.put(games.get(port).getName(), port);
                            }
                            oos.writeObject(response);
                            oos.flush();
                            System.err.println(" << Games list");
                        }
                    }
                    oos.close();
                    ois.close();
                    responderSocket.close();
                    serverSock.close();
                }
            } catch (IOException | ClassNotFoundException ex) {
                if (serverSock.isClosed() || responderSocket.isClosed()) {
                    return;
                }
                Logger.getLogger(CElizationServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (ois != null)
                    ois.close();
                    if (oos !=null)
                    oos.close();
                    if (responderSocket != null)
                    responderSocket.close();
                    if (serverSock != null)
                    serverSock.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
