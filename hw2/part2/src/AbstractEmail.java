/**
 * Abstract class with properties of a classic email.
 */
public abstract class AbstractEmail implements IterableCollection {

	/**
	 * Email address.
	 */
	protected String email;
	/**
	 * Name of the owner of the email address.
	 */
	protected String name;

	/**
	 * Creates an email address with the given email and name.
	 * @param email Email address.
	 * @param name Name of the owner.
	 */
	public AbstractEmail(String email, String name) {
		this.email = email;
		this.name = name;
	}

	/**
	 * Prints the contents of the email address.
	 * @param level Which level the email is at, in the tree structure, in the composite pattern.
	 */
	abstract public void print(int level);

	protected void printTabs(int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("\t");
		}
	}

}
