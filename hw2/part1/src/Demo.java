public class Demo {

	public static void main(String[] args) {
		TheRealBestDS<Integer> ds = new TheRealBestDS<>();
		Thread[] threads = new Thread[5];

		for (int i = 0; i < 5; i++)
			threads[i] = new Thread(new DsThread(ds, i));

		for (int i = 0; i < 5; i++)
			threads[i].start();

		for (int i = 0; i < 5; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Threads that makes operations on the same data structure to show that it is thread safe.
	 */
	public static class DsThread implements Runnable {
		private TheRealBestDS<Integer> ds;
		int i;

		public DsThread(TheRealBestDS<Integer> ds, int i) {
			this.ds = ds;
			this.i = i;
		}

		@Override
		public void run() {
			ds.insert(i);
			ds.get(i);
			ds.remove(i);
		}
	}
}
