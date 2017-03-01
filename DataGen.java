import java.util.Random;

public class DataGen
{
	public static void main(String[] args) 
	{
		int numValues;
		String outputPath;
		try
		{
			numValues = Integer.parseInt(args[0]);
			outputPath = args[1];
		} catch (Exception ex) {
			usage();
			return;
		}

		int[] nums = new int[numValues];
		Random rand = new Random();
		for (int i = 0; i < numValues; i++) nums[i] = rand.nextInt();
		FileToArray.ToFile(outputPath, nums);
	}

	private static void usage() {
		System.out.println("Usage: DataGen [num_values] [output_path]");
	}
}
