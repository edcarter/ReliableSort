import java.util.List;

public class Driver
{
	private List<FileSorter> variants;

	public Driver(List<FileSorter> variants) {
		this.variants = variants;
	}

	public void sort(String unsortedPath, String sortedPath) throws FailureException {
		boolean passed = false;
		for (FileSorter variant : variants) {
			try {
				variant.Sort(unsortedPath, sortedPath);
				if (!adjudicate(unsortedPath, sortedPath)) {
					throw new LocalException("variant failed adjudicator");
				} else {
					passed = true;
					System.out.println("passed: " + variant.getClass().getSimpleName());
					break;
				}
			} catch (LocalException ex) {
				System.out.println(String.format("LocalException, variant %s failed. Attempting recovery.",
						variant.getClass().getSimpleName()));
			}
		}
		if (!passed) throw new FailureException("All Variants Have Failed!");
	}

	// check that sorted ints have the correct length, order, and checksum
	private boolean adjudicate(String unsortedPath, String sortedPath) {
		int[] unsorted = FileToArray.FromFile(unsortedPath);
		int[] sorted = FileToArray.FromFile(sortedPath);
		int sum1 = 0;
		int sum2 = 0;
		int last = sorted[0];

		if (unsorted.length != sorted.length) return false; // different lengths
		for (int i = 0; i < unsorted.length; i++) {
			sum1 += unsorted[i];
			sum2 += sorted[i];
			if (last > sorted[i]) return false; // not sorted
		}
		if (sum1 != sum2) return false; // different check sums
		return true;
	}
}

