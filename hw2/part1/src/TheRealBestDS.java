/**
 * Makes the world's best even better because now its thread safe.
 * Proxy class for the BEstDSEver.
 * @param <T> Template type.
 */
public class TheRealBestDS<T> implements DSInterface<T> {

	/**
	 * Main class that we are masking with this proxy class.
	 */
	private BestDSEver<T> ds;

	public TheRealBestDS() {
		ds = new BestDSEver<>();
	}

	@Override
	public synchronized void insert(T o) {
		System.out.println("Started: insert(" + o.toString() + ")");
		ds.insert(o);
		System.out.println("Ended: insert(" + o.toString() + ")\n");
	}

	@Override
	public synchronized void remove(T o) {
		System.out.println("Started: remove(" + o.toString() + ")");
		ds.remove(o);
		System.out.println("Ended: remove(" + o.toString() + ")\n");
	}

	@Override
	public synchronized T get(int index) {
		System.out.println("Started: get(" + index + ")");
		T retval = ds.get(index);
		System.out.println("Ended: get(" + index + ")\n");

		return retval;
	}
}
