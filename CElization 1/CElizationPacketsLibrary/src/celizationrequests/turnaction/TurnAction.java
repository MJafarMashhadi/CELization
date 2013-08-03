/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationrequests.turnaction;

import celizationrequests.GameObjectID;
import java.io.Serializable;

/**
 *
 * @author mjafar
 */
public abstract class TurnAction implements Serializable {
    protected GameObjectID target;

    /**
     * Make an action by setting it's id
     * @param id
     */
    public TurnAction(GameObjectID id) {
        target = id;
    }

    /**
     * Get target id
     * @return
     */
    public GameObjectID getTarget() {
        return target;
    }
}
