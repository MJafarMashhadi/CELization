/**
 *
 */
package celization.buildings;

import celizationrequests.GameObjectID;
import celization.GameParameters;
import celization.TurnEvent;
import static celization.buildings.Building.size;
import celization.civilians.Worker;

/**
 * @author mjafar
 *
 */
public final class HeadQuarters extends Building {

    private GameObjectID workerBeingTrained;

    static {
        size = GameParameters.headQuartersSize;
    }

    /**
     *
     */
    public HeadQuarters() {
        super();
        requiredBuildingTime = GameParameters.headQuartersETA;
        requiredResources = GameParameters.headQuartersMaterial;
        workerBeingTrained = null;
    }

    public void startTraining(GameObjectID name) {
        workerBeingTrained = name;
        gameInstance.addEvent(new TurnEvent(TurnEvent.TRAINING_STARTED, "Worker: " + gameInstance.getCivilianByUID(name).getName()));
    }

    @Override
    public boolean busy() {
        return (workerBeingTrained != null);
    }

    @Override
    public void step() {
        if (!busy()) {
            return;
        }
        Worker w = gameInstance.getWorkerByUID(workerBeingTrained);
        w.growUp();
        if (w.isMature()) {
            workerBeingTrained = null;
            gameInstance.increaseAliveOnes();
            gameInstance.addEvent(new TurnEvent(TurnEvent.TRAINING_FINISHED, "Worker: " + w.getName()));
        }
    }
}
