/**
 *
 */
package celization.buildings;

import celization.GameObjectID;
import celization.GameParameters;
import static celization.buildings.Building.size;
import celization.civilians.Worker;

/**
 * @author mjafar
 *
 */
public final class MainBuilding extends Building {

    private GameObjectID workerBeingTrained;

    static {
        size = GameParameters.mainBuildingSize;
    }

    /**
     *
     */
    public MainBuilding() {
        super();
        requiredBuildingTime = gameInstance.getParams().mainBuildingETA;
        requiredResources = GameParameters.mainBuildingMaterial;
        workerBeingTrained = null;
    }

    public void startTrainingWorker(GameObjectID name) {
        workerBeingTrained = name;
    }

    @Override
    public boolean busy() {
        return (workerBeingTrained != null);
    }

    @Override
    public GameObjectID step() {
        if (workerBeingTrained == null) {
            return null;
        }

        Worker w = gameInstance.getWorkerByUID(workerBeingTrained);
        w.growUp();
        if (w.isMature()) {
            workerBeingTrained = null;
            gameInstance.increaseAliveOnes();
            return w.getID();
        }

        return null;
    }
}
