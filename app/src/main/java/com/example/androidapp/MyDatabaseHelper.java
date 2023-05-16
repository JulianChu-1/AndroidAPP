package com.example.androidapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_API_TABLE = "CREATE TABLE API_TABLE (API_KEY TEXT PRIMARY KEY)";
        db.execSQL(SQL_CREATE_API_TABLE);
        String SQL_CREATE_Dialogue_TABLE = "CREATE TABLE Dialogue_TABLE (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Message TEXT, _TYPE TEXT,_DATETIME TEXT, API_KEY TEXT)";
        db.execSQL(SQL_CREATE_Dialogue_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在这里添加数据库升级的代码
    }
}
