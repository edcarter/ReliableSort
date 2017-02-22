import java.util.Timer;

public class FileInsertionSort implements FileSorter
{
	private int timeoutMs;
	private static String insSortLibrary = "inssort";

	public FileInsertionSort(int timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	@Override
	public int[] Sort(String filePath) throws LocalException {
		Timer t = new Timer();
		InsertionSort is = new InsertionSort(filePath);
		Watchdog w = new Watchdog(is);
		System.loadLibrary(insSortLibrary);
		t.schedule(w, timeoutMs);
		is.start();
		try {
			/* there is a slight possiblity of a race condition
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
		private String path;

		public InsertionSort(String path) {
			this.complete = false;
			this.path = path;
		}

		public void run() {
			arry = sort(path);
			complete = true;
		}

		public boolean completed() {
			return this.complete;
		}

		public int[] getResult() {
			return this.arry;
		}

		public native int[] sort(String path);
	}
}
