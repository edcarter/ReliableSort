import java.util.Random;
import java.io.IOException;
import java.io.PrintWriter;

public class DataGen
{
	public static void main(String[] args) 
	{
		int numValues = Integer.parseInt(args[0]);
		String outputPath = args[1];
		Random rand = new Random();
		try 
		{
			PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
			for ( ; numValues > 0; numValues--)
			{
				Integer randInt = rand.nextInt();
    				writer.println(randInt.toString());
			}
			writer.close();
		} catch (IOException e) {	
		}
	}
}
