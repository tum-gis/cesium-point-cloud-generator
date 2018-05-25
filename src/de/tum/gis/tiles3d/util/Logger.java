/*
 * Cesium Point Cloud Generator
 * 
 * Copyright 2017 - 2018
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The Cesium Point Cloud Generator is developed at Chair of Geoinformatics,
 * Technical University of Munich, Germany.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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



