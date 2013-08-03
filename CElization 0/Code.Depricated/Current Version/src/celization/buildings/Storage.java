/**
 *
 */
package celization.buildings;

import celization.GameObjectID;
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
        requiredBuildingTime = gameInstance.getParams().stockpileETA;
        requiredResources = GameParameters.stockpileMaterial;
    }

    @Override
    public boolean busy() {
        return false;
    }

    @Override
    public GameObjectID step() {
        return null;
    }
}
