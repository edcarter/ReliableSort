import java.util.Random;
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
		Timer t = new Timer();
		HeapSort hs = new HeapSort(unsortedPath, sortedPath, pFailure);
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
		private boolean complete;
		private String unsortedPath;
		private String sortedPath;
		private double pFailure;
		private int memoryAcceses;

		HeapSort(String unsortedPath, String sortedPath, double pFailure) {
			this.unsortedPath = unsortedPath;
			this.sortedPath = sortedPath;
			this.pFailure = pFailure;
			this.complete = false;
			this.memoryAcceses = 0;
		}

		public void run() {
			int[] arry = FileToArray.FromFile(unsortedPath);
			sort(arry);
			FileToArray.ToFile(sortedPath, arry);
			complete = !estimateFailure();
		}

		// estimate whether a pseudo hardware failure occurred or not.
		// if true, we have a failure. otherwise there was no failure.
		private boolean estimateFailure() {
			Random rand = new Random();
			double hazard = pFailure * memoryAcceses;
			double myRand = rand.nextDouble();
			if (0.5 <= myRand && myRand <= 0.5 + hazard) return true;
			return false;
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
			int n = pq.length;                // 2 memory accesses
			memoryAcceses += 2;
			for (int k = n/2; k >= 1; k--) {  // 2 memory accesses
				sink(pq, k, n);               // 3 memory accesses
				memoryAcceses += 5;
			}
			while (n > 1) {                   // 1 memory access
				exch(pq, 1, n--);           // 2 memory accesses
				sink(pq, 1, n);             // 2 memory accesses
				memoryAcceses += 5;
			}
		}

		/***************************************************************************
		 * Helper functions to restore the heap invariant.
		 ***************************************************************************/

		private void sink(int[] pq, int k, int n) {
			while (2*k <= n) {                         // 2 memory accesses
				int j = 2*k;                           // 2 memory accesses
				if (j < n && less(pq, j, j+1)) j++;  // 4 memory accesses
				if (!less(pq, k, j)) break;            // 3 memory accesses
				exch(pq, k, j);                        // 3 memory accesses
				k = j;                                 // 2 memory accesses
				memoryAcceses += 16;
			}
		}

		/***************************************************************************
		 * Helper functions for comparisons and swaps.
		 * Indices are "off-by-one" to support 1-based indexing.
		 ***************************************************************************/
		private boolean less(int[] pq, int i, int j) {
			memoryAcceses += 4;
			return pq[i-1] < pq[j-1]; // 4 memory accesses
		}

		private void exch(int[] pq, int i, int j) {
			int swap = pq[i-1]; // 3 memory accesses
			pq[i-1] = pq[j-1];  // 3 memory accesses
			pq[j-1] = swap;     // 3 memory accesses
			memoryAcceses += 9;
		}
	}
}
