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
}
