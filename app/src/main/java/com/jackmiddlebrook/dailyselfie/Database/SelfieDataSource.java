package com.jackmiddlebrook.dailyselfie.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.jackmiddlebrook.dailyselfie.SelfieRecord;

import java.util.ArrayList;

/**
 * Created by jackmiddlebrook on 3/18/15.
 *
 */
public class SelfieDataSource {

    private Context mContext;
    private SelfieSQLiteHelper mSelfieSQLiteHelper;

    public SelfieDataSource(Context context) {

        mContext = context;
        mSelfieSQLiteHelper = new SelfieSQLiteHelper(context);

    }

    public ArrayList<SelfieRecord> read() {
        ArrayList<SelfieRecord> selfies = readSelfies();
        return selfies;
    }

    public ArrayList<SelfieRecord> readSelfies() {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                SelfieSQLiteHelper.SELFIES_TABLE,
                new String[] {SelfieSQLiteHelper.COLUMN_SELFIE_NAME, BaseColumns._ID, SelfieSQLiteHelper.COLUMN_SELFIE_ASSET},
                null, // selection
                null, // selection args
                null, // group by
                null, // having
                null); // order
        ArrayList<SelfieRecord> selfies = new ArrayList<SelfieRecord>();
        if (cursor.moveToFirst()) {
           do {
               SelfieRecord selfie = new SelfieRecord(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, SelfieSQLiteHelper.COLUMN_SELFIE_NAME),
                        getStringFromColumnName(cursor, SelfieSQLiteHelper.COLUMN_SELFIE_ASSET));
               selfies.add(selfie);
           } while(cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return selfies;
    }

    private int getIntFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }

    private SQLiteDatabase open() {
        return mSelfieSQLiteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void create(SelfieRecord selfie) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        /// implementation details
        ContentValues selfieValues = new ContentValues();
        selfieValues.put(SelfieSQLiteHelper.COLUMN_SELFIE_NAME, selfie.getSelfieText());
        selfieValues.put(SelfieSQLiteHelper.COLUMN_SELFIE_ASSET, selfie.getSelfieUriAsString());
        long selfieID = database.insert(SelfieSQLiteHelper.SELFIES_TABLE, null, selfieValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

}
