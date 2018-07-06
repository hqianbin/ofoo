package com.android.ofoo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

	public static String parse(long timeStamp, String format){
		String dateTime = new SimpleDateFormat(format).format(new Date(timeStamp));
		return dateTime;
	}
    public static String parse(Date date, String format) {
        String dateTime = new SimpleDateFormat(format).format(date);
        return dateTime;
    }

}
