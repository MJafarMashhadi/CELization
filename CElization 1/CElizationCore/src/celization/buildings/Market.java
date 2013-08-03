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
public final class Market extends Building {

    static {
        size = GameParameters.marketSize;
    }

    public Market() {
        super();
        requiredBuildingTime = GameParameters.marketETA;
        requiredResources = GameParameters.marketMaterial;
    }

    @Override
    public boolean busy() {
        return false;
    }

    @Override
    public void step() {
    }
}
