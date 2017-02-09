public class Driver
{
	static int BUF_SIZE = 2;
	static String insSortLibrary = "inssort";

	public static void main(String[] args) {
		int[] send_buf = new int[BUF_SIZE];
		int[] recv_buf;
		for (int i=0; i<BUF_SIZE; i++) {
			send_buf[i] = i;
			System.out.println("send_buf["+i+"] = "+send_buf[i]);
		}
		MyInsertionSort is = new MyInsertionSort();
		System.loadLibrary(insSortLibrary);
		recv_buf = is.insertionsort(send_buf);
		for (int i=0; i<BUF_SIZE; i++) {
			System.out.println("recv_buf["+i+"] = "+recv_buf[i]);
		}
	}
}
