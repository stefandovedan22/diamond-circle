package main.scene;

import java.util.logging.Level;

import javafx.application.Platform;
import z.logger.MyLogger;

/***
 * utility class for timer on main scene
 * @author Stefan
 *
 */
public class Timer extends Thread {

	private int minutes, seconds;
	
	public Timer() {
		minutes = seconds = 0;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				sleep(1000);
				seconds++;
				if(seconds == 60) {
					minutes++;
					seconds = 0;
				}
				Platform.runLater(() -> MainViewController.timeLabel.setText(minutes+":"+seconds));
			}

		} catch (InterruptedException e) {
			MyLogger.log(Level.WARNING, "Interrupted Exceeption in Timer class!!", e);
		}
	}
}
