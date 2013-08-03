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
public final class WoodCamp extends Mine {

    static {
        size = GameParameters.woodCampSize;
    }

    public WoodCamp() {
        super();
        requiredBuildingTime = gameInstance.getParams().woodCampETA;
        requiredResources = GameParameters.woodCampMaterial;
    }
}
