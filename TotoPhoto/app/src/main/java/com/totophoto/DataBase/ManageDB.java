package com.totophoto.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.totophoto.Image.Favorite;
import com.totophoto.Image.Image;
import com.totophoto.Models.Settings;

import java.util.ArrayList;

/**
 * This class is used for managed the Data-Base
 */
public class ManageDB {
    private myDB mydb;

    public ManageDB(Context context) {
        mydb = new myDB(context);
    }

    /**
     *
     * @param newFavorite The favorite to add
     */
    public void addFavorite(Favorite newFavorite) {
        if (checkIfExist(newFavorite)){
            return;
        }
        else {
            SQLiteDatabase db = mydb.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(myDB.DB_IMG, newFavorite.getName());
            values.put(myDB.DB_LINK, newFavorite.getLink());
            db.insertWithOnConflict(myDB.DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }
    }

    /**
     *
     * @param delFavorite The favorite to delete
     */
    public void deleteFavorite(Favorite delFavorite){
        SQLiteDatabase db = mydb.getWritableDatabase();
        db.delete(myDB.DB_TABLE, myDB.DB_IMG + " = \"" + delFavorite.getName() + "\" AND " + myDB.DB_LINK + " = \"" +delFavorite.getLink() + "\"" ,null);
        db.close();
    }

    /**
     *
     * @return An array who contain the list of the favorites
     */
    public ArrayList<Image> getFavoriteList(){
        ArrayList<Image> favoriteList = new ArrayList<>();
        SQLiteDatabase db = mydb.getReadableDatabase();
        Cursor cursor = db.query(myDB.DB_TABLE, new String[] {myDB.DB_IMG, myDB.DB_LINK}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int indexName = cursor.getColumnIndex(myDB.DB_IMG);
            int indexLink = cursor.getColumnIndex(myDB.DB_LINK);
            favoriteList.add(new Image(cursor.getString(indexName), cursor.getString(indexLink)));
        }
        cursor.close();
        db.close();
        return favoriteList;
    }

    /**
     *
     * @param newFavorite The favorite to check if exite
     * @return true if exist and false if don't exist
     */
    public boolean checkIfExist(Favorite newFavorite) {
        SQLiteDatabase db = mydb.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + myDB.DB_TABLE + " WHERE " + myDB.DB_IMG + " LIKE \"" + newFavorite.getName() + "\" AND " + myDB.DB_LINK + " = \"" + newFavorite.getLink() + "\";", null);
        if (c.getCount() != 0){
            c.close();
            db.close();
            return true;
        }
        c.close();
        db.close();
        return false;
    }

    // Gestion des settings

    /**
     *
     * @return The current language
     */
    private String getLang() {
        SQLiteDatabase db = mydb.getReadableDatabase();
        Cursor cursor = db.query(myDB.DB_TABLE_LANG, new String[] {myDB.DB_LANG}, null, null, null, null, null);
        String Lang = null;
        while (cursor.moveToNext()) {
            int indexLang = cursor.getColumnIndex(myDB.DB_LANG);
            Lang = cursor.getString(indexLang);
        }
        cursor.close();
        db.close();
        return Lang;
    }

    /**
     *
     * @return The current mode
     */
    private String getMode() {
        SQLiteDatabase db = mydb.getReadableDatabase();
        Cursor cursor = db.query(myDB.DB_TABLE_MODE, new String[] {myDB.DB_MODE}, null, null, null, null, null);
        String Mode = null;
        while (cursor.moveToNext()) {
            int indexMode = cursor.getColumnIndex(myDB.DB_MODE);
            Mode = cursor.getString(indexMode);
        }
        cursor.close();
        db.close();
        return Mode;
    }

    /**
     *
     * @return The settings class
     */
    public Settings getSettings() {
        String Lang = this.getLang();
        String Mode = this.getMode();
        Settings settings = new Settings(Lang, Mode);
        return settings;
    }

    /**
     *
     * @param Lang The language selected
     */
    public void setLang(String Lang) {
        SQLiteDatabase db = mydb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(myDB.DB_LANG, Lang);
        db.insertWithOnConflict(myDB.DB_TABLE_LANG, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     *
     * @param Mode The mode selected
     */
    public void setMode(String Mode) {
        SQLiteDatabase db = mydb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(myDB.DB_MODE, Mode);
        db.insertWithOnConflict(myDB.DB_TABLE_MODE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
}
