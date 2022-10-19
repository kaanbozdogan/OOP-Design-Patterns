/**
 * Iterates on emails.
 */
public interface EmailIterator {

	/**
	 * If the iterator has any more items to iterate on.
	 * @return True if it has.
	 */
	boolean hasNext();

	/**
	 * Returns The next element in the iteration.
	 * @return The next element in the iteration
	 */
	AbstractEmail next();
}
