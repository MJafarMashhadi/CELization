package celization.buildings.extractables;

import celization.GameParameters;
import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class StoneMine extends Mine {

    public StoneMine() {
        super();
        requiredBuildingTime = GameParameters.stoneMineETA;
        requiredResources = GameParameters.stoneMineMaterial;
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.stoneMineSize;
    }
}
