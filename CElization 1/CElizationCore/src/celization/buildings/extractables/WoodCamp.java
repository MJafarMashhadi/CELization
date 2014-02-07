package celization.buildings.extractables;

import celization.GameParameters;
import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class WoodCamp extends Mine {

    public WoodCamp() {
        super();
        requiredBuildingTime = GameParameters.woodCampETA;
        requiredResources = GameParameters.woodCampMaterial;
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.woodCampSize;
    }
}
