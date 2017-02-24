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
        String unsortedPath = "unsorted";
        String sortedPath = "sorted";

        File f1 = new File(unsortedPath);
        File f2 = new File(sortedPath);
        if (f1.isFile()) f1.delete();
        if (f2.isFile()) f2.delete();

        DataGen.main(new String[] {Integer.toString(size), unsortedPath});

        FileHeapSort hs = new FileHeapSort(1000, 0);
        FileInsertionSort is = new FileInsertionSort(1000, 0);
        Driver d = new Driver(Arrays.asList(hs, is));
        d.sort(unsortedPath, sortedPath);

        int[] result = FileToArray.FromFile(sortedPath);
        Assert.assertEquals(size, result.length);

        int last = result[0];
        for (int i = 0; i < size; i++) {
            Assert.assertTrue(last <= result[i]);
            last = result[i];
        }
    }
}