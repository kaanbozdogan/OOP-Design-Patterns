import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class provides all the data and mechanics needed to play
 * "Campuses and Puzzles" game.
 */
public class Game implements ActionListener, Runnable {

	/** Frame of the game. */
	private JFrame frame;
	/** Main panel of the game. */
	private JPanel panel;
	/** User characters. */
	private CharacterBox[] c;
	/** Enemy characters. */
	private CharacterBox[] e;
	/** Colors table. */
	private ColorBox[][] colors;
	/** Log bar. */
	private LogBar log;
	private boolean userTurn;
	private int lastRandColor;

	/** Row size of the color table. */
	public static final int ROW_S = 6;
	/** Color size for every character. */
	public static final int COLOR_S = 3;
	/** Character count. */
	public static final int CHAR_S = 3;
	/** Column size of the color table. */
	public static final int COL_S = COLOR_S * CHAR_S;
	/** Standard damage one matching tile gives without any modifiers. */
	public static final int STD_DMG = 5;

	/** Swing timer. */
	Timer timer;
	/** Lock for color table. */
	static private ReentrantLock lock = new ReentrantLock();
	/** Condition variable to reactivate the color matching loop when 2 buttons are clicked.
	 * Provides synchronization by preventing busy waiting. */
	static private Condition clickedTwoButtons = lock.newCondition();
	private Thread boardLoop;

	/** Creates a standard game. */
	public Game() {
		userTurn = true;
		lastRandColor = -1;

		initFrame();
		initBoard();

		timer = new Timer(5, this);
		timer.start();

		boardLoop = new Thread(this::boardLoop);
	}

/*---setter/getter---*/

	/**
	 * Returns the lock for color table.
	 * @return The lock for color table.
	 */
	static public ReentrantLock getLock() {
		return lock;
	}

	/**
	 * Returns the condition variable.
	 * @return The condition variable
	 */
	static public Condition getCond() {
		return clickedTwoButtons;
	}

/*---helper---*/

	/**
	 * Returns a random color from 3 colors which used in the game (red, blue, green)
	 * @return A random color.
	 */
	private Color getRandomColor () {
		int rand;
		do {
			rand = (int) (Math.random() * 3);
		} while(rand == lastRandColor);

		lastRandColor = rand;

		if (rand == 0)
			return Color.red;
		else if (rand == 1)
			return Color.blue;
		else
			return Color.green;
	}

	/**
	 * Initializes the java swing frame of the game
	 */
	private void initFrame() {
		frame = new JFrame("Campuses and Puzzles");
		frame.setSize(600,925);

		WindowListener close = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				try {
					boardLoop.join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		};
		frame.addWindowListener(close);

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Initialize the main panel of the game.
	 */
	private void initBoard() {
		// init main panel
		panel = new JPanel();
		panel.setFocusable(true);
		panel.setBackground(Color.lightGray);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		// filler
		Component filler1 = Box.createRigidArea(new Dimension(400,10));
		panel.add(filler1);

		initMenu();

		// filler
		Component filler2 = Box.createRigidArea(new Dimension(400, 30));
		panel.add(filler2);

		initCharacters();

		// filler
		Component filler3 = Box.createRigidArea(new Dimension(400, 50));
		panel.add(filler3);

		initColors();

		// filler
		Component filler4 = Box.createRigidArea(new Dimension(400, 50));
		panel.add(filler4);

		initLog();

		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	/**
	 * Initializes the user and enemy characters of the game.
	 */
	private void initCharacters() {
		JPanel charP = new JPanel();
		charP.setMaximumSize(new Dimension(360, 200));
		charP.setLayout(new GridLayout(2, CHAR_S));
		charP.setBackground(Color.gray);
		charP.setBorder(BorderFactory.createLineBorder(Color.gray, 10));

		// init characters witch each color
		c = new CharacterBox[CHAR_S];
		e = new CharacterBox[CHAR_S];
		c[0] = new CharacterBox(AbstractCharacterFactory.getRandomFactory().getCharacter(Color.red));
		c[1] = new CharacterBox(AbstractCharacterFactory.getRandomFactory().getCharacter(Color.blue));
		c[2] = new CharacterBox(AbstractCharacterFactory.getRandomFactory().getCharacter(Color.green));
		e[0] = new CharacterBox(AbstractCharacterFactory.getRandomFactory().getCharacter(Color.red));
		e[1] = new CharacterBox(AbstractCharacterFactory.getRandomFactory().getCharacter(Color.blue));
		e[2] = new CharacterBox(AbstractCharacterFactory.getRandomFactory().getCharacter(Color.green));

		for (int i = 0; i < CHAR_S; i++)
			charP.add(c[i].getLabel());
		for (int i = 0; i < CHAR_S; i++)
			charP.add(e[i].getLabel());

		panel.add(charP);
	}

	/**
	 * Initializes the color table of the game.
	 */
	private void initColors() {
		// init color panel
		JPanel colorP = new JPanel();
		colorP.setMaximumSize(new Dimension(COL_S * 40, ROW_S * 40));
		colorP.setLayout(new GridLayout(ROW_S, COL_S));
		colorP.setBackground(Color.lightGray);
		// init colors
		colors = new ColorBox[ROW_S][COL_S];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				colors[i][j] = new ColorBox(getRandomColor());
				colorP.add(colors[i][j]);
			}
		}
		panel.add(colorP);
		resetColorBoard();
	}

