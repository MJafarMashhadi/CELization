package celization.buildings.extractables;

import celization.GameParameters;
import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class GoldMine extends Mine {

    public GoldMine() {
        super();
        requiredBuildingTime = GameParameters.goldMineETA;
        requiredResources = GameParameters.goldMineMaterial;
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.goldMineSize;
    }
}
