/**
 * Service interface of the proxy design pattern.
 * @param <T> Template type.
 */
public interface DSInterface<T> {

	/**
	 * Inserts an item to the data structure.
	 * @param o Inserted item.
	 */
	void insert(T o);

	/**
	 * Removes the item from the data structure.
	 * @param o The removed item.
	 */
	void remove(T o);

	/**
	 * Returns the item at the index.
	 * @param index Index of the item.
	 * @return The item at ith index.
	 */
	T get(int index);
}
