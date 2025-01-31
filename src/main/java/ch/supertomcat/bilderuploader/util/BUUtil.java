package ch.supertomcat.bilderuploader.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * This class provides methods, which are often used.
 */
public class BUUtil {
	/**
	 * Changes the root logger level
	 * 
	 * @param level Level
	 */
	public static void changeLog4JRootLoggerLevel(Level level) {
		LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
		Configuration config = loggerContext.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		loggerConfig.setLevel(level);
		loggerContext.updateLoggers(config);
	}

	/**
	 * This method filters not allowed chars in paths or filenames
	 * You must set noPath to true, if the string is only a filename.
	 * 
	 * @param str String
	 * @param noPath Is no path
	 * @return Corrected String
	 */
	public static String correctFileString(String str, boolean noPath) {
		String result = str;

		if (noPath) {
			// if the String is not a path we filter more chars
			result = result.replaceAll("[\"*<>?|/:\\\\]", "");
		} else {
			result = result.replaceAll("[\"*<>?|]", "");
		}
		return result;
	}
}
