package com.totophoto.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.totophoto.Image.Favorite;
import com.totophoto.Image.Image;
import java.util.ArrayList;

/**
 * Created by Thomas on 05/02/2018.
 */

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
}
