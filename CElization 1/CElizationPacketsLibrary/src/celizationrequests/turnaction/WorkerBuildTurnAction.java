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
public class WorkerBuildTurnAction extends TurnAction {

    private Coordinates buildingLocation;
    private Class buildingType;

    public WorkerBuildTurnAction(GameObjectID id, Coordinates buildingLocation, Class buildingType) {
        super(id);
        this.buildingLocation = buildingLocation;
        this.buildingType = buildingType;
    }

    public Coordinates getBuildingLocation() {
        return buildingLocation;
    }

    public Class getBuildingType() {
        return buildingType;
    }
}
