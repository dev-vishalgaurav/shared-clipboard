package com.sharedclipboard.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Girouard23 on 5/24/16.
 */
public class Clipping implements Comparable<Clipping> {

    long dateTime;
    String dateString;
    String text;

    public Clipping(long dateTime, String text) {
        this.dateTime = dateTime;
        this.dateString = convertDateString(dateTime);
        this.text = text;
    }

    public String convertDateString(long time) {
        long millis = time * 1000;
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/y K:m a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public String getDateString() {
        return this.dateString;
    }

    public long getDateTime() {
        return this.dateTime;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public int compareTo(Clipping clipping) {
        long compareTime = clipping.getDateTime();
        /* For Ascending order*/
        return (int) (compareTime - this.getDateTime());

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
