package celization.buildings;

import celizationrequests.GameObjectID;
import celization.GameParameters;
import celization.TurnEvent;
import celization.civilians.soldiers.Soldier;
import celizationrequests.Coordinates;

/**
 *
 * @author mjafar
 */
public class Barracks extends Building {

    private GameObjectID soldierBeingTrained;

    /**
     *
     */
    public Barracks() {
        super();
        requiredBuildingTime = GameParameters.barracksETA;
        requiredResources = GameParameters.barracksMaterial;
        soldierBeingTrained = null;
    }

    public void startTrainingSoldier(GameObjectID name) {
        gameInstance.addEvent(new TurnEvent(TurnEvent.TRAINING_STARTED, "Soldier: " + gameInstance.getCivilianByUID(name).getName()));
        soldierBeingTrained = name;
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

    public void startTraining(GameObjectID solider) {
        soldierBeingTrained = solider;
    }

    @Override
    public Coordinates getSize() {
        return GameParameters.barracksSize;
    }
}
