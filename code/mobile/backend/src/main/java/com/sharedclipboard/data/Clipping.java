package com.sharedclipboard.data;

/**
 * Created by Girouard23 on 5/24/16.
 */
public class Clipping {

    long dateTime;
    String dateString;
    String text;

    public Clipping(long dateTime, String text) {
        this.dateTime = dateTime;
        this.dateString = Long .toString(dateTime);
        this.text = text;
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
}
