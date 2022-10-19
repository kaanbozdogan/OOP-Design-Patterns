import java.util.concurrent.TimeUnit;

/**
 * Class that sums and calculates the dft of 2 matrix
 */
public abstract class AbstractMatrixDFT {
	/**
	 * Threads that does the calculation.
	 */
	protected class CalcThread implements Runnable {
		/**
		 * Quarter of the matrix that the thread is responsible of.
		 */
		private int quarter;

		public CalcThread(int quarter) {
			this.quarter = quarter;
		}

		@Override
		public void run() {
			int iStart = ((quarter - 1) / 2) * (ROW_S / 2),
					iEnd = 	iStart + (ROW_S / 2),
					jStart = ((quarter + 1) % 2) * (COL_S / 2),
					jEnd = jStart + (ROW_S / 2);

			System.out.println("t" + quarter + " starts.");
			sum(iStart, iEnd, jStart, jEnd, quarter);

			synchBarrier(quarter);

			dft(iStart, iEnd, jStart, jEnd, quarter);
		}
	}

	/**
	 * Two matrix that is operated on.
	 */
	protected Matrix m1, m2;
	protected static final int ROW_S = 1024, COL_S = 1024;

	public AbstractMatrixDFT() {
		System.out.println("Creating matrix 1.");
		m1 = new Matrix(ROW_S, COL_S);
		System.out.println("Matrix 1 created. \n");
		System.out.println("Creating matrix 2.");
		m2 = new Matrix(ROW_S, COL_S);
		System.out.println("Matrix 2 created. \n");
	}

	/**
	 * Sums m1 and m2 between given indexes.
	 * @param iStart Start of row index.
	 * @param iEnd End of row index.
	 * @param jStart Start of column index.
	 * @param jEnd End of column index.
	 * @param threadI Thread no doing the calculation.
	 */
	protected void sum(int iStart, int iEnd, int jStart, int jEnd, int threadI) {
		System.out.println("t" + threadI + ". Calculating sum.");

		for (int i = iStart; i < iEnd; i++) {
			for (int j = jStart; j < jEnd; j++) {
				m1.set(i, j, Complex.sum(m1.get(i,j), m2.get(i,j)));
			}
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("t" + threadI + " sum ended.");
	}

	/**
	 * Calculates the dft of m1 and m2 between given indexes.
	 * @param iStart Start of row index.
	 * @param iEnd End of row index.
	 * @param jStart Start of column index.
	 * @param jEnd End of column index.
	 * @param threadI Thread no doing the calculation.
	 */
	protected void dft(int iStart, int iEnd, int jStart, int jEnd, int threadI) {
		System.out.println("t" + threadI + " calculating dft.");

		for (int i = iStart; i < iEnd; i++) {
			for (int j = jStart; j < jEnd; j++) {
				//dft calculation
			}
		}
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("t" + threadI + " dft ended.");
	}

	/**
	 * Calculation is done by creating and executing the threads that does the calculations.
	 */
	public void calculate() {
		Thread[] threads = new Thread[4];

		for (int i = 0; i < 4; i++)
			threads[i] = new Thread(new CalcThread(i));

		for (int i = 0; i < 4; i++)
			threads[i].start();

		for (int i = 0; i < 4; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Implementation of the synchronization barrier.
	 * @param quarter The quarter the thread operates on.
	 */
	protected abstract void synchBarrier(int quarter);
}
