import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The color box showed in the game UI.
 */
public class ColorBox extends JButton implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ReentrantLock lock = Game.getLock();

		if (!lock.isLocked()) {
			if (!clicked) {
				clicked = true;
				clickCount++;
				setBorder(BorderFactory.createLineBorder(Color.black, 3));

				if (clickCount >= 2) {
					lock.lock();
					Game.getCond().signal();
					lock.unlock();
				}
			}
			else {
				resetClicked();
			}
		}
	}

	/** Color of the box. */
	private Color color;
	/** Is the color box clicked. */
	private boolean clicked;
	/** Total color boxes clicked. */
	static private int clickCount = 0;

	public ColorBox(Color color) {
		super();
		this.color = color;
		clicked = false;
		setBackground(color);
		addActionListener(this);
		setBorder(BorderFactory.createLineBorder(Color.gray, 2));
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		setBackground(color);
	}

	public static int getClickCount() {
		return clickCount;
	}

	public boolean isClicked() {
		return clicked;
	}

	/** Makes this color box like it is not clicked. */
	public void resetClicked() {
		clickCount --;
		clicked = false;
		setBorder(BorderFactory.createLineBorder(Color.gray, 2));
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ColorBox))
			return false;

		ColorBox other = (ColorBox) obj;

		if (this.color == null || other.color == null)
			return false;
		else
			return this.color.equals(other.color);
	}
}
