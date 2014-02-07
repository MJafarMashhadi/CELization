package celization.mapgeneration.astar;

import java.io.Serializable;
import java.util.LinkedList;

public class AStarCore implements Serializable {
    private static final long serialVersionUID = -1775332902510430911L;

    private AStar mapContainer;
    private Node start, end;
    private LinkedList<Node> route = new LinkedList<Node>();
    private LinkedList<Node> closedNodes = new LinkedList<Node>();
    private LinkedList<Node> openNodes = new LinkedList<Node>();

    public AStarCore(AStar map) {
        this.mapContainer = map;
    }

    public AStarCore(AStar map, int fromRow, int fromCol, int toRow, int toCol) {
        this.mapContainer = map;
        end = new Node(toRow, toCol, null, null);
        start = new Node(fromRow, fromCol, null, end);
        findPath();
    }

    private void findPath() {
        openNodes.clear();
        closedNodes.clear();
        route.clear();
        mapContainer.mapChanged = false;

        // add start node to the openSet
        openNodes.add(start);

        Node currentNode = start;
        while (openNodes.size() > 0) {
            // get current node
            currentNode = getSmallestFvaluedNode();
            // add adjacent nodes to open list
            addToOpenList(currentNode.getRow() - 1, currentNode.getCol(), currentNode);
            addToOpenList(currentNode.getRow() + 1, currentNode.getCol(), currentNode);
            addToOpenList(currentNode.getRow(), currentNode.getCol() - 1, currentNode);
            addToOpenList(currentNode.getRow(), currentNode.getCol() + 1, currentNode);
            // move current node from open nodes to closed nodes
            closedNodes.add(currentNode);
            openNodes.remove(currentNode);
            //if (findInNodes(closedNodes, end.getRow(), end.getCol()) != -1) {
            if (currentNode.equals(end)) {
                break;
            }
        }
        // found path
        // in this map no route to destination will not happen
        while (!start.equals(currentNode) /*&& currentNode != null */) {
            route.addFirst(currentNode);
            currentNode = currentNode.getParent();
        }
    }

    private Node getSmallestFvaluedNode() {
        Node smallest = openNodes.getFirst();
        for (Node thisNode : openNodes) {
            if (thisNode.getFScore() < smallest.getFScore()) {
                smallest = thisNode;
            }
        }
        return smallest;
    }

    private int findInNodes(LinkedList<Node> list, int row, int col) {
        Node thisNode;
        for (int i = 0; i < list.size(); i++) {
            thisNode = list.get(i);
            if (thisNode.getRow() == row && thisNode.getCol() == col) {
                return i;
            }
        }
        return -1;
    }

    private void addToOpenList(int row, int col, Node currentNode) {
        try {
            int openListID;
            if (mapContainer.map[col][row] && findInNodes(closedNodes, row, col) == -1) {
                openListID = findInNodes(openNodes, row, col);
                if (openListID == -1) {
                    openNodes.add(new Node(row, col, currentNode, end));
                } else {
                    openNodes.get(openListID).changeParent(currentNode);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public int getPathLength() {
        return route.size();
    }

    public Node getNextMove() {
        if (mapContainer.mapChanged) {
            if (route.size() > 0) {
                start = route.getFirst();
            }
            findPath();
        }
        if (route.size() == 0) {
            return null;
        }
        Node returnNode = route.getFirst();
        route.removeFirst();
        return returnNode;
    }

    public void clear() {
        mapContainer = null;
        start = null;
        end = null;
        route.clear();
        closedNodes.clear();
        openNodes.clear();
    }
}
