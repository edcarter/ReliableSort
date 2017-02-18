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
		} catch (InterruptedException ex) { /*todo gracefully exit*/ }

		if (w.cancelled) {
			System.out.println("primary algorithm timed out");
			System.loadLibrary(insSortLibrary);
			MyInsertionSort is = new MyInsertionSort(send_buf);
			t = new Timer();
			w = new Watchdog(is);
			t.schedule(w, watchdogTimeoutMs);
			is.start();
			try {
				is.join();
				t.cancel();
			} catch (InterruptedException ex) { /*todo gracefully exit*/}
		}

		if (w.cancelled) {
			System.out.println("Secondary algorithm timed out");
		}
		for (int i : send_buf) System.out.println("send_buf= " + i);
	}
}

