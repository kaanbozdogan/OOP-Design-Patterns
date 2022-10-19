import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class GameBoard implements ActionListener, Runnable {

	private class GamePanel extends JPanel {

		public GamePanel() {
			super();
			setFocusable(true);
			setBackground(Color.lightGray);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;

			// print user characters
			for (int i = 0; i < charC; i++)
				drawEntity(g2d, c[i]);
			// print enemy characters
			for (int i = 0; i < charC; i++)
				drawEntity(g2d, e[i]);
		}

		private void drawEntity(Graphics2D g2d, CharacterBox box) {
			if (box.getColor() == null)
				g2d.setColor(Color.black);
			else
				g2d.setColor(box.getColor());
			g2d.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
		}

	}

/*---Fields---*/
	private JFrame frame;
	private GamePanel panel;
	private CharacterBox[] c;
	private CharacterBox[] e;
	private ColorBox[][] colors;

	private final int rowC = 6;
	private final int colorC = 3;
	private final int charC = 3;
	private final int colC = colorC * charC;

	Timer t;

	public GameBoard() {
		initFrame();
		initBoard();

		t = new Timer(5, this);
		t.start();
	}

/*---private---*/
	private AbstractCharacterFactory getRandomFactory() {
		int rand = (int) (Math.random() * 3);

		if (rand == 0)
			return new AtlantisCharacterFactory();
		else if (rand == 1)
			return new ValhallaCharacterFactory();
		else
			return new UnderwildCharacterFactory();
	}

	static public Color getRandomColor () {
		int rand = (int) (Math.random() * 3);

		if (rand == 0)
			return Color.red;
		else if (rand == 1)
			return Color.blue;
		else
			return Color.green;
	}

	private void initFrame() {
		frame = new JFrame("2d game");
		frame.setSize(600,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

	private void initBoard() {
		// init main panel
		panel = new GamePanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		// filler
		Component filler1 = Box.createRigidArea(new Dimension(400,30));
		panel.add(filler1);

		initMenu();

		// filler
		Component filler2 = Box.createRigidArea(new Dimension(400, 350));
		panel.add(filler2);

		initColors();
		initCharacters();

		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	private void initCharacters() {
		int x, y, width;
		// init user character
		c = new CharacterBox[charC];
		x = 60;
		y = 150;
		width = 80;
		for (int i = 0; i < charC; i++) {
			c[i] = new CharacterBox(getRandomFactory().getCharacter(getRandomColor()),x,y,width,width);
			x += width + 40;
		}

		// init enemy characters
		e = new CharacterBox[charC];
		x = 60;
		y = 250;
		width = 80;
		for (int i = 0; i < charC; i++) {
			e[i] = new CharacterBox(getRandomFactory().getCharacter(getRandomColor()),x,y,width,width);
			x += width + 40;
		}
	}

	private void initColors() {
		// init color panel
		JPanel colorP = new JPanel();
		colorP.setMaximumSize(new Dimension(colC * 40,rowC * 40));
		colorP.setLayout(new GridLayout(rowC,colC));
		colorP.setBackground(Color.lightGray);
		// init colors
		colors = new ColorBox[rowC][colC];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				colors[i][j] = new ColorBox(getRandomColor());
				colorP.add(colors[i][j]);
			}
		}
		panel.add(colorP);
	}

	private void initMenu() {
		// init menu
		Box menu = new Box(0);
		menu.setAlignmentX(Component.CENTER_ALIGNMENT);
		menu = Box.createHorizontalBox();
		JButton[] menuBs = new JButton[2];
		menuBs[0] = new JButton("Pause");
		menuBs[1] = new JButton("Exit");

		for (int i = 0; i < 2; i++) {
			menuBs[i].setMaximumSize(new Dimension(60,30));
			menuBs[i].setBorder(BorderFactory.createLineBorder(Color.gray, 2));
			menuBs[i].setBackground(Color.white);
			menu.add(menuBs[i]);
		}
		panel.add(menu);

		menuBs[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add mutex lock to pause the game
				if (menuBs[0].getText().equals("Pause"))
					menuBs[0].setText("Play");
				else
					menuBs[0].setText("Pause");
			}
		});

		menuBs[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exit Clicked");
				System.exit(0);
			}
		});
	}

	private ColorMatch clearMatchingColors() {
		int match, i, j;
		ColorMatch returnVal;

		// vertical check
		for (j = 0; j < colC; j++) {
			match = 1;
			// check current column
			for (i = 0; i < rowC - 1; i++) {
				// if there is no possible 3 or more color matches no need to check the rest of the column
				/*if (i + match + 1 >= rowC)
					continue;
				 */

				// check if adjacent colors match
				if (colors[i][j].equals(colors[i + 1][j]))
					match++;

					// check if matches are enough to remove them
				else if (match >= 3)
					break;

					// not enough match to remove them
				else
					match = 1;
			}

			if (match >= 3) {
				System.out.println("MAtch!!!!!!!");
				returnVal = new ColorMatch(j, match, true, colors[i - 1][j].getColor());
				// remove matching colors
				for (int k = i - match + 1; k <= i; k++)
					colors[k][j].setColor(null);
				return returnVal;
			}
		}

		// horizontal check
		for (i = 0; i < rowC; i++) {
			match = 1;
			// check current row
			for (j = 0; j < colC - 1; j++) {

				// if there is no possible 3 or more color matches no need to check the rest of the column
				/*if (j + match + 1 >= colC)
					continue;
				*/

				// check if adjacent colors match
				if (colors[i][j].equals(colors[i][j + 1]))
					match++;

					// check if matches are enough to remove them
				else if (match >= 3)
					break;

					// not enough match to remove them
				else
					match = 1;
			}

			if (match >= 3) {
				System.out.println("MAtch!!!!!!!");
				returnVal = new ColorMatch(j - match + 1, match, false, colors[i][j - 1].getColor());
				// remove matching colors
				for (int k = j - match + 1; k <= j; k++)
					colors[i][k].setColor(null);
				return returnVal;
			}
		}



		return null;
	}

	private boolean fillEmptyColors() {
		boolean retVal = false;
		// search color slots which are empty
		for (int j = 0; j< colC; j++) { //search column
			int emptyC = 0; //consequent empty slots
			int newC = 0; //newly added color slots which are not empty. no need to check them
			for (int i = 0; i < rowC; i++) { //search row
				// find consequent empty slots in the current column
				while (i < rowC && colors[i][j].getColor() == null) {
					emptyC ++;
					i++;
				}
				// empty slot found
				if (emptyC > 0) {
					retVal = true;
					int tempI = i - emptyC - 1; // save where we left checking the null slots
					// shift colors and fill empty color slots
					for (; i < rowC; i++)
						colors[i-emptyC][j].setColor(colors[i][j].getColor());
					// fill empty color slots created by the shift
					for (i = i - emptyC; i < rowC; i++)
						colors[i][j].setColor(getRandomColor());
					// set vars to check the rest of the column
					i = tempI;
					newC += emptyC;
					emptyC = 0;
				}
			}
		}
		return retVal;
	}

	private void matchColorsAndFillBoard() {
		ColorMatch match = null;
		do {
			sleep(1000);
			do {
				if (match != null)
					match.print();
				match = clearMatchingColors();

				sleep(100);

			} while (match != null);
			sleep(1000);
		} while (fillEmptyColors());
	}

	private void switchClickedColors() {
		int[][] ij = new int[2][2];
		int n = 0;

		for (int i = 0; i < rowC; i++) {
			for (int j = 0; j < colC; j++) {
				if (colors[i][j].isClicked() && n < 2) {
					ij[n][0] = i;
					ij[n][1] = j;
					n++;
				}
			}
		}

		if (n == 2) {
			Color temp = colors[ij[0][0]][ij[0][1]].getColor();
			colors[ij[0][0]][ij[0][1]].setColor(colors[ij[1][0]][ij[1][1]].getColor());
			colors[ij[1][0]][ij[1][1]].setColor(temp);

			colors[ij[0][0]][ij[0][1]].resetClicked();
			colors[ij[1][0]][ij[1][1]].resetClicked();
		}
	}

	private void sleep(int n) {
		try {
			TimeUnit.MILLISECONDS.sleep(n);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void boardLoop() {
		ColorMatch match;
		do {
			do {
				match = clearMatchingColors();
			} while (match != null);
		} while (fillEmptyColors());

		while (true) {
			System.out.print("");
			if (ColorBox.getClickCount() >= 2) {
				sleep(100);
				switchClickedColors();
				sleep(1000);
				matchColorsAndFillBoard();
			}
		}
	}

/*---public---*/
	public void print() {
		Color curr;

		for (int i = 0; i < rowC; i++) {
			for (int j = 0; j < colC; j++) {
				curr = colors[i][j].getColor();
				if (curr != null) {
					if (curr.equals(Color.red))
						System.out.print("red   ");
					else if (curr.equals(Color.blue))
						System.out.print("blue  ");
					else
						System.out.print("green ");
				}
				else
					System.out.print("____  ");
				System.out.print("| ");
			}
			System.out.println();
		}
		System.out.println();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		panel.repaint();
	}

	@Override
	public void run() {
		Thread boardLoop = new Thread(this::boardLoop);
		boardLoop.start();
	}

}
