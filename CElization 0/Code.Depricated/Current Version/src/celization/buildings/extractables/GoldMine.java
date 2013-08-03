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
public final class GoldMine extends Mine {

    static {
        size = GameParameters.goldMineSize;
    }

    public GoldMine() {
        super();
        size = GameParameters.goldMineSize;
        requiredBuildingTime = gameInstance.getParams().goldMineETA;
        requiredResources = GameParameters.goldMineMaterial;
    }
}
