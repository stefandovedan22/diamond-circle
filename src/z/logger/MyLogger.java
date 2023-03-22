package z.logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/***
 * simple logger class
 * @author Stefan
 *
 */
public class MyLogger {
	
	static private SimpleFormatter formatterTxt;
	static private FileHandler fileTxt;
	static private Logger logger;
	
	public static void setup() throws IOException {
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.WARNING);
		
		fileTxt = new FileHandler("src"+File.separator+"z"+File.separator+"logger"+File.separator+"log.txt", true);
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		
		logger.addHandler(fileTxt);
	}
	
	public static void log(Level level, String message, Exception e) {
		logger.log(level, message, e);
	}
	
	public static void close() {
		fileTxt.close();
	}

}
