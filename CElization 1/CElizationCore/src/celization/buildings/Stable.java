package celization.buildings;

import celization.GameParameters;
import celization.TurnEvent;
import celization.civilians.soldiers.Soldier;
import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;

/**
 *
 * @author mjafar
 */
public class Stable extends Building {
    private GameObjectID soldierBeingTrained;

    /**
     *
     */
    public Stable() {
        super();
        requiredBuildingTime = GameParameters.stableETA;
        requiredResources = GameParameters.stableMaterial;
        soldierBeingTrained = null;
    }

    @Override
    public boolean busy() {
        return (soldierBeingTrained != null);
    }

    @Override
    public void step() {
        if (!busy()) {
            return;
        }
        Soldier s = gameInstance.getSoldierByUID(soldierBeingTrained);
        s.growUp();
        if (s.isMature()) {
            soldierBeingTrained = null;
            gameInstance.increaseAliveOnes();
            gameInstance.addEvent(new TurnEvent(TurnEvent.TRAINING_FINISHED, "Soldier: " + s.getName()));
        }
    }

    public void startTraining(GameObjectID name) {
        soldierBeingTrained = name;
        gameInstance.addEvent(new TurnEvent(TurnEvent.TRAINING_STARTED, "Soldier: " + gameInstance.getCivilianByUID(name).getName()));
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.stableSize;
    }
}
