import java.util.Random;

public class DataGen
{
	public static void main(String[] args) 
	{
		int numValues = Integer.parseInt(args[0]);
		String outputPath = args[1];
		int[] nums = new int[numValues];
		Random rand = new Random();
		for (int i = 0; i < numValues; i++) nums[i] = rand.nextInt();
		FileToArray.ToFile(outputPath, nums);
	}
}
