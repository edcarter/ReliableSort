import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Driver
{
	private List<FileSorter> variants;

	public Driver(List<FileSorter> variants) {
		this.variants = variants;
	}

	public int[] sort(String numbersPath) throws FailureException {
		int[] result = null;
		for (FileSorter variant : variants) {
			try {
				result = variant.Sort(numbersPath);
				if (!adjudicate(result, numbersPath)) throw new LocalException("variant failed adjudicator");
			} catch (LocalException ex) {
				System.out.println(String.format("LocalException, variant %s failed. Attempting recovery.",
						variant.getClass().getSimpleName()));
			}
		}
		if (result == null) throw new FailureException("All Variants Have Failed!");
        return result;
	}

	// check that sorted ints have the correct length, order, and checksum
	private boolean adjudicate(int[] sorted, String numbersPath) {
		int[] numbers = readNumbers(numbersPath);
		int sum1 = 0;
		int sum2 = 0;
		int last = sorted[0];

		if (numbers.length != sorted.length) return false; // different lengths
		for (int i = 0; i < numbers.length; i++) {
			sum1 += numbers[i];
			sum2 += sorted[i];
			if (last > sorted[i]) return false; // not sorted
		}
		if (sum1 != sum2) return false; // different check sums
		return true;
	}

	@SuppressWarnings("Duplicates")
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
}

