public class MyInsertionSort extends Thread
{
	public int[] buf;

	public MyInsertionSort(int[] buf) {
		super();
		this.buf = buf;
	}

	public void run() {
		insertionsort(buf);
	}

	public native int[] insertionsort(int[] buf);
}
