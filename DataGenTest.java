import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by elias on 21/02/17.
 */
public class DataGenTest {
    @Test
    public void main() throws Exception {
        int size = 1000;
        String testPath = "deleteme";

        File f = new File(testPath);
        if (f.isFile()) f.delete();

        DataGen.main(new String[] {Integer.toString(size), testPath});

        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(testPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Integer.parseInt(line); //try to parse every line to make sure it is formatted correctly
                count++;
            }
        }
        Assert.assertEquals(size, count);
        f.delete();
    }
}