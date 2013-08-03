/**
 *
 */
package celization.mapgeneration;

import celization.CElization;
import celization.Coordinates;
import celization.GameParameters;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public class GameMap implements Serializable {

    /**
     * map matrix
     */
    private LandBlock[][] map;
    /**
     * psudo-random algorithm for map generation
     */
    private PerlinNoiseGenerator noiseGenerator;
    private PerlinNoiseParameters perlinNoiseParameters;
    /**
     * game instance to get necessary parameters within
     */
    private CElization gameInstance;

    public GameMap(CElization gameInstance, PerlinNoiseParameters perlinNoiseParameters, Coordinates gameMapSize) {
        /**
         * initialize game instance
         */
        this.gameInstance = gameInstance;
        GameParameters.gameMapSize = gameMapSize;
        this.perlinNoiseParameters = perlinNoiseParameters;

        /**
         * initialize map variables
         */
        map = new LandBlock[GameParameters.gameMapSize.col][GameParameters.gameMapSize.row];

        /**
         * initialize perlin noise generator
         */
        noiseGenerator = new PerlinNoiseGenerator(this.perlinNoiseParameters);

        /**
         * Process map
         */
        double height;
        BlockType type = BlockType.PLAIN;
        for (int i = 0; i < GameParameters.gameMapSize.col; i++) {
            for (int j = 0; j < GameParameters.gameMapSize.row; j++) {
                /**
                 * Get perlin noise value
                 */
                height = Math.abs(10 + noiseGenerator.get(i, j));
                if (height > 20) {
                    height -= 20;
                }
                /**
                 * Assign each value to a land type
                 */
                if (0 <= height && height < 5) {
                    type = BlockType.WATER;
                } else if (5 <= height && height < 7.5) {
                    type = BlockType.JUNGLE;
                } else if (7.5 <= height && height < 14) {
                    type = BlockType.PLAIN;
                } else if (14 <= height && height <= 20) {
                    type = BlockType.MOUNTAIN;
                }
                /**
                 * make land block
                 */
                map[i][j] = new LandBlock(i, j, type, height,
                        gameInstance.users);
            }
        }

    }

    public void unlockCells(Coordinates topLeft, Coordinates size, String user) {
        for (int i = 0; i <= size.col; i++) {
            for (int j = 0; j <= size.row; j++) {
                try {
                    gameInstance.getPathFinder(user).unlockCell( topLeft.col + i, topLeft.row + j);
                    if (gameInstance.gameMap.get(topLeft.col + i, topLeft.row + j).getType(user) != BlockType.PLAIN) {
                        gameInstance.getPathFinder(user).lockCell(topLeft.col + i, topLeft.row + j);
                    }
                } catch (Exception e) {
                    /* it's just an array out of bound exception which java will
                     * take care of ;)
                     */
                }
            }
        }
    }

    public boolean isAvailable(Coordinates topLeft, Coordinates size, String username) {
        try {
            for (int i = 0; i < size.col; i++) {
                for (int j = 0; j < size.row; j++) {
                    if (!gameInstance.getPathFinder(username).isAvailable(
                            new Coordinates(topLeft.col + i, topLeft.row + j))) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public LandBlock get(int col, int row) {
        return map[col][row];
    }

    public LandBlock get(Coordinates location) {
        return get(location.col, location.row);
    }

    public boolean isWalkable(Coordinates location, String username) {
        return isWalkable(location.col, location.row, username);
    }

    public boolean isWalkable(int col, int row, String username) {
        if (row < 0 || col < 0 || row >= GameParameters.gameMapSize.row
                || col >= GameParameters.gameMapSize.col) {
            return false;
        }

        BlockType blockType = map[col][row].getType(username);
        if (blockType == BlockType.UNKNOWN || blockType == BlockType.PLAIN) {
            return true;
        }

        return false;
    }
}
