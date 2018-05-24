package de.tum.gis.tiles3d.util;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Logger{
	
	private static Logger INSTANCE = new Logger();

	private static LogLevel consoleLogLevel = LogLevel.INFO;

	private static Calendar cal;
	
	private static DecimalFormat df = new DecimalFormat("00");

	private Logger() {
	}

	public static Logger getInstance() {
		return INSTANCE;
	}

	private static String getPrefix(LogLevel type) {
		cal = Calendar.getInstance();

		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		
		StringBuffer prefix = new StringBuffer()
		.append("[")
		.append(df.format(h))
		.append(":")
		.append(df.format(m))
		.append(":")
		.append(df.format(s))
		.append(" ")
		.append(type.value())
		.append("] ");

		return prefix.toString();
	}

	public static void log(LogLevel type, String msg) {
		StringBuffer buffer = new StringBuffer(getPrefix(type));
		buffer.append(msg);

		if (consoleLogLevel.ordinal() >= type.ordinal())
			System.out.println(buffer.toString());
	}

	public static void debug(String msg) {		
		log(LogLevel.DEBUG, msg);
	}

	public static void info(String msg) {
		log(LogLevel.INFO, msg);
	}

	public static void warn(String msg) {
		log(LogLevel.WARN, msg);
	}

	public static void error(String msg) {
		log(LogLevel.ERROR, msg);
	}

	public enum LogLevel {

		ERROR("ERROR"),
		WARN("WARN"),
		INFO("INFO"),
		DEBUG("DEBUG");
		
		private final String value;

		LogLevel(String v) {
	        value = v;
	    }

	    public String value() {
	        return value;
	    }

	    public static LogLevel fromValue(String v) {
	        for (LogLevel c: LogLevel.values()) {
	            if (c.value.equals(v)) {
	                return c;
	            }
	        }

	        return INFO;
	    }
	}
}



