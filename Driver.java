import java.io.*;
import java.util.*;
import java.lang.*;

public class Driver
{
	static String insSortLibrary = "inssort";
	static int watchdogTimeoutMs = 1000;

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage:\n	java Driver [path_to_numbers]\n");
			return;
		}
		int[] send_buf = readNumbers(args[0]);

		MyHeapSort hs = new MyHeapSort(send_buf);
		Timer t = new Timer();
		Watchdog w = new Watchdog(hs);
		t.schedule(w, watchdogTimeoutMs);
		hs.start();
		try { 
			/* there is a slight possiblity of a race condition 
			where the thread joins because it finished, but then it is 
			stopped by the watchdog before we can cancel the timer */
			hs.join();
			t.cancel();

			if (w.cancelled) {
				System.out.println("timed out");
			} else {
				System.out.println("thread joined without cancellation");
			}
		} catch (InterruptedException ex) { }

		for (int i : send_buf) System.out.println("send_buf = " + i);
		MyInsertionSort is = new MyInsertionSort();
		System.loadLibrary(insSortLibrary);
		is.insertionsort(send_buf);
		for (int i : send_buf) System.out.println("send_buf= " + i);
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
}

