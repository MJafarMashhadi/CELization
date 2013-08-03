/**
 *
 */
package celization.buildings;

import celizationrequests.GameObjectID;
import celization.GameParameters;
import celization.NaturalResources;
import celization.TurnEvent;
import static celization.buildings.Building.size;
import celization.civilians.Scholar;
import celization.exceptions.BuildingBusyException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;

/**
 * @author mjafar
 *
 */
public final class University extends Building {

    private GameObjectID studentBeingTrained;

    static {
        size = GameParameters.universitySize;
    }

    /**
     *
     */
    public University() {
        super();
        studentBeingTrained = null;
        requiredBuildingTime = GameParameters.universityETA;
        requiredResources = GameParameters.universityMaterial;
    }

    public void startResearch(String courseName)
            throws InsufficientResourcesException,
            InsufficientResearchesException, BuildingBusyException {
        if (!gameInstance.getCourseManager().dependencyMet(courseName)) {
            throw new InsufficientResearchesException();
        }

        if (this.busy()) {
            throw new BuildingBusyException();
        }
        gameInstance.getNaturalResources().subtract(gameInstance.getCourseManager().get(courseName).requiredResources);
        gameInstance.getCourseManager().startResearch(courseName);
        gameInstance.addEvent(new TurnEvent(TurnEvent.RESEARCH_STARTED, courseName));
    }

    public String currentResearch() {
        return gameInstance.getCourseManager().getName(gameInstance.getCourseManager().getCurrent());
    }

    public void startTraining(GameObjectID name) {
        studentBeingTrained = name;
        gameInstance.addEvent(new TurnEvent(TurnEvent.TRAINING_STARTED, "Student: " + gameInstance.getCivilianByUID(name).getName()));
    }

    @Override
    public boolean busy() {
        if (gameInstance.getCourseManager().isBusy() || studentBeingTrained != null) {
            return true;
        }
        return false;
    }

    @Override
    public void step() {
        /**
         * add to knowledge resource
         */
        gameInstance.addToResources(new NaturalResources(0, 0, 0, 0, gameInstance.getNumberOfScholars(true)));

        if (!busy()) {
            return;
        }

        /**
         * progress training students
         */
        if (studentBeingTrained != null) {
            Scholar s = gameInstance.getScholarByUID(studentBeingTrained);

            s.growUp();
            if (s.isMature()) {
                studentBeingTrained = null;
                gameInstance.increaseAliveOnes();
                gameInstance.addEvent(new TurnEvent(TurnEvent.TRAINING_FINISHED, "Student: " + s.getName()));
            }
        }

        /**
         * progress researches
         */
        if (gameInstance.getCourseManager().isBusy()) {
            GameObjectID finishedReserchID = gameInstance.getCourseManager().progress();
            if (finishedReserchID != null) {
                gameInstance.takeEffectOfResearches(gameInstance.getCourseManager().getName(finishedReserchID));
                gameInstance.addEvent(new TurnEvent(TurnEvent.RESEARCH_FINISHED, gameInstance.getCourseManager().getName(finishedReserchID)));
            }
        }
    }
}
