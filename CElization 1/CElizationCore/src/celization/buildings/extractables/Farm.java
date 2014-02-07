package celization.buildings.extractables;

import celization.GameParameters;
import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class Farm extends Mine {

    public Farm() {
        super();
        requiredBuildingTime = GameParameters.farmETA;
        requiredResources = GameParameters.farmMaterial;
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.farmSize;
    }
}
