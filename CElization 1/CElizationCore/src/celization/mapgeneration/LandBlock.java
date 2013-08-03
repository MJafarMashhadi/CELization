/**
 *
 */
package celization.mapgeneration;

import java.util.HashMap;
import celization.NaturalResources;
import java.io.Serializable;
import java.util.Set;

/**
 * @author mjafar
 *
 */
public class LandBlock implements Serializable {

    private BlockType blockType;
    private HashMap<String, Boolean> locked;
    private NaturalResources availableResources;
    private double height;

    public LandBlock(BlockType type, double height, Set<String> users) {
        locked = new HashMap<>();
        blockType = type;

        for (String username : users) {
            locked.put(username, Boolean.TRUE);
        }
        
        int resourcesAmount;
        resourcesAmount = (int) Math.abs(10 * Math.sin(height));

        availableResources = new NaturalResources(blockType, resourcesAmount);
        this.height = height;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    public BlockType getType(boolean doNotShowLocked, String user) {
        if (doNotShowLocked) {
            if (locked.get(user) == Boolean.FALSE) {
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
        locked.put(user, Boolean.FALSE);
    }

    public void setResources(NaturalResources naturalResources) {
        availableResources = naturalResources;
    }

    public NaturalResources getResources() {
        return availableResources;
    }

    void removeUser(String username) {
        locked.remove(username);
    }
}
