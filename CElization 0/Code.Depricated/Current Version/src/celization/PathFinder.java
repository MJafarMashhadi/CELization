/**
 *
 */
package celization;

import java.util.ArrayList;
import java.util.LinkedList;

import celization.buildings.Building;
import celization.buildings.MainBuilding;
import celization.buildings.Storage;

/**
 * @author mjafar
 *
 */
@Deprecated
public class PathFinder {

    /**
     *
     */
    @Deprecated
    public static boolean[][] availableBlocks;

    @Deprecated
    public static String findPath(Coordinates from, Coordinates to) {

        int rowCount = GameParameters.gameMapSize.row;
        int colCount = GameParameters.gameMapSize.col;

        String[][] directions = new String[rowCount][colCount];
        Coordinates[][] parents = new Coordinates[rowCount][colCount];
        boolean[][] visited = new boolean[rowCount][colCount];
        boolean found = false;

        LinkedList<Coordinates> queue = new LinkedList<Coordinates>();
        queue.add(from);

        int cRow, cCol;

        String[] dirList = {"up", "right", "down", "left"};
        int[] deltaRow = {-1, 0, 1, 0};
        int[] deltaCol = {0, 1, 0, -1};

        while (!found && queue.size() > 0) {
            Coordinates point = queue.poll();
            if (point.equals(to)) {
                found = true;
            }

            for (int i = 0; i < 4; i++) {
                cRow = point.row + deltaRow[i];
                cCol = point.col + deltaCol[i];
                if (cRow < 0 || cRow >= rowCount || cCol < 0
                        || cCol >= colCount) {
                    continue;
                }
                if (visited[cRow][cCol] || !availableBlocks[cCol][cRow]) {
                    continue;
                }
                queue.add(new Coordinates(cCol, cRow));
                directions[cRow][cCol] = dirList[i];
                directions[cRow][cCol] = dirList[i];
                parents[cRow][cCol] = point;
                visited[cRow][cCol] = true;
                if (to.equals(new Coordinates(cCol, cRow))) {
                    found = true;
                }
            }
        }

        if (!found) {
            return "none";
        }

        Coordinates point = to.clone();
        Coordinates parent = parents[to.row][to.col];
        while (parent != null && !(from.equals(parent))) {
            point = parent;
            parent = parents[point.row][point.col];
        }

        String dir = directions[point.row][point.col];
        if (dir == null) {
            return "none";
        } else {
            return dir;
        }

    }

    @Deprecated
    private static int getDistance(Coordinates f, Coordinates t) {
        int rowCount = GameParameters.gameMapSize.row, colCount = GameParameters.gameMapSize.col;

        Coordinates[][] parents = new Coordinates[rowCount][colCount];
        boolean[][] visited = new boolean[rowCount][colCount];
        boolean found = false;

        LinkedList<Coordinates> queue = new LinkedList<Coordinates>();
        queue.add(f);

        int cRow, cCol;

        int[] deltaRow = {-1, 0, 1, 0};
        int[] deltaCol = {0, 1, 0, -1};

        while (!found && queue.size() > 0) {
            Coordinates point = queue.poll();
            if (point.equals(t)) {
                found = true;
            }

            for (int i = 0; i < 4; i++) {
                cRow = point.row + deltaRow[i];
                cCol = point.col + deltaCol[i];
                if (cRow < 0 || cRow >= rowCount || cCol < 0
                        || cCol >= colCount) {
                    continue;
                }
                if (visited[cRow][cCol] || !availableBlocks[cCol][cRow]) {
                    continue;
                }
                queue.add(new Coordinates(cCol, cRow));
                parents[cRow][cCol] = point;
                visited[cRow][cCol] = true;
                if (t.equals(new Coordinates(cCol, cRow))) {
                    found = true;
                }
            }
        }

        if (!found) {
            return -1;
        }

        Coordinates point = t.clone();
        Coordinates parent = parents[t.row][t.col];

        int distance = 1;
        while (parent != null && !(f.equals(parent))) {
            point = parent;
            parent = parents[point.row][point.col];
            distance++;
        }

        return distance;
    }

    @Deprecated
    public static Coordinates nearestStorage(Coordinates position,
            Building[] buildings) {
        /**
         * find possible destinations
         */
        ArrayList<Building> canBeStoredThere = new ArrayList<Building>(1);
        for (int i = 0; i < buildings.length; i++) {
            Building b = buildings[i];
            if (b instanceof MainBuilding || b instanceof Storage) {
                canBeStoredThere.add(b);
            }
        }
        /**
         * find distances
         */
        int[] distances = new int[canBeStoredThere.size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = getDistance(position,
                    canBeStoredThere.get(i).getLocation());
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
