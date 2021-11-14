package com.antoniorg.utils;

import java.util.concurrent.TimeUnit;

public abstract class GlobalUtils {

	public static String milisecondsToTimeUnit(long miliseconds) {
	    String hms = String.format("%02dmin y %02dseg",
	            TimeUnit.MILLISECONDS.toMinutes(miliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(miliseconds)),
	            TimeUnit.MILLISECONDS.toSeconds(miliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(miliseconds)));
	    
	    return hms;
	}
	
	public static String secondsToTimeUnit(long seconds) {
		return milisecondsToTimeUnit(seconds*1000);
	}
}
