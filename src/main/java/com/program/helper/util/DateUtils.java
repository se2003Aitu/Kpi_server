package com.program.helper.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private static final TimeZone KAZAKHSTAN_TIMEZONE = TimeZone.getTimeZone("Asia/Almaty");

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance(KAZAKHSTAN_TIMEZONE);
        return calendar.getTime();
    }
}
