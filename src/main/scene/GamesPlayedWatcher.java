package main.scene;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;

import javafx.application.Platform;
import z.logger.MyLogger;

public class GamesPlayedWatcher extends Thread {
	
	private Path directory;
	private int filesCount = 0;
	
	public GamesPlayedWatcher() {
		directory = Paths.get("src"+File.separator+"z"+File.separator+"files"+File.separator+"data");
	}

	@Override
	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
			WatchKey key = null;
			while(true) {
				key = watcher.take();
				WatchEvent.Kind<?> kind = null;
				for(WatchEvent<?> event : key.pollEvents()) {
					kind = event.kind();
					if(kind == StandardWatchEventKinds.OVERFLOW)
						continue;
					else if(kind == StandardWatchEventKinds.ENTRY_CREATE) {
						filesCount++;
						Platform.runLater(() -> MainViewController.gamesPlayedLabel.setText(Integer.toString(filesCount)));
					}
				}
				boolean valid = key.reset();
				if(!valid) {
					break;
				}
			}
		} catch (IOException e) {
			MyLogger.log(Level.WARNING, "IOException in file watcher", e);
		} catch (InterruptedException ex) {
			MyLogger.log(Level.WARNING, "InterruptedException in file watcher", ex);
		}
	}
}
