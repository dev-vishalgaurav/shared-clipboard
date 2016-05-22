package com.sharedclipboard.storage.db.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.sharedclipboard.storage.db.ClippingContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal Gaurav
 */
public class Clipping {

    private long id;
    private String clipping;
    private int clippingType;
    private long date;

    /**
     * initiate id as -1
     * @param clipping
     * @param clippingType
     * @param date
     */
    public Clipping(String clipping, int clippingType, long date) {
        this.id = -1l;
        this.clipping = clipping;
        this.clippingType = clippingType;
        this.date = date;
    }

    public Clipping(long id, String clipping, int clippingType, long date) {
        this.id = id;
        this.clipping = clipping;
        this.clippingType = clippingType;
        this.date = date;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClipping() {
        return clipping;
    }

    public void setClipping(String clipping) {
        this.clipping = clipping;
    }

    public int getClippingType() {
        return clippingType;
    }

    public void setClippingType(int clippingType) {
        this.clippingType = clippingType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Clipping(Cursor cursor) throws SQLiteException{
        this.id = cursor.getLong(cursor.getColumnIndex(ClippingContract.ClippingsTable._ID));
        this.clipping = cursor.getString(cursor.getColumnIndex(ClippingContract.ClippingsTable.COLUMN_CLIPPING));
        this.clippingType = cursor.getInt(cursor.getColumnIndex(ClippingContract.ClippingsTable.COLUMN_CLIPPING_TYPE));
        this.date   = cursor.getLong(cursor.getColumnIndex(ClippingContract.ClippingsTable.COLUMN_DATE_TIME));
    }


    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(ClippingContract.ClippingsTable.COLUMN_CLIPPING,clipping);
        values.put(ClippingContract.ClippingsTable.COLUMN_CLIPPING_TYPE,clippingType);
        values.put(ClippingContract.ClippingsTable.COLUMN_DATE_TIME,date);
        return values;
    }

    /**
     * it needs a cursor which has query results for a set of clippings
     * @param cursor
     * @return
     */
    public static List<Clipping> getAllClippings(Cursor cursor) {
        List<Clipping> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(new Clipping(cursor));
        }
        return result;
    }
}
