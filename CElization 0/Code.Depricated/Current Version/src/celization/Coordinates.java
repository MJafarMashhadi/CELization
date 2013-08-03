/**
 *
 */
package celization;

import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public class Coordinates implements Serializable {
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
