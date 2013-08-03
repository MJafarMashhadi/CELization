/**
 *
 */
package celization.civilians.workeractions;

import celizationrequests.Coordinates;

/**
 * @author mjafar
 *
 */
public final class Move extends CivilianActions {

    private int pathFinderIndex;
    private Coordinates destination;

    public Move(Coordinates destination) {
        this.destination = destination;
        pathFinderIndex = -1;
    }

    public Coordinates getDestination() {
        return destination;
    }

    /**
     *
     * @param index
     */
    public Move(int index) {
        pathFinderIndex = index;
    }

    /**
     *
     * @return
     */
    public int getPathFinderIndex() {
        return pathFinderIndex;
    }
}
