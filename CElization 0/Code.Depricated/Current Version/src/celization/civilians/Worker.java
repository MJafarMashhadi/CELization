/**
 *
 */
package celization.civilians;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import celization.Coordinates;
import celization.GameParameters;
import celization.NaturalResources;
import celization.NeedsGameInstance;
import celization.civilians.workeractions.Build;
import celization.civilians.workeractions.EmptyPocket;
import celization.civilians.workeractions.Move;
import celization.civilians.workeractions.WorkerActions;
import celization.exceptions.IllegalDuplicateException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;
import celization.mapgeneration.astar.Node;

/**
 * @author mjafar
 *
 */
public final class Worker extends Civilian implements NeedsGameInstance {
	private Map<String, Experience> experience = new HashMap<String, Experience>();
	private NaturalResources carryingResources;

	public WorkerState workState;
	private String workPlace;

	private LinkedList<WorkerActions> actionsQueue = new LinkedList<WorkerActions>();

	Coordinates nextPos;

	public Worker(String name) {
		super(name);

		workState = WorkerState.Free;

		/** each worker consumes 2 units of food */
		foodConsumption = GameParameters.workerFoodConsumption;

		this.name = name;

		experience.put(GameParameters.ExpAgriculture, new Experience());
		experience.put(GameParameters.ExpGoldMining, new Experience());
		experience.put(GameParameters.ExpStoneMining, new Experience());
		experience.put(GameParameters.ExpCarpentary, new Experience());
		experience.put(GameParameters.ExpCivilEng, new Experience());

		carryingResources = new NaturalResources();

		creationTime = GameParameters.workerETA;
	}

	public void addAction(WorkerActions a) {
		actionsQueue.add(a);
		if (a instanceof Move) {
			getNextMove();
		}
	}

	public void step() {
		if (actionsQueue.size() == 0) {
			return;
		}
		if (isWalking()) {
			stepMove();
		} else if (actionsQueue.getFirst() instanceof Build) {

			try {
				((Build) actionsQueue.getFirst()).Do(gameInstance);
			} catch (IllegalArgumentException e) {
				// won't happen
			} catch (InsufficientResourcesException e) {
				System.err.println("ran out of resources");
			} catch (InsufficientResearchesException e) {
				// won't happen
			} catch (IllegalDuplicateException e) {
				// won't happen
			}
			firstActionDone();
		} else if (actionsQueue.getFirst() instanceof EmptyPocket) {
			gameInstance.addToResources(carryingResources);
			carryingResources.clear();
			firstActionDone();
			step();
		}
	}

	/**
	 * if worker should walk do the moves
	 */
	private void stepMove() {
		if (!isWalking()) {
			return;
		}
		/** go on one step */
		setPosition(nextPos);
		/** unlock visited cells */
		int diff = (GameParameters.workerUnlockArea.row - 1) / 2;
		gameInstance.unlockCells(new Coordinates(position.col - diff, position.row - diff), GameParameters.workerUnlockArea);
		getNextMove();
	}

	public void extract(String materialType, double amount) {
		/** cannot extract */
		// FIXME:comatible with new definitions
		if (!hasCapacity(amount)) {
			return;
		}
		/** gain experience */
		experience.get(materialType).excercise();
		/** put materials in pocket */
		if (materialType.equals(GameParameters.ExpAgriculture)) {
			carryingResources.numberOfFood += amount;
		} else if (materialType.equals(GameParameters.ExpGoldMining)) {
			carryingResources.numberOfGolds += amount;
		} else if (materialType.equals(GameParameters.ExpStoneMining)) {
			carryingResources.numberOfStones += amount;
		} else if (materialType.equals(GameParameters.ExpCarpentary)) {
			carryingResources.numberOfWoods += amount;
		}
		/** pocket will be full in next turn */
		if (!hasCapacity(amount)) {
			actionsQueue.addLast(new Move(gameInstance.getPathFinder().getNearestStorage(position, gameInstance.getBuildingsArray())));
			actionsQueue.addLast(new EmptyPocket());
			actionsQueue.addLast(new Move(position));
		}
	}

	public boolean hasCapacity(double amount) {
		return amount + carryingResources.sum() <= gameInstance.getParams().workerPocketCapacity;
	}

	public void excersize(String type) {
		experience.get(type).excercise();
	}

	public double getExperience(String type) {
		return experience.get(type).amountOfExperience;
	}

	public NaturalResources getInventory() {
		return carryingResources;
	}

        public String getOccupation() {
            return getOccupationForJudge();
        }

	public String getOccupationForJudge(/*String myID*/) {
		// FIXME: i think it's totally wrong!
		switch (workState) {
		case Building:
			return String.format("constructing %s", "something", workPlace);

		case MiningStone:
			return String.format("working at %s %s", "stone mine", workPlace);

		case MiningGold:
			return String.format("working at %s %s", "gold mine", workPlace);

		case WoodCamp:
			return String.format("working at %s %s", "wood camp", workPlace);

		case Farming:
			return String.format("working at %s %s", "farm", workPlace);

		case Free:
			return "idle";
		}

		return null;
	}

	/**
	 * get type for judge UIDs
	 */
	@Override
	public String getTypeString() {
		return "worker";
	}

	/**
	 * set new destination
	 *
	 * @param destination
	 * @throws IllegalArgumentException
	 */
	public void move(Coordinates destination) throws IllegalArgumentException {
		if (destination.row < 0
				|| destination.row >= GameParameters.gameMapSize.row
				|| destination.col < 0
				|| destination.col >= GameParameters.gameMapSize.col) {
			throw new IllegalArgumentException();
		}

		if (isWalking()) {
			// update destination
			int pathfinderIndex = ((Move) actionsQueue.getFirst())
					.getPathFinderIndex();
			gameInstance.getPathFinder().remove(pathfinderIndex);
			actionsQueue.removeFirst();
			actionsQueue.addFirst(new Move(gameInstance.getPathFinder()
					.newPathFinder(position, destination)));
		} else {
			actionsQueue.add(new Move(destination));
		}
		getNextMove();
	}

	/**
	 * get next position according to A* path finding algorithm
	 */
	private void getNextMove() {
		boolean haveMoveInQueue = false;
		Node whereToGo = null;
		for (WorkerActions firstAction : actionsQueue) {
			if (firstAction instanceof Move) {
				int index = ((Move) firstAction).getPathFinderIndex();
				if (index == -1) {
					actionsQueue.removeFirst();
					actionsQueue.addFirst(new Move(gameInstance.getPathFinder()
							.newPathFinder(position,
									((Move) firstAction).getDestination())));
					index = ((Move) firstAction).getPathFinderIndex();
				}
				whereToGo = gameInstance.getPathFinder().getNextMove(index);
				haveMoveInQueue = true;
				if (whereToGo == null) {
					haveMoveInQueue = false;
					actionsQueue.removeFirst();
				}
			}
		}
		if (!haveMoveInQueue) {
			return;
		}

		nextPos = whereToGo.toCoordinates();
	}

	/**
	 * whether worker is in an moving action
	 *
	 * @return
	 */
	private boolean isWalking() {
		if (actionsQueue.size() == 0) {
			return false;
		}
		return actionsQueue.getFirst() instanceof Move;
	}

	/**
	 * removes action from list when doing an action is completed
	 */
	private void firstActionDone() {
		if (actionsQueue.size() > 0) {
			actionsQueue.removeFirst();
		}
	}
}