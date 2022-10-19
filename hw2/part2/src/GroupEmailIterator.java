/**
 * Iterator to iterate through the children of an email group.
 */
public class GroupEmailIterator implements EmailIterator {

	/**
	 * Iterated email group.
	 */
	private GroupEmail group;
	/**
	 * Current index.
	 */
	private int index;

	/**
	 * Creates a group email iterator of the given group.
	 * @param group Given group.
	 */
	public GroupEmailIterator(GroupEmail group) {
		this.group = group;
		index = 0;
	}

	@Override
	public boolean hasNext() {
		return index < group.size();
	}

	@Override
	public AbstractEmail next() {
		AbstractEmail retVal;

		if (index < group.size())
			retVal = group.get(index);
		else
			retVal = null;

		index ++;
		return retVal;
	}
}
