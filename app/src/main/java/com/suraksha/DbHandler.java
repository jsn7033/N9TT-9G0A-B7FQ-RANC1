package com.suraksha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jsn on 19/7/16.
 */
public class DbHandler {

    private static DbHandler mDbHandler;

    private DbHandler(Context mContext){
        this.mContext=mContext;
    }

    public static DbHandler getInstance(Context mContext){
        if(mDbHandler == null){
            mDbHandler = new DbHandler(mContext);
        }

        return mDbHandler;
    }

    public static final String VALUE_TABLE = "value_table";
    public static final String VALUE = "value";

    public static final String CREATE_VALUE_TABLE = "CREATE TABLE " + VALUE_TABLE + " ("
            + VALUE + " Text);";

    private DbHelper mDbHelper;
    private Context mContext;

    public SQLiteDatabase getDbObj(int value){

        mDbHelper = new DbHelper(mContext);
        return value == 1 ? mDbHelper.getWritableDatabase() : mDbHelper.getReadableDatabase();
    }

    private class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, "db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_VALUE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

}
