import java.util.Arrays;

/**
 * Created by elias on 23/02/17.
 */
public class DataSort {

    public static void main(String[] args) {
        String inputFile;
        String outputFile;
        double pfail;
        int timeout;
        try {
            inputFile = args[0];
            outputFile = args[1];
            pfail = Double.parseDouble(args[2]);
            timeout = Integer.parseInt(args[3]);
        } catch (Exception ex) {
            usage();
            return;
        }

        FileHeapSort hs = new FileHeapSort(timeout, pfail);
        FileInsertionSort is = new FileInsertionSort(timeout, pfail);
        Driver d = new Driver(Arrays.asList(hs, is));
        try {
            d.sort(inputFile, outputFile);
        } catch (FailureException ex) {
            System.out.println("Caught failure exception: " + ex.getMessage());
            return;
        }
        System.out.println("SUCCESS!");
    }

    private static void usage() {
        System.out.println("Usage: DataSort [unsorted_file] [sorted_file] [p_memory_fail] [variant_timeout]");
    }
}
