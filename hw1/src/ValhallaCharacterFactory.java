import java.awt.*;

/**
 * Creates a character with Valhalla style.
 */
public class ValhallaCharacterFactory extends AbstractCharacterFactory {

	@Override
	public AbstractCharacter getCharacter(Color color) {

		return modifyCharacterStats(super.getCharacter(color), 1.3,0.4,1.3, "Valhalla");
	}
}
