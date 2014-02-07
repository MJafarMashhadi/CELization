package celization.mapgeneration;

import celization.NaturalResources;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author mjafar
 *
 */
public class LandBlock implements Serializable {

    private static final long serialVersionUID = 7743183084564194210L;
    private BlockType blockType;
    private ArrayList<String> unlockedFor = new ArrayList<>();
    private NaturalResources availableResources;
    private double height;

    public LandBlock(BlockType type, double height) {
        blockType = type;
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
            if (unlockedFor.contains(user)) {
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

    public BlockType getType() {
        return this.getType(false, null);
    }

    public void unLock(String user) {
        unlockedFor.add(user);
    }

    public void setResources(NaturalResources naturalResources) {
        availableResources = naturalResources;
    }

    public NaturalResources getResources() {
        return availableResources;
    }

    void removeUser(String username) {
        if (unlockedFor.contains(username)) {
            unlockedFor.remove(username);
        }
    }
}
