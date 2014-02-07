package celization.buildings;

import celization.GameParameters;
import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class Market extends Building {

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

    @Override
    public Coordinates getSize() {
        return GameParameters.marketSize;
    }
}
