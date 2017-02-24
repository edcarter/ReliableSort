import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by elias on 23/02/17.
 */
public class FileToArray {
    public static int[] FromFile(String path) {
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