	/**
	 * Initializes the menu buttons of the game.
	 */
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

	/**
	 * Initialize the log bar of the game.
	 */
	private void initLog() {
		log = new LogBar();
		panel.add(log.getPanel());
		log.add("Characters are initialized.");
		log.add("Color table is initialized.");
	}

	/**
	 * Checks if the board has any matches.
	 * @return True if board has any matches.
	 */
	private boolean matchFound() {
		int match, i, j;
		ColorMatch returnVal;

		// vertical check
		for (j = 0; j < COL_S; j++) {
			match = 1;
			// check current column
			for (i = 0; i < ROW_S - 1; i++) {
				// check if adjacent colors match
				if (colors[i][j].equals(colors[i + 1][j]))
					match++;
					// check if matches are enough to remove them
				else if (match >= 3)
					return true;
					// not enough match to remove them
				else
					match = 1;
			}
		}

		// horizontal check
		for (i = 0; i < ROW_S; i++) {
			match = 1;
			// check current row
			for (j = 0; j < COL_S - 1; j++) {
				// check if adjacent colors match
				if (colors[i][j].equals(colors[i][j + 1]))
					match++;
					// check if matches are enough to remove them
				else if (match >= 3)
					return true;
					// not enough match to remove them
				else
					match = 1;
			}
		}
		return false;
	}

	/**
	 * Clears the matching colors in the table after a move.
	 * @return True if any color match found and cleared.
	 */
	private ColorMatch clearMatchingColors() {
		int match, i, j;
		ColorMatch returnVal;

		// vertical check
		for (j = 0; j < COL_S; j++) {
			match = 1;
			// check current column
			for (i = 0; i < ROW_S - 1; i++) {
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
				returnVal = new ColorMatch(j, match, true, colors[i - 1][j].getColor());
				// remove matching colors
				for (int k = i - match + 1; k <= i; k++)
					colors[k][j].setColor(null);
				return returnVal;
			}
		}

		// horizontal check
		for (i = 0; i < ROW_S; i++) {
			match = 1;
			// check current row
			for (j = 0; j < COL_S - 1; j++) {
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
				returnVal = new ColorMatch(j - match + 1, match, false, colors[i][j - 1].getColor());
				// remove matching colors
				for (int k = j - match + 1; k <= j; k++)
					colors[i][k].setColor(null);
				return returnVal;
			}
		}
		return null;
	}

