import java.util.*;
import java.lang.*;
import java.io.Console;

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
				// call adjudicator
			} catch (LocalException ex) {
				System.out.println(String.format("LocalException, variant %s failed. Attempting recovery.",
						variant.getClass().getSimpleName()));
			}
		}
		if (result == null) throw new FailureException("All Variants Have Failed!");
        return result;
	}
}

