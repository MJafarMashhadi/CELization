/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationserver.core;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author mjafar
 */
public class TurnManager {
    private HashMap<String, Boolean> onlineUsers;

    /**
     * Makes a new turn manager
     */
    public TurnManager() {
        onlineUsers = new HashMap<>();
    }

    /**
     * Add a new user to turns system
     *
     * @param username
     */
    public void addUser(String username) {
        onlineUsers.put(username, Boolean.FALSE);
    }

    /**
     * Remove a user from turn system
     *
     * used when user logs out of the game
     *
     * @param username
     */
    public void removeUser(String username) {
        onlineUsers.remove(username);
    }

    /**
     * Checks if a user can send his/her next turn's actions
     *
     * @param username
     * @return
     */
    public boolean canSendTurnRequest(String username) {
        if (!onlineUsers.containsKey(username)) {
            return false;
        }
        return !onlineUsers.get(username).booleanValue();
    }

    /**
     * Returns true when all users completed their turn action request sendigs
     *
     * @return
     */
    public boolean turnCompleted() {
        for (Boolean completed : onlineUsers.values()) {
            if (completed != Boolean.TRUE) {
                return false;
            }
        }

        return true;
    }

    /**
     * Makes all users free to send turn action request
     */
    public void nextTurn() {
        for(String username : onlineUsers.keySet()) {
            onlineUsers.put(username, Boolean.FALSE);
        }
    }

    /**
     * Called when user has send this turn's actions
     * 
     * @param username
     */
    public void sentAction(String username) {
        onlineUsers.put(username, Boolean.TRUE);
    }
}
