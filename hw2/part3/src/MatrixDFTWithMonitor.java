import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that sums and calculates the dft of two matrix.
 * Synchronization barrier is implemented with lock and monitors.
 */
public class MatrixDFTWithMonitor extends AbstractMatrixDFT {
	/**
	 * Lock of monitor
	 */
	private final Lock lock;
	/**
	 * Condition variable of monitor
	 */
	private final Condition barrier;
	/**
	 * Waiting thread count
	 */
	private int waitingThreads;

	public MatrixDFTWithMonitor() {
		super();
		lock = new ReentrantLock();
		barrier = lock.newCondition();
		waitingThreads = 0;
	}

	@Override
	protected void synchBarrier(int quarter) {
		lock.lock();
		waitingThreads++;
		System.out.println("t" + quarter + " waiting. " + waitingThreads + " threads awaiting.");

		try {
			if (waitingThreads < 4) {
				barrier.await();
			} else {
				System.out.println("t" + quarter + " signalling all.");
				barrier.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
