package com.jackmiddlebrook.dailyselfie.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by jackmiddlebrook on 3/18/15.
 */
public class SelfieSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "selfie.db";
    private static final int DB_Version = 1;

    // Selfie Table functionality
    public static final String SELFIES_TABLE = "SELFIES";
    public static final String COLUMN_SELFIE_ASSET = "ASSET";
    public static final String COLUMN_SELFIE_NAME = "NAME";
    private static String CREATE_SELFIES =
            "CREATE TABLE " + SELFIES_TABLE +" (" + BaseColumns._ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_SELFIE_ASSET + " TEXT," +
            COLUMN_SELFIE_NAME + " TEXT)";


    public SelfieSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_Version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SELFIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
