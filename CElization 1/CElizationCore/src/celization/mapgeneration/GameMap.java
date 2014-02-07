/**
 *
 */
package celization.mapgeneration;

import celization.mapgeneration.perlinnoise.PerlinNoiseGenerator;
import celization.mapgeneration.perlinnoise.PerlinNoiseParameters;
import celization.CElization;
import celizationrequests.Coordinates;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author mjafar
 *
 */
public class GameMap implements Serializable {
    private static final long serialVersionUID = -1236552987782393122L;

    /**
     * map matrix
     */
    private LandBlock[][] map;
    private Coordinates gameMapSize;

    public Coordinates getGameMapSize() {
        return gameMapSize;
    }
    /**
     * psudo-random algorithm for map generation
     */
    private PerlinNoiseGenerator noiseGenerator;
    /**
     * minimum bounding box of visible areas
     */
    private HashMap<String, Coordinates> topLefts;
    private HashMap<String, Coordinates> bottomRights;
    /**
     * game instance to get necessary parameters within
     */
    private CElization gameInstance;

    /**
     * Makes game map based on perlin noise values
     *
     * @param gameInstance
     * @param perlinNoiseParameters
     * @param gameMapSize
     */
    public GameMap(CElization gameInstance, PerlinNoiseParameters perlinNoiseParameters, Coordinates gameMapSize) {
        /**
         * initialize game instance
         */
        this.gameInstance = gameInstance;
        this.gameMapSize = gameMapSize;
        topLefts = new HashMap<>();
        bottomRights = new HashMap<>();
        noiseGenerator = new PerlinNoiseGenerator(perlinNoiseParameters);
        makeMap();
    }

    public void removeUser(String username) {
        try {
            for (int i = 0; i < gameMapSize.col; i++) {
                for (int j = 0; j < gameMapSize.row; j++) {
                    map[i][j].removeUser(username);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        
        topLefts.remove(username);
        bottomRights.remove(username);
    }

    public void unlockCells(Coordinates topLeft, Coordinates size, String user) {
        if (!topLefts.containsKey(user) || !bottomRights.containsKey(user)) {
            topLefts.put(user, gameMapSize.clone());
            bottomRights.put(user, Coordinates.ZERO);
        }
        for (int i = 0; i < size.col; i++) {
            for (int j = 0; j < size.row; j++) {
                try {
                    map[topLeft.col + i][topLeft.row + j].unLock(user);
                    gameInstance.getPathFinder(user).unlockCell(topLeft.col + i, topLeft.row + j);
                    if (gameInstance.getGameMap().get(topLeft.col + i, topLeft.row + j).getType(user) != BlockType.PLAIN) {
                        gameInstance.getPathFinder(user).lockCell(topLeft.col + i, topLeft.row + j);
                    }
                    if (topLeft.col < topLefts.get(user).col) {
                        Coordinates newTL = topLefts.get(user).clone();
                        newTL.col = topLeft.col;
                        topLefts.put(user, newTL);
                    }
                    if (topLeft.col + i > bottomRights.get(user).col) {
                        Coordinates newBR = bottomRights.get(user).clone();
                        newBR.col = topLeft.col + i;
                        bottomRights.put(user, newBR);
                    }
                    if (topLeft.row < topLefts.get(user).row) {
                        Coordinates newTL = topLefts.get(user).clone();
                        newTL.row = topLeft.row;
                        topLefts.put(user, newTL);
                    }
                    if (topLeft.row + j > bottomRights.get(user).row) {
                        Coordinates newBR = bottomRights.get(user).clone();
                        newBR.row = topLeft.row + j;
                        bottomRights.put(user, newBR);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    /* it's just an array out of bound exception which java will
                     * take care of ;)
                     */
                }
            }
        }
    }

    public Coordinates getBoundingBoxTopLeft(String username) {
        return topLefts.get(username);
    }

    public Coordinates getBoundingBoxBottomRight(String username) {
        return bottomRights.get(username);
    }

    public boolean isAvailable(Coordinates topLeft, Coordinates size, String username) {
        try {
            for (int i = 0; i < size.col; i++) {
                for (int j = 0; j < size.row; j++) {
                    if (!gameInstance
                            .getPathFinder(username)
                            .isAvailable(new Coordinates(topLeft.col + i, topLeft.row + j))) {
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
        if (row < 0 || col < 0 || row >= gameMapSize.row || col >= gameMapSize.col) {
            return false;
        }

        BlockType blockType = map[col][row].getType(username);
        if (blockType == BlockType.UNKNOWN || blockType == BlockType.PLAIN) {
            return true;
        }

        return false;
    }

    private void makeMap() {
        if (map != null) {
            return;
        }
        /**
         * initialize map variables
         */
        map = new LandBlock[gameMapSize.col][gameMapSize.row];

        /**
         * Process map
         */
        double height;
        BlockType type = BlockType.PLAIN;
        for (int i = 0; i < gameMapSize.col; i++) {
            for (int j = 0; j < gameMapSize.row; j++) {
                /**
                 * Get perlin noise value
                 */
                height = noiseGenerator.get(i, j) / 20;
                /**
                 * Assign each value to a land type
                 */
                if (0.0 <= height && height < 0.03) {
                    type = BlockType.WATER;
                } else if (0.03 <= height && height < 0.05) {
                    type = BlockType.JUNGLE;
                } else if (0.05 <= height && height < 0.15) {
                    type = BlockType.PLAIN;
                } else if (0.15 <= height && height <= 1) {
                    type = BlockType.MOUNTAIN;
                }
                /**
                 * make land block
                 */
                map[i][j] = new LandBlock(type, height);
            }
        }
//        topLefts.put(username, gameMapSize.clone());
//        bottomRights.put(username, Coordinates.ZERO);
//        for (int i = 0; i < gameMapSize.col; i++) {
//            for (int j = 0; j < gameMapSize.row; j++) {
//                if (map[i][j].getType(username) == BlockType.UNKNOWN) {
//                    continue;
//                }
//                System.out.println("Gooz");
//                if (i < topLefts.get(username).col) {
//                    Coordinates newTL = topLefts.get(username).clone();
//                    newTL.col = i;
//                    topLefts.put(username, newTL);
//                }
//                if (i > bottomRights.get(username).col) {
//                    Coordinates newBR = bottomRights.get(username).clone();
//                    newBR.col = i;
//                    bottomRights.put(username, newBR);
//                }
//                if (j < topLefts.get(username).row) {
//                    Coordinates newTL = topLefts.get(username).clone();
//                    newTL.row = j;
//                    topLefts.put(username, newTL);
//                }
//                if (j > bottomRights.get(username).row) {
//                    Coordinates newBR = bottomRights.get(username).clone();
//                    newBR.row = j;
//                    bottomRights.put(username, newBR);
//                }
//            }
        }
}
