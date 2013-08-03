/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationrequests.turnaction;

import celizationrequests.GameObjectID;


/**
 *
 * @author mjafar
 */
public class WorkerWorkTurnAction extends TurnAction {
    private GameObjectID workPlace;
    
    public WorkerWorkTurnAction(GameObjectID id, GameObjectID workPlace) {
        super(id);
        this.workPlace = workPlace;
    }

    public GameObjectID getWorkPlace() {
        return workPlace;
    }
    
    
    
}
