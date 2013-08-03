/**
 *
 */
package celization.buildings;

import celizationrequests.GameObjectID;
import celization.GameParameters;
import static celization.buildings.Building.size;

/**
 * @author mjafar
 *
 */
public final class Storage extends Building {

    static {
        size = GameParameters.stockpileSize;
    }

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
}
