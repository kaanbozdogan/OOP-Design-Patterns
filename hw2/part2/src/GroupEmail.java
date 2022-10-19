import java.util.ArrayList;

/**
 * Composite class of the design pattern. Can contain personal or group emails.
 */
public class GroupEmail extends AbstractEmail {

	/**
	 * Email addresses this group contains
	 */
	private ArrayList<AbstractEmail> group;

	/**
	 * Creates an email group with given email and the group name
	 * @param email Given email.
	 * @param name Given group name.
	 */
	public GroupEmail(String email, String name) {
		super(email, name);
	}

	/**
	 * Creates an email group with given email, the group name and emails contained.
	 * @param email Given email.
	 * @param name Given group name.
	 * @param group Emails contained.
	 */
	public GroupEmail(String email, String name, ArrayList<AbstractEmail> group) {
		super(email, name);
		this.group = group;
	}

	@Override
	public void print(int level) {
		printTabs(level);
		System.out.println("Group Email: " + email + ", Group Name: " + name);

		EmailIterator it = getIterator();
		level++;
		while (it.hasNext())
			it.next().print(level);
	}

	@Override
	public EmailIterator getIterator() {
		return new GroupEmailIterator(this);
	}

	/**
	 * Adds an email to this group.
	 * @param element Email added.
	 */
	public void add(AbstractEmail element) {
		if (group == null)
			group = new ArrayList<>();
		group.add(element);
	}

	/**
	 * Gets the email at index.
	 * @param i index.
	 * @return Email at the index.
	 */
	public AbstractEmail get(int i) {
		return group.get(i);
	}

	/**
	 * Gets the count of emails contained in this group.
	 * @return Count of emails contained in this group.
	 */
	public int size() {
		if (group == null)
			return 0;
		else
			return group.size();
	}
}
