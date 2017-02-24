import org.junit.Assert;

import java.io.File;
import java.util.Arrays;

/**
 * Created by elias on 21/02/17.
 */
public class DriverTest {
    @org.junit.Test
    public void sort() throws Exception {
        int size = 1000;
        String testPath = "deleteme";

        File f = new File(testPath);
        if (f.isFile()) f.delete();

        DataGen.main(new String[] {Integer.toString(size), testPath});

        FileHeapSort hs = new FileHeapSort(1000, 0);
        FileInsertionSort is = new FileInsertionSort(1000, 0);
        Driver d = new Driver(Arrays.asList(hs, is));
        int[] result = d.sort(testPath);
        Assert.assertEquals(size, result.length);

        int last = result[0];
        for (int i = 0; i < size; i++) {
            Assert.assertTrue(last <= result[i]);
            last = result[i];
        }
    }
}