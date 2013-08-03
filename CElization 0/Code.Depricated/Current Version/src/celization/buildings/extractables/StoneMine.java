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
public final class StoneMine extends Mine {

    static {
        size = GameParameters.stoneMineSize;
    }

    public StoneMine() {
        super();
        requiredBuildingTime = gameInstance.getParams().stoneMineETA;
        requiredResources = GameParameters.stoneMineMaterial;
    }
}
