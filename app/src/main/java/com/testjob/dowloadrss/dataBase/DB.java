package com.testjob.dowloadrss.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.testjob.dowloadrss.dataBase.model.RssItem;

import static com.testjob.dowloadrss.dataBase.DB.DBHelper.COLUMN_ID;
import static com.testjob.dowloadrss.dataBase.DB.DBHelper.DB_TABLE;


public class DB {


    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public void addRec(RssItem rssItem) {
        ContentValues cv = new ContentValues();
        cv = new ContentValues();
        cv.put(DB.DBHelper.COLUMN_DESCRIPTION, rssItem.getDescription());
        cv.put(DB.DBHelper.COLUMN_LINK, rssItem.getLink());
        cv.put(DB.DBHelper.COLUMN_PUBDATE, rssItem.getPubDate());
        cv.put(DB.DBHelper.COLUMN_TITLE, rssItem.getTitle());

        mDB.insert(DB_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    public void delAllRec() {
        mDB.delete(DB_TABLE, null, null);
    }


    public static class DBHelper extends SQLiteOpenHelper {

        public static final String LOG_TAG = "dbHelper";

        public static final String DB_NAME = "myDB";
        private static final int DB_VERSION = 2;
        public static final String DB_TABLE = "rsstable";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PUBDATE = "pubDate";
        public static final String COLUMN_DESCRIPTION = "description";


        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table "+ DB_TABLE +" ("
                    + COLUMN_ID +" integer primary key autoincrement,"
                    + COLUMN_LINK +" text,"
                    + COLUMN_DESCRIPTION +" text,"
                    + COLUMN_PUBDATE+" text,"
                    + COLUMN_TITLE+" text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
                onCreate(db);
            }
        }
    }
}
