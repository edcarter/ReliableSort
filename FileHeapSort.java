import java.util.Timer;

public class FileHeapSort implements FileSorter
{
	private int timeoutMs;
	private double pFailure;

	FileHeapSort(int timeoutMs, double pFailure) {
		this.timeoutMs = timeoutMs;
		this.pFailure = pFailure;
	}

	@Override
	public void Sort(String unsortedPath, String sortedPath) throws LocalException {
		int[] numbers = FileToArray.FromFile(unsortedPath);
		Timer t = new Timer();
		HeapSort hs = new HeapSort(numbers, pFailure);
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
		if (!hs.completed())
			throw new LocalException("HeapSort implementation failed");
	}

	class HeapSort extends Thread
	{
		private int[] arry;
		private boolean complete;
		private double pFailure;
		private int memoryAcceses;

		HeapSort(int[] arry, double pFailure) {
			this.arry = arry;
			this.pFailure = pFailure;
			this.complete = false;
			this.memoryAcceses = 0;
		}

		public void run() {
			sort(arry);
			// todo write arry to file
			complete = true;
		}

		boolean completed() {
			return this.complete;
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
