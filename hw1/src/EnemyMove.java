/**
 * Holds the properties of an enemy move.
 */
public class EnemyMove {

	/** Coordinates of the move. */
	private int i, j;
	/** Alignment of the move. */
	private boolean isVertical;

	public EnemyMove(int i, int j, boolean isVertical) {
		this.i = i;
		this.j = j;
		this.isVertical = isVertical;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public boolean isVertical() {
		return isVertical;
	}

	public void setVertical(boolean vertical) {
		isVertical = vertical;
	}
}
