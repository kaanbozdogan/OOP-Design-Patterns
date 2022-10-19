/**
 * Can return an iterator to iterate over it's items.
 */
public interface IterableCollection {

	/**
	 * Returns an email iterator.
	 * @return An email iterator.
	 */
	EmailIterator getIterator();
}
