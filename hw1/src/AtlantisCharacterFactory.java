import java.awt.*;

/**
 * Creates a character with Atlantis style.
 */
public class AtlantisCharacterFactory extends AbstractCharacterFactory {

	@Override
	public AbstractCharacter getCharacter(Color color) {

		return modifyCharacterStats(super.getCharacter(color), 0.8,1.2,1.2, "Atlantis");
	}
}
