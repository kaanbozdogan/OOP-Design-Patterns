import java.awt.*;

/**
 * Holds the properties of the color boxes matched.
 */
public class ColorMatch {

	/** Count of the color boxes matched. */
	private int count;
	/** Column of the match. */
	private int x;
	/** Alignment of the match. */
	private boolean isVert;
	/** Color of the boxes matched. */
	private Color color;

	public ColorMatch(int x, int count, boolean isVert, Color color) {
		this.count = count;
		this.x = x;
		this.isVert = isVert;
		this.color = color;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public boolean isVert() {
		return isVert;
	}

	public void setVert(boolean vert) {
		isVert = vert;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		String colStr;
		if (color.equals(Color.red))
			colStr = "red";
		else if (color.equals(Color.blue))
			colStr = "blue";
		else colStr = "green";

		return count + " " + colStr + " colors are matched.";
	}
}
