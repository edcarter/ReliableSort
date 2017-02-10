import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;

public class Driver
{
	static String insSortLibrary = "inssort";

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage:\n java Driver [path_to_numbers]\n");
			return;
		}
		int[] send_buf = readNumbers(args[0]);

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

