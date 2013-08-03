package celization.mapgeneration.astar;

import java.util.ArrayList;
import java.util.LinkedList;

import celizationrequests.Coordinates;
import celization.GameParameters;
import celization.buildings.Building;
import celization.buildings.HeadQuarters;
import celization.buildings.Storage;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public class AStar implements Serializable {

    protected boolean map[][];
    protected boolean mapChanged = false;
    private LinkedList<AStarCore> pathFinders;
    int finishedPaths = 0;

    public void setMap(boolean __map[][]) {
        map = __map;
        mapChanged = true;
        pathFinders = new LinkedList<>();
    }

    public AStar(Coordinates gameMapSize) {
        map = new boolean[gameMapSize.col][gameMapSize.row];
        for (int i = 0; i < gameMapSize.col; i++) {
            for (int j = 0; j < gameMapSize.row; j++) {
                map[i][j] = true;
            }
        }
    }

    public AStarCore getPathFinder(int index) {
        return pathFinders.get(index);
    }

    public int getPathLength(int index) {
        return pathFinders.get(index).getPathLength();
    }

    public Node getNextMove(int index) {
        Node nextNode = pathFinders.get(index).getNextMove();

        if (nextNode == null) {
            remove(index);
            if (finishedPaths == pathFinders.size()) {
                finishedPaths = 0;
                pathFinders.clear();
            }
        }

        return nextNode;
    }

    public int newPathFinder(Coordinates start, Coordinates end) {
        if (pathFinders == null) {
            pathFinders = new LinkedList<>();
        }
        pathFinders.addLast(new AStarCore(this, start.row, start.col, end.row, end.col));
        return pathFinders.size() - 1;
    }

    public boolean isAvailable(Coordinates location) {
        return map[location.col][location.row];
    }

    public void lockCell(int col, int row) {
        mapChanged = true;
        map[col][row] = false;
    }

    public void unlockCell(int col, int row) {
        mapChanged = true;
        map[col][row] = true;
    }

    /**
     * remove a path finder instance
     *
     * @param pathfinderIndex
     */
    public void remove(int pathfinderIndex) {
        pathFinders.get(pathfinderIndex).clear();
        finishedPaths++;
    }

    public Coordinates getNearestStorage(Coordinates position, Collection<Building> buildings) {
        /**
         * find possible destinations
         */
        ArrayList<Building> canBeStoredThere = new ArrayList<Building>(1);
        Iterator<Building> itr = buildings.iterator();
        while (itr.hasNext()) {
            Building b = itr.next();
            if (b instanceof HeadQuarters || b instanceof Storage) {
                canBeStoredThere.add(b);
            }
        }

        /**
         * find distances
         */
        int[] distances = new int[canBeStoredThere.size()];
        for (int i = 0; i < distances.length; i++) {
            int index = this.newPathFinder(position, canBeStoredThere.get(i).getLocation());
            distances[i] = pathFinders.get(index).getPathLength();
            remove(index);
        }

        /**
         * find minimum distance
         */
        int minimumDistance, minimumDistanceIndex;
        minimumDistanceIndex = 0;
        minimumDistance = distances[0];
        for (int i = 0; i < distances.length; i++) {
            if (minimumDistance < 0) {
                minimumDistance = distances[i];
                minimumDistanceIndex = i;
            }
            if (distances[i] < minimumDistance && distances[i] > 0) {
                minimumDistanceIndex = i;
                minimumDistance = distances[i];
            }
        }

        /**
         * get position of minimum distance
         */
        return canBeStoredThere.get(minimumDistanceIndex).getLocation();
    }
}
