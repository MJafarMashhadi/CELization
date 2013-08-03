/**
 *
 */
package celization.civilians.workeractions;

import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
import celization.GameState;
import celization.TurnEvent;
import celization.exceptions.IllegalDuplicateException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;

/**
 * @author mjafar
 *
 */
public final class Build extends CivilianActions {

    private GameObjectID UID;
    private Coordinates location;
    private Class buildingType;

    /**
     *
     */
    public Build(GameObjectID id, Coordinates l, Class type) {
        UID = id;
        location = l;
        buildingType = type;
    }

    public GameObjectID getWorkplace() {
        return UID;
    }

    public void Do(GameState gameInstance) throws IllegalArgumentException, InsufficientResourcesException, InsufficientResearchesException, IllegalDuplicateException {
        gameInstance.makeNewBuilding(UID, location, buildingType);
        gameInstance.addEvent(new TurnEvent(TurnEvent.CONSTRUCTION_STARTED, buildingType.getSimpleName()));
    }
}
