package com.jackmiddlebrook.dailyselfie.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jackmiddlebrook on 3/18/15.
 */
public class SelfieDataSource {

    private Context mContext;
    private SelfieSQLiteHelper mSelfieSQLiteHelper;

    public SelfieDataSource(Context context) {

        mContext = context;
        mSelfieSQLiteHelper = new SelfieSQLiteHelper(context);
        SQLiteDatabase database = mSelfieSQLiteHelper.getReadableDatabase();
        database.close();
    }

}
