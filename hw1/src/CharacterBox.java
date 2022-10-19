import javax.swing.*;
import java.awt.*;

/**
 * The character Box showed int the game UI.
 */
public class CharacterBox {
	/** Owner character of the box. */
	private AbstractCharacter character;
	/** Character label printed to the game UI. */
	JLabel label;

	public CharacterBox(AbstractCharacter character) {
		this.character = character;
		label = new JLabel();
		label.setForeground(Color.white);
		label.setBackground(character.getColor());
		label.setOpaque(true);
		label.setBorder(BorderFactory.createLineBorder(Color.gray, 5));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		updateText();
	}

	public AbstractCharacter getCharacter() {
		return character;
	}

	public void setCharacter(AbstractCharacter character) {
		this.character = character;
	}

	public double getStrength() {
		return character.getStrength();
	}

	public double getAgility() {
		return character.getAgility();
	}

	public double getHealth() {
		return character.getHealth();
	}

	public Color getColor() {
		return character.getColor();
	}

	public String getType() {
		return character.getType();
	}

	public String getStyle() {
		return character.getStyle();
	}

	public JLabel getLabel() {
		return label;
	}

	/**
	 * Updates the stats of the character showed in the game UI.
	 */
	public void updateText() {
		label.setText("<html>" + character.getStyle() + " " + character.getType() +
				"<br/>Strength = " + character.getStrength() +
				"<br/>Agility = " + character.getAgility() +
				"<br/>Health = " + character.getHealth() + "<html>");
	}

	/**
	 * Character loses health equal to the damage.
	 * @param damage Damage given.
	 * @return Remaining health.
	 */
	public int takeDamage(int damage) {
		int remHealth = character.takeDamage(damage);

		if (!character.isAlive())
			label.setVisible(false);

		return remHealth;
	}

	/**
	 * Returns if the character is alive.
	 * @return True if character is alive.
	 */
	public boolean isAlive() {
		return character.isAlive();
	}

	public void resetCharacter(Color color) {
		character = AbstractCharacterFactory.getRandomFactory().getCharacter(color);
		label.setVisible(true);
	}
}
