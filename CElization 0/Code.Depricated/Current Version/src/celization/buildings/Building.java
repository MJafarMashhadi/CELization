/**
 *
 */
package celization.buildings;

import java.util.ArrayList;

import celization.Coordinates;
import celization.GameObjectID;
import celization.GameParameters;
import celization.GameState;
import celization.NaturalResources;
import celization.NeedsGameInstance;
import celization.civilians.Worker;
import celization.civilians.WorkerState;
import celization.exceptions.BuildingFullException;
import celization.exceptions.CannotWorkHereException;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public abstract class Building implements NeedsGameInstance, Serializable {
	public static Coordinates size;
        private Coordinates position;
	public NaturalResources requiredResources;
	public ArrayList<GameObjectID> builderWorkers = new ArrayList<>(1);
	public GameState gameInstance;
	public ArrayList<GameObjectID> occupiedWorkers = new ArrayList<>(1);

	protected double requiredBuildingTime;
	protected double buildingPhase;

	@Override
	public void injectGameInstance(GameState state) {
		gameInstance = state;
	}

	public boolean buildStartBuilding(Coordinates position) {
		buildingPhase = 0;
		this.position = position;
		return true;
	}

	public void buildAddBuilderWorker(GameObjectID workerID) {
		builderWorkers.add(workerID);

		Worker worker = gameInstance.getWorkerByUID(workerID);
		worker.workState = WorkerState.Building;
		// a little messy look code
		// to prevent the worker from
		// farther moves and make
		// him/her to stay where he/she is.
		worker.move(worker.getPos());

		requiredBuildingTime -= (int) (worker
				.getExperience(GameParameters.ExpCivilEng) * 2);
		worker.excersize(GameParameters.ExpCivilEng);
	}

	public void buildAddBuilderWorker(GameObjectID[] workingWorkers) {
		builderWorkers = new ArrayList<GameObjectID>(workingWorkers.length);

		for (GameObjectID id : workingWorkers) {
			builderWorkers.add(id);
		}

		for (GameObjectID workerName : builderWorkers) {
			Worker worker = gameInstance.getWorkerByUID(workerName);
			// a little messy look code
			// to prevent the worker from
			// farther moves and make
			// him/her to stay where he/she is.
			worker.move(worker.getPos());

			worker.workState = WorkerState.Building;
			requiredBuildingTime -= worker
					.getExperience(GameParameters.ExpCivilEng) * 2;
		}
	}

	public void buildStep() {
		/** remove dead workers */
		for (GameObjectID name : builderWorkers) {
			if (!gameInstance.getWorkerByUID(name).stillAlive()) {
				builderWorkers.remove(name);
			}
		}
		/** have alive workers */
		if (builderWorkers.size() > 0) {
			buildingPhase += 1;
		}
	}

	public boolean buildBuildingFinished() {
		return buildingPhase >= requiredBuildingTime;
	}

	public void buildBuildingFinishedCallBack() {
		/** free building workers */
		for (GameObjectID workerID : builderWorkers) {
			gameInstance.getWorkerByUID(workerID).workState = WorkerState.Free;
			gameInstance.getWorkerByUID(workerID).excersize(
					GameParameters.ExpCivilEng);
		}
		builderWorkers.clear();

		/** add storage capacities */
		if (this instanceof Storage) {
			gameInstance.addToStorageCapacity(GameParameters.StorageExtraCapacity);
		}
	}

	public void sell() {
		gameInstance.addToResources(new NaturalResources(
				requiredResources.numberOfFood / 2,
				requiredResources.numberOfGolds / 2,
				requiredResources.numberOfScience / 2,
				requiredResources.numberOfStones / 2,
				requiredResources.numberOfWoods / 2));

		if (this instanceof Storage) {
			gameInstance.removeFromStorageCapacity(GameParameters.StorageExtraCapacity);
		}



		gameInstance.removeBuilding(this);
	}

	public void addOccupiedWorker(GameObjectID workerID)
			throws BuildingFullException, CannotWorkHereException {
		/** building full */
		if (occupiedWorkers.size() == gameInstance.getParams().maximumNumberOfWorkers) {
			throw new BuildingFullException();
		}
		/** Buildings which don't need a worker to work in */
		if (this instanceof Port || this instanceof MainBuilding
				|| this instanceof Market || this instanceof University
				|| this instanceof Storage) {
			throw new CannotWorkHereException();
		}
		/** Hire the worker */
		occupiedWorkers.add(workerID);
	}

	public boolean isActive() {
		/** worker should be in the building */
		for (GameObjectID workerName : occupiedWorkers) {
			if (gameInstance.getCivilianByUID(workerName).getPos()
					.equals(this.position)) {
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
            return position;
        }

	public abstract boolean busy();

	public abstract GameObjectID step();

}
