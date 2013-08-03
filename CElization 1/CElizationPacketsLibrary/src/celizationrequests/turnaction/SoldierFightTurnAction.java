package celizationrequests.turnaction;

import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;

/**
 *
 * @author MohammadJafar MashhadiEbrahim
 */
public class SoldierFightTurnAction extends TurnAction {
    private Coordinates targetLocation;

    public SoldierFightTurnAction(GameObjectID soldierID, Coordinates targetLocation) {
        super(soldierID);
        this.targetLocation = targetLocation;
    }

    public Coordinates getTargetLocation() {
        return targetLocation;
    }
    
}
