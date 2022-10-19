
/**
 * Personal email. Leaf class of the design pattern.
 */
public class PersonalEmail extends AbstractEmail {

	/**
	 * Creates a personal email.
	 * @param email Personal email.
	 * @param name Name of the person.
	 */
	public PersonalEmail(String email, String name) {
		super(email, name);
	}

	@Override
	public void print(int level) {
		printTabs(level);
		System.out.println("Name: " + name + ", Email: " + email);
	}

	@Override
	public EmailIterator getIterator() {
		return new PersonalEmailIterator(this);
	}
}
