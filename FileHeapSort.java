import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class FileHeapSort implements FileSorter
{
	private int timeoutMs;
	private double hazard;

	FileHeapSort(int timeoutMs, double hazard) {
		this.timeoutMs = timeoutMs;
		this.hazard = hazard;
	}

	@Override
	public int[] Sort(String filePath) throws LocalException {
		int[] numbers = readNumbers(filePath);
		Timer t = new Timer();
		HeapSort hs = new HeapSort(numbers, hazard);
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

	private static int[] readNumbers(String path) {
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
		private double hazard;
		private int memoryAcceses;

		HeapSort(int[] arry, double hazard) {
			this.arry = arry;
			this.hazard = hazard;
			this.complete = false;
			this.memoryAcceses = 0;
		}

		public void run() {
			sort(arry);
			complete = true;
		}

		boolean completed() {
			return this.complete;
		}

		int[] getResult() {
			return this.arry;
		}

		/* the below heap sort algorithm was provided by:
		  http://algs4.cs.princeton.edu/24pq/Heap.java.html
		  Accessed 17 Feb 2017
		 */

		/*
		   The {@code Heap} class provides a static methods for heapsorting
		   an array.
		   <p>
		   For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
		   <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.

		   @author Robert Sedgewick
		 *  @author Kevin Wayne
		 */

		/**
		 * Rearranges the array in ascending order, using the natural order.
		 * @param pq the array to be sorted
		 */
		void sort(int[] pq) {
			while (true) {}
/*			int n = pq.length;
			for (int k = n/2; k >= 1; k--)
				sink(pq, k, n);
			while (n > 1) {
				exch(pq, 1, n--);
				sink(pq, 1, n);
			}*/
		}

		/***************************************************************************
		 * Helper functions to restore the heap invariant.
		 ***************************************************************************/

		private void sink(int[] pq, int k, int n) {
			while (2*k <= n) {
				int j = 2*k;
				if (j < n && less(pq, j, j+1)) j++;
				if (!less(pq, k, j)) break;
				exch(pq, k, j);
				k = j;
			}
		}

		/***************************************************************************
		 * Helper functions for comparisons and swaps.
		 * Indices are "off-by-one" to support 1-based indexing.
		 ***************************************************************************/
		private boolean less(int[] pq, int i, int j) {
			return pq[i-1] < pq[j-1];
		}

		private void exch(int[] pq, int i, int j) {
			int swap = pq[i-1];
			pq[i-1] = pq[j-1];
			pq[j-1] = swap;
		}
	}
}
