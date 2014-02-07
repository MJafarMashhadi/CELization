package celization.buildings;

import celization.GameParameters;
import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class Storage extends Building {

    public Storage() {
        super();
        requiredBuildingTime = GameParameters.stockpileETA;
        requiredResources = GameParameters.stockpileMaterial;
    }

    @Override
    public boolean busy() {
        return false;
    }

    @Override
    public void step() {
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.stockpileSize;
    }
}
