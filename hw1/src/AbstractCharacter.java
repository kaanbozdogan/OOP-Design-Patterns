import javax.swing.*;
import java.awt.*;

/**
 * Holds the properties of a character.
 */
public abstract class AbstractCharacter{

	private double strength;
	private double agility;
	private int health;
	private String type;
	private String style;
	private Color color;

	public AbstractCharacter(int strength, int agility, int health, Color color, String type) {
		this.strength = strength;
		this.agility = agility;
		this. health = health;
		this.color = color;
		this.type = type;
		this.style = null;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public double getAgility() {
		return agility;
	}

	public void setAgility(double agility) {
		this.agility = agility;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Character loses health equal to the damage.
	 * @param damage Damage given.
	 * @return Remaining health.
	 */
	public int takeDamage(int damage) {
		health -= damage;
		return Math.max(health, 0);
	}

	/**
	 * Returns if the character is alive.
	 * @return True if character is alive.
	 */
	public boolean isAlive() {
		return health > 0;
	}
}
