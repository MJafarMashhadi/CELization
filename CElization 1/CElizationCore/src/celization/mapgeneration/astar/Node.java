package celization.mapgeneration.astar;

import celizationrequests.Coordinates;
import java.io.Serializable;
import java.util.Objects;

public final class Node implements Serializable {
    private static final long serialVersionUID = 6416372973975732205L;

    private int row;
    private int col;
    private int hScore;
    private int gScore;
    private Node parentNode;
    private Node endNode;

    public int getFScore() {
        return gScore + hScore;
    }

    public int getGScore() {
        return gScore;
    }

    public Node(int row, int col, Node parent, Node endNode) {
        this.row = row;
        this.col = col;
        this.endNode = endNode;
        changeParent(parent);
    }

    public void changeParent(Node newParent) {
        parentNode = newParent;
        if (parentNode == null) {
            gScore = 0;
        } else {
            gScore = 1 + parentNode.getGScore();
        }
        if (endNode == null) {
            hScore = 0;
        } else {
            hScore = Math.abs(endNode.getRow() - row) + Math.abs(endNode.getCol() - col);
        }
    }

    public Node getParent() {
        return parentNode;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object e) {
        if (e == null) {
            return false;
        }
        if (e.getClass() != this.getClass()) {
            return false;
        }
        if (((Node) e).getRow() != this.getRow() || ((Node) e).getCol() != this.getCol()) {
            return false;
        }

        return true;
    }

    public Coordinates toCoordinates() {
        return new Coordinates(getCol(), getRow());
    }
}
