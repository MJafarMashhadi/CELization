/**
 *
 */
package celization.civilians.workeractions;

import celization.Coordinates;
import celization.GameObjectID;
import celization.GameState;
import celization.exceptions.IllegalDuplicateException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;

/**
 * @author mjafar
 *
 */
public final class Build extends WorkerActions {

    private GameObjectID UID;
    private Coordinates location;
    private String buildingType;

    /**
     *
     */
    public Build(GameObjectID id, Coordinates l, String type) {
        UID = id;
        location = l;
        buildingType = type;

    }

    public GameObjectID getWorkplace() {
        return UID;
    }

    public void Do(GameState gameInstance) throws IllegalArgumentException, InsufficientResourcesException, InsufficientResearchesException, IllegalDuplicateException {
        // FIXME
        // gameInstance.addEvent(String.format("construction started %s", gameInstance.makeNewBuilding(UID, location, buildingType)));
    }
}
