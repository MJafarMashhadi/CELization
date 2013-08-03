package celization.equipment;

import celization.CElization;
import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
import celization.GameParameters;
import celization.GameState;
import celization.NaturalResources;
import celization.NeedsGameInstance;
import java.util.LinkedList;

import celization.mapgeneration.BlockType;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public final class Boat implements NeedsGameInstance, Serializable {

    /**
     * Boat Position
     */
    private Coordinates position;
    /**
     * The port that Boat Belongs to
     */
    public GameObjectID portUniqueId;
    /**
     * Game instance
     */
    private GameState gameInstance;
    /**
     * Building steps variables
     */
    private int requiredBuildingTime;
    private int buildingPhase;

    /**
     * Make a new Boat and starts building it
     */
    public Boat() {
        requiredBuildingTime = GameParameters.boatETA;
        buildingPhase = 0;
    }

    /**
     * Assign this Boat to a port
     *
     * @param puid : port uniqueID
     */
    public void addToPort(GameObjectID puid) {
        portUniqueId = puid;
        position = gameInstance.getBuildingByUID(portUniqueId).getLocation();
    }

    /**
     * Inject game instance (implements needsGameInstance)
     */
    @Override
    public void injectGameInstance(GameState game) {
        gameInstance = game;
    }

    /**
     * Move randomly accross the water and collect food
     */
    public void wander() {
        LinkedList<Integer> directions = new LinkedList<>();
        /**
         * *************
         * \ | / * 8 1 2 * --7 . 3--* 6 5 4 * / | \ *
		 **************
         */
        directions.add(1);
        directions.add(2);
        directions.add(3);
        directions.add(4);
        directions.add(5);
        directions.add(6);
        directions.add(7);
        directions.add(8);
        int column, row;
        for (int directionNumber = 0; directionNumber < directions.size(); directionNumber++) {
            if (directionNumber == 1 || directionNumber == 5) {
                column = position.col;
            } else if (2 <= directionNumber && directionNumber <= 4) {
                column = position.col + 1;
            } else {
                column = position.col - 1;
            }

            if (directionNumber == 3 || directionNumber == 7) {
                row = position.row;
            } else if (4 <= directionNumber && directionNumber <= 6) {
                row = position.row + 1;
            } else {
                row = position.row - 1;
            }
            try {
                /**
                 * Remove directions that lead to non-water blocks
                 */
                if (gameInstance.getMapBlock(column, row).getType(gameInstance.getUsername()) != BlockType.WATER) {
                    directions.remove(directionNumber);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                /**
                 * remove out of map directions
                 */
                directions.remove(directionNumber);
            }
        }

        /**
         * Move
         */
        int selectedDirection = directions.get(CElization.rndMaker.nextInt(directions.size()))
                .intValue();

        if (2 <= selectedDirection && selectedDirection <= 4) {
            position.col++;
        } else {
            position.col--;
        }

        if (4 <= selectedDirection && selectedDirection <= 6) {
            position.row++;
        } else {
            position.row--;
        }

        /**
         * collect food
         */
        gameInstance.addToResources(new NaturalResources(0, 0, 0, (int) (GameParameters.boatFoodProduction * gameInstance.getParams().minesExtractionRatioFOOD), 0));
    }

    /**
     * @return the position
     */
    public Coordinates getLocation() {
        return position;
    }

    /**
     * Progress building steps by one step
     */
    public void stepBuilding() {
        if (!buildingFinished()) {
            buildingPhase++;
        }
    }

    /**
     * @return wether building finished
     */
    public boolean buildingFinished() {
        return buildingPhase >= requiredBuildingTime;
    }
}
