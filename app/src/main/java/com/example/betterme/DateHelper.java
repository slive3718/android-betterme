package com.example.betterme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {
    public static final String SERVER_CREATED_AT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CREATED_AT_FORMAT = "MMM dd, yy  hh:mm a";
    public static final String MM_DD_YYYY = "MMM dd, yyyy";

    public static Date getDateFromString(String dateStr,String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    format, Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }


    public static String getFormattedDateTime(String timeStr) {
        try {
            Calendar cal = Calendar.getInstance();
            // Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    SERVER_CREATED_AT_FORMAT, Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(timeStr);
            cal.setTime(date);
            SimpleDateFormat timeFormat = new SimpleDateFormat(
                    CREATED_AT_FORMAT, Locale.getDefault());
            return timeFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFormattedDate(String timeStr) {
        try {
            Calendar cal = Calendar.getInstance();
            // Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    SERVER_DATE_FORMAT, Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(timeStr);
            cal.setTime(date);
            SimpleDateFormat timeFormat = new SimpleDateFormat(
                    MM_DD_YYYY, Locale.getDefault());
            return timeFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
