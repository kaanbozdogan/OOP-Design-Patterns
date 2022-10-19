import java.awt.*;

/**
 * Creates a character with Underwild style.
 */
public class UnderwildCharacterFactory extends AbstractCharacterFactory {

	@Override
	public AbstractCharacter getCharacter(Color color) {

		return modifyCharacterStats(super.getCharacter(color), 0.8,1.6,0.8, "Underwild");
	}
}
