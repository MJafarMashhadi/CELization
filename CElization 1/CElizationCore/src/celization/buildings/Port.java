package celization.buildings;

import celizationrequests.GameObjectID;
import java.util.ArrayList;

import celization.GameParameters;
import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class Port extends Building {

    ArrayList<GameObjectID> boats = new ArrayList<>();

    public Port() {
        super();
        requiredBuildingTime = GameParameters.portETA;
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
    public void step() {
        for (GameObjectID boatID : boats) {
            gameInstance.getBoatByUID(boatID).wander();
        }
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.portSize;
    }
}
