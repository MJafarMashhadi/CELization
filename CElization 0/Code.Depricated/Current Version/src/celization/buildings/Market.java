/**
 *
 */
package celization.buildings;

import celization.GameObjectID;
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
		requiredBuildingTime = gameInstance.getParams().marketETA;
		requiredResources = GameParameters.marketMaterial;
	}

	@Override
	public boolean busy() {
		return false;
	}

	@Override
	public GameObjectID step() {
		return null;
	}
}
