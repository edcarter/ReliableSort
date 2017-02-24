import java.util.Arrays;

/**
 * Created by elias on 23/02/17.
 */
public class DataSort {

    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];
        double pfail = Double.parseDouble(args[2]);
        int timeout = Integer.parseInt(args[3]);
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
}
