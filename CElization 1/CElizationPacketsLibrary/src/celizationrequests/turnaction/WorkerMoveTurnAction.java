/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationrequests.turnaction;

import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;

/**
 *
 * @author mjafar
 */
public class WorkerMoveTurnAction extends TurnAction {
    private Coordinates destination;
    
    public WorkerMoveTurnAction(GameObjectID id, Coordinates destination) {
        super(id);
        this.destination = destination.clone();
    }

    public Coordinates getDestination() {
        return destination;
    }
    
}
