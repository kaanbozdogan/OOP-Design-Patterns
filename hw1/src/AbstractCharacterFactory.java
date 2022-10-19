import java.awt.*;

/**
 * Abstract Factory which provides an interface for character creation with some specific properties.
 */
public abstract class AbstractCharacterFactory {

	/**
	 * Returns a character with given properties and used factory style.
	 * @param color Color of the character returned.
	 * @return Character with given color and used factory style.
	 */
	public AbstractCharacter getCharacter(Color color) {
		if (Color.red.equals(color)) {
			return new FireCharacter();
		} else if (Color.blue.equals(color)) {
			return new IceCharacter();
		} else if (Color.green.equals(color)) {
			return new NatureCharacter();
		} else
			throw new IllegalArgumentException("Character color does not exist.");
	}

	/**
	 * Updates the character stats with given modifiers.
	 * @param character Character whose stats will be updated.
	 * @param strMod Strength modifier.
	 * @param aglMod Agility modifier.
	 * @param hltMod Health modifier.
	 * @param style Style of the character.
	 * @return Character with modified stats.
	 */
	protected AbstractCharacter modifyCharacterStats(AbstractCharacter character, double strMod, double aglMod, double hltMod, String style) {
		if (character != null) {
			character.setStrength(character.getStrength() * strMod);
			character.setAgility(character.getAgility() * aglMod);
			character.setHealth((int) (character.getHealth() * hltMod));
			character.setStyle(style);
		} else
			throw new IllegalArgumentException("Character color does not exist.");

		return character;
	}

	/**
	 * Returns a factory with random style.
	 * @return A factory with random style.
	 */
	static public AbstractCharacterFactory getRandomFactory() {
		int rand = (int) (Math.random() * 3);

		if (rand == 0)
			return new AtlantisCharacterFactory();
		else if (rand == 1)
			return new ValhallaCharacterFactory();
		else
			return new UnderwildCharacterFactory();
	}

}
