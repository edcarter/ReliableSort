import java.util.*;
import java.lang.*;
import java.io.Console;

public class Driver
{
	static int timeoutMs = 1000;

	public static void main(String[] args) {
		Console console = System.console();
		console.readLine();
		if (args.length != 1) {
			System.out.println("Usage:\n	java Driver [path_to_numbers]\n");
			return;
		}
		String numbersPath = args[0];
		int[] result;
		FileHeapSort fhs = new FileHeapSort(timeoutMs);
		try {
		    result = fhs.Sort(numbersPath);
        } catch (LocalException ex) {
		    FileInsertionSort fis = new FileInsertionSort(timeoutMs);
		    try {
                result = fis.Sort(numbersPath);
            } catch (LocalException ex1) {
		        return;
		        //throw new FailureException("All fallbacks have failed");
            }
        }
        for(int i : result) System.out.println(i);
	}
}

