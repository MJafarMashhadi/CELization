/**
 *
 */
package celization.civilians;

import java.util.HashMap;
import java.util.Map;

import celization.GameParameters;
import celization.NaturalResources;
import celization.NeedsGameInstance;
import celization.civilians.workeractions.Build;
import celization.civilians.workeractions.EmptyPocket;
import celization.civilians.workeractions.Move;
import celization.exceptions.IllegalDuplicateException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;

/**
 * @author mjafar
 *
 */
public final class Worker extends ActiveCivilians implements NeedsGameInstance {

    private Map<String, Experience> experience = new HashMap<String, Experience>();
    private NaturalResources carryingResources;

    public Worker(String name) {
        super(name);

        workState = CivilianState.Free;

        /**
         * each worker consumes 2 units of food
         */
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

    public void extract(String materialType, double amount) {
        /**
         * cannot extract
         */
        if (!hasCapacity(amount)) {
            return;
        }
        /**
         * gain experience
         */
        experience.get(materialType).excercise();
        /**
         * put materials in pocket
         */
        switch (materialType) {
            case GameParameters.ExpAgriculture:
                carryingResources.numberOfFood += amount;
                break;
            case GameParameters.ExpGoldMining:
                carryingResources.numberOfGolds += amount;
                break;
            case GameParameters.ExpStoneMining:
                carryingResources.numberOfStones += amount;
                break;
            case GameParameters.ExpCarpentary:
                carryingResources.numberOfWoods += amount;
                break;
        }
        /**
         * pocket will be full in next turn
         */
        if (!hasCapacity(amount)) {
            actionsQueue.addLast(new Move(gameInstance.getPathFinder().getNearestStorage(location, gameInstance.getBuildings().values())));
            actionsQueue.addLast(new EmptyPocket());
            actionsQueue.addLast(new Move(location));
        }
    }

    @Override
    public void step() {
        super.step();
        if (actionsQueue.size() == 0) {
            return;
        }
        if (actionsQueue.getFirst() instanceof Build) {
            try {
                ((Build) actionsQueue.getFirst()).Do(gameInstance);
            } catch (IllegalArgumentException | InsufficientResearchesException | IllegalDuplicateException e) {
                // won't happen
            } catch (InsufficientResourcesException e) {
                System.err.println("ran out of resources");
            }
            firstActionDone();
        } else if (actionsQueue.getFirst() instanceof EmptyPocket) {
            gameInstance.addToResources(carryingResources);
            carryingResources.clear();
            firstActionDone();
            step();
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
}