package celizationclient.backend.net;

import celizationrequests.turnaction.ClearToSendNewTurnsAction;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mjafar
 */
public class ClientListenerThread extends Thread {

    private java.net.Socket connection;
    private LinkedList<Object> responses;
    private ObjectInputStream inputStream = null;
    private boolean continueListening;
    private ArrayList<ResponseListener> responseListeners = new ArrayList<>();

    public ClientListenerThread(Socket connection) {
        super("ClientListenerThread");
        this.connection = connection;
        responses = new LinkedList<>();
        continueListening = true;
    }

    public synchronized boolean hasResponse(Class responseType) {
        try {
            for (int i = 0; i < responses.size(); i++) {
                if (responses.get(i).getClass() == responseType) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public synchronized Object getResponse(Class responseType) {
        for (int i = 0; i < responses.size(); i++) {
            Object response;
            response = responses.get(i);
            if (response.getClass() == responseType) {
                responses.remove(i);
                return response;
            }
        }
        return null;
    }

    public void kill() {
        continueListening = false;
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addResponseListener(Runnable callback, Class type, boolean expires) {
        responseListeners.add(new ResponseListener(callback, type, expires));
    }
    
    public void addResponseListener(Runnable callback, Class type) {
        responseListeners.add(new ResponseListener(callback, type));
    }
    
    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Couldn't get IO Streams to listen", "Darn it! IOException again?!",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ClientListenerThread.class.getName()).log(Level.SEVERE, null, ex);
            continueListening = false;
        }

        while (continueListening) {
            Object responseObj;
            try {
                responseObj = inputStream.readObject();
                responses.addLast(responseObj);
                // Manage listeners
                ResponseListener listener;
                for (ListIterator<ResponseListener> litr = responseListeners.listIterator(); litr.hasNext();) {
                    listener = litr.next();
                    if (listener.getType() == responseObj.getClass()) {
                        listener.callback.run();
                        if (listener.isExpires()) {
                            litr.remove();
                        }
                    }
                }
            } catch (SocketException ex) {
                Logger.getLogger(ClientListenerThread.class.getName()).log(Level.SEVERE, null, ex);
                // FIXME: Happens when we close the game, i guess it should be debugged
            } catch (ClassNotFoundException ex) {
                // Will not happen
            } catch (IOException ex) {
                // Darn it! IOException again?!
                Logger.getLogger(ClientListenerThread.class.getName()).log(Level.SEVERE, null, ex);
                javax.swing.JOptionPane.showMessageDialog(null, "Couldn't read from network socket! That's odd!\n"
                        + "By the way you have lost some information maybe this turn's scene or maybe a chat message\n"
                        + "from a friend! but don't worry, your resources and buildings and other game states are stored in\n"
                        + "server and they're safe if server computer is safe.", "Darn it! IOException again?!",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                kill();
                break;
            } finally {
                System.gc();
            }
        }
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            // Darn it! IOException again?!
        }
    }

    class ResponseListener {

        private boolean expires;
        private Runnable callback;
        private Class type;

        public ResponseListener(Runnable callback, Class type, boolean expires) {
            this.expires = expires;
            this.callback = callback;
            this.type = type;
        }

        public ResponseListener(Runnable callback, Class type) {
            this(callback, type, true);
        }

        public boolean isExpires() {
            return expires;
        }

        public Runnable getCallback() {
            return callback;
        }

        public Class getType() {
            return type;
        }
    }
}
