/**
 * Iterator for a personal email. Only iterates on one email.
 */
public class PersonalEmailIterator implements EmailIterator {

	/**
	 * One email that the iterator iterates on.
	 */
	private AbstractEmail email;
	/**
	 * If iterator iterated over one email it has.
	 */
	private boolean hasNext;

	/**
	 * Creates an iterator for the given personal email.
	 * @param email Given personal email.
	 */
	public PersonalEmailIterator(AbstractEmail email) {
		this.email = email;
		hasNext = true;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public AbstractEmail next() {
		if (hasNext) {
			hasNext = false;
			return email;
		}
		else
			return null;
	}
}
