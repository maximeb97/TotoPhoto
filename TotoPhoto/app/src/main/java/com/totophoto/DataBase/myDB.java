package com.totophoto.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is used for create the Data-Base
 */
public class myDB extends SQLiteOpenHelper{
    private static final String DB_NAME="TotoPhoto";
    private static final int DB_VER = 1;
    public static final String DB_TABLE="Favorite";
    public static final String DB_IMG="Name";
    public static final String DB_LINK="Link";

    public static final String DB_TABLE_LANG="Language";
    public static final String DB_LANG="Lang";
    public static final String DB_TABLE_MODE="NightMode";
    public static final String DB_MODE = "Mode";

    public myDB(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL);", DB_TABLE, DB_IMG, DB_LINK);
        db.execSQL(query);
        query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", DB_TABLE_LANG, DB_LANG);
        db.execSQL(query);
        query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", DB_TABLE_MODE, DB_MODE);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = String.format("DELETE TABLE IF EXIST %s", DB_TABLE);
        db.execSQL(query);
        query = String.format("DELETE TABLE IF EXIST %s", DB_TABLE_LANG);
        db.execSQL(query);
        query = String.format("DELETE TABLE IF EXIST %s", DB_TABLE_MODE);
        db.execSQL(query);
        onCreate(db);
    }
}
