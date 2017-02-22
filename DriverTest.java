import org.junit.Assert;

import java.util.Arrays;

/**
 * Created by elias on 21/02/17.
 */
public class DriverTest {
    @org.junit.Test
    public void sort() throws Exception {
        FileHeapSort hs = new FileHeapSort(1000);
        FileInsertionSort is = new FileInsertionSort(1000);
        Driver d = new Driver(Arrays.asList(hs, is));
        int[] result = d.sort("smalldata");
        for (int i : result) System.out.println(i);
        Assert.assertEquals(10, result.length);
    }
}