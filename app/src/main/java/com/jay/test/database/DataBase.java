package com.jay.test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    private Cursor cursor;

    private static final String DB_NAME = "DB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "product";

    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String CURRENCY = "currency";

    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table product" +
                        "(_id integer primary key," +
                        "title text," +
                        "image text," +
                        "description text," +
                        "price text," +
                        "currency text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS product");
        onCreate(db);
    }


    //insert data into the database
    public void insertData(String title, String image, String description, String price, String currency) {

        //check if there is such a post in the database
        //use title as a key
        if (!getDataFromColumn(TITLE).contains(title)) {

            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("image", image);
            values.put("description", description);
            values.put("price", price);
            values.put("currency", currency);
            sqLiteDatabase.insert(TABLE_NAME, null, values);
        }
    }


    //taking data from a column in the database
    public ArrayList<String> getDataFromColumn(String column) {

        cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);

        ArrayList<String> dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                dataList.add(cursor.getString(cursor.getColumnIndex(column)));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return dataList;
    }


    //delete data from the database
    //use title as a key
    public void deleteFromDataBase(String title) {

        cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null,
                null, null, null);

        sqLiteDatabase.delete(TABLE_NAME, "title = ?",new String[]{title});
        cursor.close();
    }
}
