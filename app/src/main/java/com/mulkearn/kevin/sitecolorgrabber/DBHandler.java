package com.mulkearn.kevin.sitecolorgrabber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1; //Version needs to be update if DB is updated
    private static final String DATABASE_NAME = "colors.db"; //File name on device
    public static final String TABLE_COLORS = "colors";
    public static final String COLUMN_ID = "_id"; //Create constant per column
    public static final String COLUMN_COLORNAME = "_colorname"; //Create constant per column

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Is called the very first time the app runs (i.e. create new table)
        String query = "CREATE TABLE " + TABLE_COLORS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_COLORNAME + " TEXT " +
                ");";
        db.execSQL(query);//execute SQL query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //IS called if table is ever updated
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLORS);
        onCreate(db);
    }

    //Add a new row to the database
    public void addColor(Colors color){
        SQLiteDatabase db = getWritableDatabase(); //Get DB connection
        ContentValues values = new ContentValues();
        values.put(COLUMN_COLORNAME, color.get_colorname());

        db.insert(TABLE_COLORS, null, values); //Inserts new row in table
        db.execSQL("DELETE FROM " + TABLE_COLORS + " WHERE " + COLUMN_ID + " NOT IN " +
                "(SELECT " + COLUMN_ID + " FROM " + TABLE_COLORS + " ORDER BY " +
                COLUMN_ID + " DESC LIMIT 40);"); //Limit number of color to 40

        db.close();
    }

    //Delete color from database
    public void deleteColor(String colorName){
        SQLiteDatabase db = getWritableDatabase(); //Get DB connection
        db.execSQL("DELETE FROM " + TABLE_COLORS + " WHERE " + COLUMN_COLORNAME + "=\"" + colorName + "\";");
    }

    //clear database
    public void clearColors(){
        SQLiteDatabase db = getWritableDatabase(); //Get DB connection
        db.execSQL("DELETE FROM " + TABLE_COLORS + ";");
    }

    //Print out database as a string
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase(); //Get DB connection
        //String query = "SELECT * FROM " + TABLE_COLORS;// + " WHERE 1";
        String query = "SELECT * FROM " + TABLE_COLORS + " ORDER BY " + COLUMN_ID + " DESC";// Most recent at the top

        Cursor c = db.rawQuery(query, null); //Cursor point to a location in your results
        c.moveToFirst(); //Move to first row of results

        //Loop through database and add to string
        while(!c.isAfterLast()){
            dbString += c.getString(c.getColumnIndex("_colorname"));
            dbString += ",";
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

}
