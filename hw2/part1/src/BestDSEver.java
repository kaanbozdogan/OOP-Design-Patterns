import java.util.ArrayList;

/**
 * The best data structure in the world because it does every thing in linear time.
 * @param <T> Template type
 */
public class BestDSEver<T> implements DSInterface<T> {

	/**
	 * Holds the data.
	 */
	private ArrayList<T> data;

	public BestDSEver() {
		data = new ArrayList<>();
	}

	@Override
	public void insert(T o) {
		data.add(o);
	}

	@Override
	public void remove(T o) {
		data.removeIf(e -> e.equals(o));
	}

	@Override
	public T get(int index) {
		if (index < data.size())
			return data.get(index);
		return null;
	}
}
