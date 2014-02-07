/**
 *
 */
package celization.buildings;

import java.util.ArrayList;

import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
import celization.GameParameters;
import celization.GameState;
import celization.NaturalResources;
import celization.NeedsGameInstance;
import celization.civilians.Worker;
import celization.civilians.CivilianState;
import celization.exceptions.BuildingFullException;
import celization.exceptions.CannotWorkHereException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author mjafar
 *
 */
public abstract class Building implements NeedsGameInstance, Serializable {

    private static final long serialVersionUID = -117249444872780945L;
    protected Coordinates location;
    public NaturalResources requiredResources;
    public ArrayList<GameObjectID> builderWorkers = new ArrayList<>(1);
    protected GameState gameInstance;
    public ArrayList<GameObjectID> occupiedWorkers = new ArrayList<>(1);
    protected double requiredBuildingTime;
    protected double buildingPhase;

    public abstract Coordinates getSize();
    
    @Override
    public void injectGameInstance(GameState state) {
        gameInstance = state;
        if (gameInstance.getCourseManager().hasBeenDone("Engineering")) {
            requiredBuildingTime *= 0.75;
        }
    }

    public boolean buildStartBuilding(Coordinates position) {
        buildingPhase = 0;
        this.location = position;
        return true;
    }

    public void buildAddBuilderWorker(GameObjectID workerID) {
        builderWorkers.add(workerID);

        Worker worker = gameInstance.getWorkerByUID(workerID);
        worker.workState = CivilianState.Building;
        // a little messy look code
        // to prevent the worker from
        // farther moves and make
        // him/her to stay where he/she is.
        worker.move(worker.getLocation());

        requiredBuildingTime -= (int) (worker
                .getExperience(GameParameters.ExpCivilEng) * 2);
        worker.excersize(GameParameters.ExpCivilEng);
    }

    public void buildAddBuilderWorker(GameObjectID[] workingWorkers) {
        builderWorkers = new ArrayList<>(workingWorkers.length);
        builderWorkers.addAll(Arrays.asList(workingWorkers));

        for (GameObjectID workerName : builderWorkers) {
            Worker worker = gameInstance.getWorkerByUID(workerName);
            // a little messy look code
            // to prevent the worker from
            // farther moves and make
            // him/her to stay where he/she is.
            worker.move(worker.getLocation());

            worker.workState = CivilianState.Building;
            requiredBuildingTime -= worker
                    .getExperience(GameParameters.ExpCivilEng) * 2;
        }
    }

    public void buildStep() {
        /**
         * remove dead workers
         */
        GameObjectID name;
        for (Iterator<GameObjectID> itr = builderWorkers.iterator(); itr.hasNext(); ) {
            name = itr.next();
            if (!gameInstance.getWorkerByUID(name).stillAlive()) {
                builderWorkers.remove(name);
                itr = builderWorkers.iterator();
            }
        }
        /**
         * have alive workers
         */
        if (builderWorkers.size() > 0) {
            buildingPhase += 1;
        }
    }

    public boolean buildBuildingFinished() {
        return buildingPhase >= requiredBuildingTime;
    }

    public void buildBuildingFinishedCallBack() {
        /**
         * free building workers
         */
        for (GameObjectID workerID : builderWorkers) {
            gameInstance.getWorkerByUID(workerID).workState = CivilianState.Free;
            gameInstance.getWorkerByUID(workerID).excersize(
                    GameParameters.ExpCivilEng);
        }
        builderWorkers.clear();

        /**
         * add storage capacities
         */
        if (this instanceof Storage) {
            gameInstance.addToStorageCapacity(GameParameters.StorageExtraCapacity);
        }
    }

    public void sell() {
        gameInstance.addToResources(new NaturalResources(
                requiredResources.numberOfGolds / 2,
                requiredResources.numberOfStones / 2,
                requiredResources.numberOfWoods / 2, requiredResources.numberOfFood / 2, requiredResources.numberOfScience / 2));

        destroy();
    }

    public void addOccupiedWorker(GameObjectID workerID)
            throws BuildingFullException, CannotWorkHereException {
        /**
         * building full
         */
        if (occupiedWorkers.size() == gameInstance.getParams().maximumNumberOfWorkers) {
            throw new BuildingFullException();
        }
        /**
         * Buildings which don't need a worker to work in
         */
        if (this instanceof Port || this instanceof HeadQuarters
                || this instanceof Market || this instanceof University
                || this instanceof Storage) {
            throw new CannotWorkHereException();
        }
        /**
         * Hire the worker
         */
        occupiedWorkers.add(workerID);
    }

    public boolean isActive() {
        /**
         * worker should be in the building
         */
        for (GameObjectID workerName : occupiedWorkers) {
            if (gameInstance.getCivilianByUID(workerName).getLocation().equals(this.location)) {
                return true;
            }
        }

        return false;
    }

    public double getBuildingPhase() {
        return buildingPhase;
    }

    public double getETA() {
        return requiredBuildingTime;
    }

    public Coordinates getLocation() {
        return location;
    }

    public abstract boolean busy();

    public abstract void step();

    public void destroy() {
        if (this instanceof Storage) {
            gameInstance.removeFromStorageCapacity(GameParameters.StorageExtraCapacity);
        }

        for (GameObjectID id : occupiedWorkers) {
            gameInstance.getWorkerByUID(id).cancelAllJobs();
        }
        occupiedWorkers.clear();

        gameInstance.removeBuilding(this);
    }
}
