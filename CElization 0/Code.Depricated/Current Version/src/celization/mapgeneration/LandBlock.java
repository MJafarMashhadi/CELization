/**
 *
 */
package celization.mapgeneration;

import java.util.HashMap;
import celization.Coordinates;
import celization.NaturalResources;
import celization.UserInfo;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public class LandBlock implements Serializable {

    private Coordinates coordinates;
    private BlockType blockType;
    private HashMap<String, Boolean> locked;
    private NaturalResources availableResources;
    private double height;

    public LandBlock(int col, int row, BlockType type, double height, HashMap<String, UserInfo> users) {
        locked = new HashMap<String, Boolean>();
        coordinates = new Coordinates(col, row);
        blockType = type;

        for (String username : users.keySet()) {
            locked.put(username, true);
        }

        availableResources = new NaturalResources(blockType);
        this.height = height;
    }

    /**
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    public BlockType getType(boolean doNotShowLocked, String user) {
        if (doNotShowLocked) {
            if (!locked.containsKey(user) || !locked.get(user)) {
                return blockType;
            } else {
                return BlockType.UNKNOWN;
            }
        } else {
            return blockType;
        }
    }

    public BlockType getType(String user) {
        return getType(true, user);
    }

    public void unLock(String user) {
        locked.put(user, false);
    }

    public void setResources(NaturalResources naturalResources) {
        availableResources = naturalResources;
    }

    public NaturalResources getResources() {
        return availableResources;
    }
}
