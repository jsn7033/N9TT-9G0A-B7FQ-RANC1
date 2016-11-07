package com.suraksha;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jsn on 19/7/16.
 */
public class DBDao {

    private static DBDao mDBDao;

    private DBDao(){};

    public static DBDao getInstance() {

        if(mDBDao == null){
            mDBDao = new DBDao();
        }

        return mDBDao;
    }

    /**
     * Insert values in table
     * @param value
     * @param db
     */
    public void insertData(float value, SQLiteDatabase db){

        ContentValues cv = new ContentValues();
        cv.put(DbHandler.VALUE, value);

        try {
            db.insert(DbHandler.VALUE_TABLE, null,cv);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     *
     * @param db
     * @return max value in db
     */
    public double getMaxValue(SQLiteDatabase db){

        double maxValue = 0;
        String sqlQuery = "Select max(CAST(value AS DOUBLE)) from " + DbHandler.VALUE_TABLE ;

        Cursor mCursor =null;

        try {
            mCursor = db.rawQuery(sqlQuery, null);

            if(mCursor != null && mCursor.moveToFirst()){
                do{
                    maxValue = mCursor.getInt(0);
                }while (mCursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null && !mCursor.isClosed()){
                mCursor.close();
            }
            db.close();
        }



        return maxValue;
    }
}
