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
	public int[] Sort(String filePath) throws LocalException {
		Timer t = new Timer();
		InsertionSort is = new InsertionSort(filePath, pFailure);
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
		if (is.completed()) {
			return is.getResult();
		} else {
			throw new LocalException("InsertionSort implementation failed");
		}
	}

	public class InsertionSort extends Thread
	{
		private int[] arry;
		private boolean complete;
		private double pFailure;
		private String path;

		public InsertionSort(String path, double pFailure) {
			this.complete = false;
			this.path = path;
			this.pFailure = pFailure;
		}

		public void run() {
			arry = sort(path, pFailure);
			complete = arry != null; // our native method returns null on failure
		}

		public boolean completed() {
			return this.complete;
		}

		public int[] getResult() {
			return this.arry;
		}

		// sort a file at 'path' with a memory access failure rate of 'pFailure'
		// returns sorted array on success and null on failure.
		public native int[] sort(String path, double pFailure);
	}
}
