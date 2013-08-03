/**
 *
 */
package celization.buildings;

import celization.GameObjectID;
import java.util.ArrayList;

import celization.GameParameters;
import static celization.buildings.Building.size;

/**
 * @author mjafar
 *
 */
// TODO: code
public final class Port extends Building {

    static {
        size = GameParameters.portSize;
    }
    ArrayList<GameObjectID> boats = new ArrayList<>();

    public Port() {
        super();
        requiredBuildingTime = gameInstance.getParams().portETA;
        requiredResources = GameParameters.portMaterial;
    }

    public void addBoat(GameObjectID b) {
        boats.add(b);
    }

    @Override
    public boolean busy() {
        for (GameObjectID boatID : boats) {
            if (!gameInstance.getBoatByUID(boatID).buildingFinished()) {
                return false;
            }
        }
        return false;
    }

    @Override
    public GameObjectID step() {
        for (GameObjectID boatID : boats) {
            gameInstance.getBoatByUID(boatID).wander();
        }
        return null;
    }
}
