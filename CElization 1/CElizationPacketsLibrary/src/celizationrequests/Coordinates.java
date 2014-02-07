/**
 *
 */
package celizationrequests;

import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public class Coordinates implements Serializable {

    public static Coordinates ZERO = new Coordinates(0, 0);
    private static final long serialVersionUID = 2896222035852738197L;
    public int col;
    public int row;

    public Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public String toString() {
        return String.format("(%3d, %3d)", col, row);
    }

    @Override
    public boolean equals(Object c) {
        if (c == null) {
            return false;
        }
        if (c.getClass() != Coordinates.class) {
            return false;
        }

        return ((((Coordinates) c).row == row) && (((Coordinates) c).col == col));
    }

    public Coordinates clone() {
        return new Coordinates(col, row);
    }
}