	/**
	 * Shifts the colors to empty fields and make new colors
	 * appear from the bottom of the board
	 * @return True if any empty fields found and filled in the board.
	 */
	private boolean fillEmptyColors() {
		boolean retVal = false;
		// search color slots which are empty
		for (int j = 0; j< COL_S; j++) { //search column
			int emptyC = 0; //consequent empty slots
			int newC = 0; //newly added color slots which are not empty. no need to check them
			for (int i = 0; i < ROW_S; i++) { //search row
				// find consequent empty slots in the current column
				while (i < ROW_S && colors[i][j].getColor() == null) {
					emptyC ++;
					i++;
				}
				// empty slot found
				if (emptyC > 0) {
					retVal = true;
					int tempI = i - emptyC - 1; // save where we left checking the null slots
					// shift colors and fill empty color slots
					for (; i < ROW_S; i++)
						colors[i-emptyC][j].setColor(colors[i][j].getColor());
					// fill empty color slots created by the shift
					for (i = i - emptyC; i < ROW_S; i++)
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

	/**
	 * Clears matches and fills the board.
	 */
	private void resetColorBoard() {
		//creating a board without any match
		ColorMatch match;
		do {
			do {
				match = clearMatchingColors();
			} while (match != null);
		} while (fillEmptyColors());
	}

	/**
	 * Clears matching colors and fills the empty spaces in the board in a loop
	 * enabling cascaded matches.
	 * @return True if any change made to the board.
	 */
	private boolean matchColorsAndFillBoard() {
		boolean isChanged = false;
		ColorMatch match = null;
		int count, charI, tileS, damage, remHealth;
		CharacterBox[] enemy;
		String enemyName;

		do {
			count = 0;
			sleep(600);
			do {
				match = clearMatchingColors();

				//damage calculation
				if (match != null) {
					log.add(match.toString());
					count ++;
					sleep(50);

					charI = match.getX() / CHAR_S; //character index who takes damage
					//the owner of the characters taking damage
					if (userTurn) {
						enemy = e;
						enemyName = "e";
					}
					else {
						enemy = c;
						enemyName = "c";
					}

					//corresponding character takes the damage
					if (match.isVert()) {
						damageStep(match, charI, enemy, enemyName);
					}
					else {
						tileS = CHAR_S - (match.getX() % CHAR_S);
						damageStep(match, charI, enemy, enemyName);
						//if tiles are shared between two characters horizontally 2nd character takes damage after this
						charI++;
						tileS = CHAR_S - tileS;
						if (tileS != 0) {
							damageStep(match, charI, enemy, enemyName);
						}
					}

					sleep(50);
				}

			} while (match != null);

			if (count > 0) {
				isChanged = true;
				sleep(800);
				log.add("Empty color tiles are filled.");
			}

		} while (fillEmptyColors());

		return isChanged;
	}

	/**
	 * Calculates and deals the damage given by match to the corresponding character.
	 * @param match Color match.
	 * @param charI Index of the character.
	 * @param enemy Enemies that the damage will be dealt to.
	 * @param enemyName Name of the enemy.
	 */
	private void damageStep(ColorMatch match, int charI, CharacterBox[] enemy, String enemyName) {
		int damage;
		int remHealth;
		if (enemy[charI].isAlive()) {
			damage = calcDamage(match, enemy[charI]);
			remHealth = enemy[charI].takeDamage(damage);
			log.add("    " + enemyName + "[" + charI + "] took " + damage + " damage. Remaining health is " + remHealth);
			if (!enemy[charI].isAlive())
				log.add(enemyName + "[" + charI + "] died.");
		}
	}

	/**
	 * If clicked colors in the board are adjacent, switches them witch each other.
	 * @return True colors are switched.
	 */
	private boolean switchClickedColors() {
		int[][] ij = new int[2][2];
		int n = 0;
		boolean retVal = false;

		for (int i = 0; i < ROW_S; i++) {
			for (int j = 0; j < COL_S; j++) {
				if (colors[i][j].isClicked() && n < 2) {
					ij[n][0] = i;
					ij[n][1] = j;
					n++;
				}
			}
		}

		//check if two buttons are clicked
		if (n == 2) {
			//check if clicked buttons are adjacent
			if (Math.abs(ij[0][0] - ij[1][0]) + Math.abs(ij[0][1] - ij[1][1]) == 1) {
				Color temp = colors[ij[0][0]][ij[0][1]].getColor();
				colors[ij[0][0]][ij[0][1]].setColor(colors[ij[1][0]][ij[1][1]].getColor());
				colors[ij[1][0]][ij[1][1]].setColor(temp);
				retVal = true;
			}
			else
				System.out.println("Clicked colors are not adjacent.");

			colors[ij[0][0]][ij[0][1]].resetClicked();
			colors[ij[1][0]][ij[1][1]].resetClicked();
		}

		return retVal;
	}

	/**
	 * Sleeps for n milliseconds. Slows the game for the user to understand
	 * what is going on with the matches and the filling of the board.
	 * @param n Sleeping time in milliseconds
	 */
	private void sleep(int n) {
		try {
			TimeUnit.MILLISECONDS.sleep(n);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates a color board with no matching colors at the start. Then takes their moves
	 * from the users, matches the colors, fills the empty spaces in the board, applies the
	 * damages to corresponding characters. And loops.
	 */
	private void boardLoop() {
		while (true) {
			System.out.print("");
			userTurn();
			sleep(500);
			enemyTurn();
		}
	}

	/**
	 * User makes a move until he/she clicks a valid combination of colors.
	 */
	private void userTurn() {
		lock.lock();
		boolean validMove = true;
		try {
			do {
				if (!validMove)
					log.add("Invalid move. Clicked colors must be adjacent. Try again.");
				else
					log.add("-USER TURN-");
				// wait for two buttons to be clicked
				clickedTwoButtons.await();
				//user turn
				sleep(300);
				userTurn = true;
				validMove = switchClickedColors();
			} while (!validMove);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		matchColorsAndFillBoard();
		checkWinCond();
	}

	/**
	 * Enemy makes a move.
	 */
	private void enemyTurn() {
		log.add("-ENEMY TURN-");
		timer.stop();
		sleep(1000);
		//enemy makes random move
		userTurn = false;
		EnemyMove move = enemyRandMove();

		//loop making a rand move until there is a move with a match
		while (!matchFound()) {
			//take the move random move bac k and try again
			takeEnemyRandMoveBack(move);
			move = enemyRandMove();
		}
		sleep(1000);
		timer.start();
		matchColorsAndFillBoard();
		checkWinCond();
	}

	/**
	 * Makes a random move.
	 * @return Returns the properties of the move for later use.
	 */
	private EnemyMove enemyRandMove() {
		int i, j;
		boolean isVertical;
		Color temp;

		i = (int) (Math.random() * (ROW_S - 1));
		j = (int) (Math.random() * (COL_S - 1));

		isVertical = (int) (Math.random() * 2) == 0;
		temp = colors[i][j].getColor();
		if (isVertical) {
			colors[i][j].setColor(colors[i + 1][j].getColor());
			colors[i + 1][j].setColor(temp);
		}
		else {
			colors[i][j].setColor(colors[i][j + 1].getColor());
			colors[i][j + 1].setColor(temp);
		}
		return new EnemyMove(i, j, isVertical);
	}

	/**
	 * Takes the move back made by enemy if it is not a valid move.
	 * @param move The move which will be taken back.
	 */
	private void takeEnemyRandMoveBack(EnemyMove move) {
		int i = move.getI(), j = move.getJ();
		Color temp = colors[i][j].getColor();;

		if (move.isVertical()) {
			colors[i][j].setColor(colors[i + 1][j].getColor());
			colors[i + 1][j].setColor(temp);
		}
		else {
			colors[i][j].setColor(colors[i][j + 1].getColor());
			colors[i][j + 1].setColor(temp);
		}
	}

	/**
	 * Checks if any of the players won. If they did resets the characters and restarts the game.
	 */
	private void checkWinCond() {
		CharacterBox[] enemy;
		boolean win = true;

		if (userTurn)
			enemy = e;
		else
			enemy = c;

		for (int i = 0; i < CHAR_S; i++)
			if (enemy[i].isAlive())
				win = false;

		if (win) {
			sleep(1000);
			if (userTurn)
				log.add("All enemy characters died. USER WINS!");
			else
				log.add("All user characters died. ENEMY WINS!");
			
			sleep(1000);
			log.add("Restarting the game...");

			sleep(1000);
			log.add("Reinitializing the characters...");
			c[0].resetCharacter(Color.red);
			c[1].resetCharacter(Color.blue);
			c[2].resetCharacter(Color.green);
			e[0].resetCharacter(Color.red);
			e[1].resetCharacter(Color.blue);
			e[2].resetCharacter(Color.green);

			sleep(1000);
			log.add("Reinitializing the color table...");
			resetColorBoard();
			sleep(1000);
			log.add("Loser of the previous game starts the new game.");
		}

	}

	/**
	 * Calculates the damage dealt to character according to some specific properties given in the pdf.
	 * @param match Color match which gives damage to the character.
	 * @param enemy Character which takes damage.
	 * @return Calculated damage.
	 */
	public int calcDamage(ColorMatch match, CharacterBox enemy) {
		double dmgMod = 1, charStr;
		Color color = match.getColor();

		CharacterBox[] userChars;
		if (userTurn)
			userChars = c;
		else
			userChars = e;

		if (color.equals(Color.red)) {
			if (enemy.getColor().equals(Color.green))
				dmgMod = 2;
			else if (enemy.getColor().equals(Color.blue))
				dmgMod = 0.5;
		}
		else if (color.equals(Color.blue)) {
			if (enemy.getColor().equals(Color.red))
				dmgMod = 2;
			else if (enemy.getColor().equals(Color.green))
				dmgMod = 0.5;
		}
		else if (color.equals(Color.green)) {
			if (enemy.getColor().equals(Color.blue))
				dmgMod = 2;
			else if (enemy.getColor().equals(Color.red))
				dmgMod = 0.5;
		}

		if (color.equals(Color.red))
			charStr = userChars[0].getStrength();
		else if (color.equals(Color.blue))
			charStr = userChars[1].getStrength();
		else
			charStr = userChars[2].getStrength();

		return (int) (STD_DMG * dmgMod * match.getCount() * Math.pow((charStr / enemy.getAgility()), 1.35));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		for (int i = 0; i < CHAR_S; i++) {
			c[i].updateText();
			e[i].updateText();
		}
		log.update();
		panel.repaint();
	}

/*---public---*/

	@Override
	public void run() {
		boardLoop.start();
	}

}
