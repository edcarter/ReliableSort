import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class MyHeapSort implements FileSorter
{
	private int timeoutMs;

	public MyHeapSort(int timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	@Override
	public int[] Sort(String filePath) throws LocalException {
		int[] numbers = readNumbers(filePath);
		Timer t = new Timer();
		HeapSort hs = new HeapSort(numbers);
		Watchdog w = new Watchdog(hs);
		t.schedule(w, timeoutMs);
		hs.start();
		try {
			/* there is a slight possiblity of a race condition
			where the thread joins because it finished, but then it is
			stopped by the watchdog before we can cancel the timer */
			hs.join();
			t.cancel();
		} catch (InterruptedException ex) {
			throw new LocalException("caught thread interrupted exception");
		}
		if (hs.completed()) {
			return hs.getResult();
		} else {
			throw new LocalException("HeapSort implementation failed");
		}
	}

	public static int[] readNumbers(String path) {
		ArrayList<Integer> ints = new ArrayList<Integer>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			for (String line; (line = br.readLine()) != null;) {
				ints.add(Integer.parseInt(line));
			}
		} catch (IOException ex) {
			System.out.println("Unable to read number from file, error is: " + ex.getMessage());
		} catch (NumberFormatException ex) {
			System.out.println("Unable to parse integer, error is: " + ex.getMessage());
		}
		int[] readInts = new int[ints.size()];
		for (int i = 0; i < ints.size(); i++) readInts[i] = ints.get(i);
		return readInts;
	}

	class HeapSort extends Thread
	{
		private int[] arry;
		private boolean complete;

		public HeapSort(int[] arry) {
			this.arry = arry;
			complete = false;
		}

		public void run() {
			while (true) {}
			//complete = true;
		}

		public boolean completed() {
			return this.complete;
		}

		public int[] getResult() {
			return this.arry;
		}
	}
}
