/**
 *
 */
package celization.buildings.extractables;

import celization.GameParameters;
import static celization.buildings.Building.size;

/**
 * @author mjafar
 *
 */
public final class Farm extends Mine {

    static {
        size = GameParameters.farmSize;
    }

    public Farm() {
        super();
        requiredBuildingTime = GameParameters.farmETA;
        requiredResources = GameParameters.farmMaterial;
    }
}
