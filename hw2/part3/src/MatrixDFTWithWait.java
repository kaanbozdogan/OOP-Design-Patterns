/**
 * Class that sums and calculates the dft of two matrix.
 * Synchronization barrier is implemented with wait() and notifyAll().
 */
public class MatrixDFTWithWait extends AbstractMatrixDFT {
	/**
	 * Holds the number of waiting threads.
	 */
	protected static class Waiting {
		int count;
		public Waiting(int count) {
			this.count = count;
		}
	}

	/**
	 * The object that threads will wait on.
	 */
	private final Waiting waitingThreads;

	public MatrixDFTWithWait() {
		super();
		this.waitingThreads = new Waiting(0);
	}

	@Override
	protected void synchBarrier(int quarter) {
		synchronized (waitingThreads) {
			waitingThreads.count++;
			System.out.println("t" + quarter + " waiting. " + waitingThreads.count + " threads waiting.");

			if (waitingThreads.count < 4) {
				try {
					waitingThreads.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("t" + quarter + " notifying all.");
				waitingThreads.notifyAll();
			}
		}
	}
}
