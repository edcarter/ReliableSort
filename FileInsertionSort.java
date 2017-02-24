import java.util.Timer;

public class FileInsertionSort implements FileSorter
{
	private int timeoutMs;
	private double pFailure;
	private static String insSortLibrary = "inssort";

	public FileInsertionSort(int timeoutMs, double pFailure) {
		this.timeoutMs = timeoutMs;
		this.pFailure = pFailure;
	}

	@Override
	public void Sort(String unsortedPath, String sortedPath) throws LocalException {
		Timer t = new Timer();
		InsertionSort is = new InsertionSort(unsortedPath, sortedPath, pFailure);
		Watchdog w = new Watchdog(is);
		System.loadLibrary(insSortLibrary);
		t.schedule(w, timeoutMs);
		is.start();
		try {
			/* there is a slight possibility of a race condition
			where the thread joins because it finished, but then it is
			stopped by the watchdog before we can cancel the timer */
			is.join();
			t.cancel();
		} catch (InterruptedException ex) {
			throw new LocalException("caught thread interrupted exception");
		}
		if (!is.completed())
			throw new LocalException("InsertionSort implementation failed");
	}

	public class InsertionSort extends Thread
	{
		private boolean complete;
		private double pFailure;
		private String unsortedPath;
		private String sortedPath;

		public InsertionSort(String unsortedPath, String sortedPath, double pFailure) {
			this.complete = false;
			this.unsortedPath = unsortedPath;
			this.sortedPath = sortedPath;
			this.pFailure = pFailure;
		}

		public void run() {
			complete = sort(unsortedPath, sortedPath, pFailure) != -1;
		}

		public boolean completed() {
			return this.complete;
		}

		// sort a file at 'path' with a memory access failure rate of 'pFailure'
		// returns -1 on failure, otherwise success.
		public native int sort(String unsortedPath, String sortedPath, double pFailure);
	}
}
