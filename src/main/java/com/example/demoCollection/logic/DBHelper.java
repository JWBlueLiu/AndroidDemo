package com.example.demoCollection.logic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private String tag = getClass().getSimpleName();

    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "DemoCollection.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(tag, "onCreate.....");
        //创建一个数据库
        db.execSQL("CREATE TABLE person (_id integer primary key autoincrement, name varchar(30), male integer NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(tag, "onUpgrade*******");
        //增加一列
    }
}