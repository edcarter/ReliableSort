/* Derrived from Dr. Dick's Watchdog.java for ECE 422 */

import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("deprecation")
public class Watchdog extends TimerTask {

	private Thread watched;
	public Boolean cancelled = false;

	public Watchdog (Thread target){
		watched = target;
	}

	public void run() {
		cancelled = true;
		watched.stop();
	}

}
