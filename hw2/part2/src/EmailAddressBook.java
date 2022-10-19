/**
 * A class that holds personal and group emails in a tree structure.
 */
public class EmailAddressBook {

	/**
	 * First level of the email address tree.
	 */
	GroupEmail rootList;

	/**
	 * Creates an empty email list.
	 */
	public EmailAddressBook() {
		this.rootList = new GroupEmail(null, null);
	}

	/**
	 * Creates an email list with given composite structure.
	 * @param rootList Given composite structure.
	 */
	public EmailAddressBook(GroupEmail rootList) {
		this.rootList = rootList;
	}

	/**
	 * Prints the email list like a tree structure.
	 */
	public void print() {
		EmailIterator it = rootList.getIterator();

		while (it.hasNext())
			it.next().print(0);
	}

	/**
	 * Adds a email to first level of the email list.
	 * @param email Added email.
	 * @return True if email is added.
	 */
	public boolean add(AbstractEmail email) {
		rootList.add(email);
		return true;
	}

	/**
	 * Adds an email to the given email group if the group exist.
	 * @param email  Added email.
	 * @param group Email group which email is added to.
	 * @return True if email is added.
	 */
	public boolean add(AbstractEmail email, String group) {
		return addHelper(rootList, email, group);
	}

	/**
	 * Recursive helper method to traverse the list to find the given group.
	 * @param currGroup Current group in the recursion
	 * @param email Added email
	 * @param group Searched group.
	 * @return True if email is added.
	 */
	private boolean addHelper(AbstractEmail currGroup, AbstractEmail email, String group) {
		boolean retVal = false;
		EmailIterator it = currGroup.getIterator();

		while (it.hasNext() && !retVal) {
			AbstractEmail curr = it.next();

			if (curr instanceof GroupEmail) {
				if (curr.name != null && curr.name.equals(group)) {
					((GroupEmail) curr).add(email);
					retVal = true;
				}
				else
					retVal = addHelper(curr, email, group);
			}
		}

		return retVal;
	}
}
