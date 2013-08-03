/**
 *
 */
package celization.buildings;

import celization.GameObjectID;
import celization.GameParameters;
import celization.NaturalResources;
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
        requiredBuildingTime = gameInstance.getParams().universityETA;
        requiredResources = GameParameters.universityMaterial;
    }

    public void startResearch(String courseName)
            throws InsufficientResourcesException,
            InsufficientResearchesException, BuildingBusyException {
        if (!gameInstance.getCourseManager().dependencyMet(courseName)) {
            throw new InsufficientResearchesException();
        }
        if (!gameInstance.doWeHave(gameInstance.getCourseManager().get(
                courseName).requiredResources)) {
            throw new InsufficientResourcesException();
        }

        if (this.busy()) {
            throw new BuildingBusyException();
        }
        gameInstance.removeFromResources(gameInstance.getCourseManager()
                .get(courseName).requiredResources);
        gameInstance.getCourseManager().startResearch(courseName);
    }

    public String currentResearch() {
        return gameInstance.getCourseManager().inProgress;
    }

    public void startTrainingStudent(GameObjectID name) {
        studentBeingTrained = name;
    }

    @Override
    public boolean busy() {
        if (gameInstance.getCourseManager().inProgress != null
                || studentBeingTrained != null) {
            return true;
        }
        return false;
    }

    @Override
    public Object step() {
        /**
         * add to knowledge resource
         */
        gameInstance.addToResources(new NaturalResources(0, 0, gameInstance.getNumberOfScholars(true), 0, 0));

        /**
         * progress training students
         */
        if (studentBeingTrained != null) {
            Scholar s = gameInstance.getScholarByUID(studentBeingTrained);

            s.growUp();
            if (s.isMature()) {
                studentBeingTrained = null;
                gameInstance.increaseAliveOnes();
                return s.getID();
            } else {
                return null;
            }
        }

        /**
         * progress researches
         */
        if (gameInstance.getCourseManager().inProgress != null) {
            String finishedReserchName = gameInstance.getCourseManager().progress();
            if (finishedReserchName != null) {
                gameInstance.takeEffectOfResearches(finishedReserchName);
                return finishedReserchName;
            } else {
                return null;
            }
        }

        return null;
    }
}
