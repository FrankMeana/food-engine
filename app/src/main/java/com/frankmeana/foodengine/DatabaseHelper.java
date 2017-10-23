package com.frankmeana.foodengine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The Database class must extend SQLiteOpenHelper in order to inherit its functionality
 * In doing so, it must implement methods onCreate and onUpgrade, also you must create
 * a constructor calling the specified functionality from its superclass
 */
public class DatabaseHelper extends SQLiteOpenHelper
{

    // constants that contain important info about our Database
    private static final String DATABASE_NAME = "Products.db";
    private static final String TABLE_NAME = "Products";
    private static final String PRIMARY_KEY = "ID";
    private static final String COL_1 = "PRODUCT_NUMBER";
    private static final String COL_2 = "BRAND_NAME";
    private static final String COL_3 = "PRODUCT_NAME";
    private static final String COL_4 = "NAMELESS_COLUMN";
    private static final String COL_5 = "COUNTRY";
    private static final String COL_6 = "GRAMS";
    private static final String COL_7 = "ML";
    private static final String COL_8 = "PCS";
    private static final String COL_9 = "DOLLAR_PER_KG";
    private static final String COL_10 = "DOLLARS";
    private static final String COL_11 = "BUSINESS_NAME";
    private static final String COL_12 = "LOCATION_1";
    private static final String COL_13 = "HOURS_MON";
    private static final String COL_14 = "HOURS_TUES";
    private static final String COL_15 = "HOURS_WED";
    private static final String COL_16 = "HOURS_THU";
    private static final String COL_17 = "HOURS_FRI";
    private static final String COL_18 = "HOURS_SAT";
    private static final String COL_19 = "HOURS_SUN";
    private static final String COL_20 = "PHONE";


    /**
     * For simplicity Construct a DatabaseHelper that only takes a Context
     * ***IMPORTANT : Whenever this constructor is called, my database will be created ***
     *
     * @param context
     */
    public DatabaseHelper(Context context)
    {
        // super(context, name, factory, version);
        super(context, DATABASE_NAME, null, 1); // increment version number when you wanna read a new file
    }

    /**
     * @param db Possible change:
     *           If the datatype for the columns does not matter I can create a method that accepts
     *           an SQLiteDatabase and an array that returns a string and finishes the rest of the query
     *           from column 2 forward (column after primary key)
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.e("ONCREATE", "ONCREATE SAYS HELLO");

        // create a my table
        db.execSQL("create table " + TABLE_NAME + "(" + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_1 + " TEXT," +
                COL_2 + " TEXT," +
                COL_3 + " TEXT," +
                COL_4 + " TEXT," +
                COL_5 + " TEXT," +
                COL_6 + " TEXT," +
                COL_7 + " TEXT," +
                COL_8 + " TEXT," +
                COL_9 + " TEXT," +
                COL_10 + " TEXT," +
                COL_11 + " TEXT," +
                COL_12 + " TEXT," +
                COL_13 + " TEXT," +
                COL_14 + " TEXT," +
                COL_15 + " TEXT," +
                COL_16 + " TEXT," +
                COL_17 + " TEXT," +
                COL_18 + " TEXT," +
                COL_19 + " TEXT," +
                COL_20 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.e("ON_UPGRADE", "I OCCURRED");
    }


    /**
     * Method to insert data (fill a row with data, column by column)
     * array columns will be the array in each iteration of the file reading
     * <p>
     * Possible change #1:
     * (This code could instead accept a Product object and put values would look like
     * contentValues.put(product.getNumber());
     * contentValues.put(product.getBusinessName());
     * etc
     */
    public boolean insertData(String[] columns)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // first argument is the column, 2nd argument is the value
        contentValues.put(COL_1, columns[0]);
        contentValues.put(COL_2, columns[1]);
        contentValues.put(COL_3, columns[2]);
        contentValues.put(COL_4, columns[3]);
        contentValues.put(COL_5, columns[4]);
        contentValues.put(COL_6, columns[5]);
        contentValues.put(COL_7, columns[6]);
        contentValues.put(COL_8, columns[7]);
        contentValues.put(COL_9, columns[8]);
        contentValues.put(COL_10, columns[9]);
        contentValues.put(COL_11, columns[10]);
        contentValues.put(COL_12, columns[11]);
        contentValues.put(COL_13, columns[12]);
        contentValues.put(COL_14, columns[13]);
        contentValues.put(COL_15, columns[14]);
        contentValues.put(COL_16, columns[15]);
        contentValues.put(COL_17, columns[16]);
        contentValues.put(COL_18, columns[17]);
        contentValues.put(COL_19, columns[18]);
        contentValues.put(COL_20, columns[19]);

        // insert into database
        long result = db.insert(TABLE_NAME, null, contentValues);

        // method insert returns -1 if the there was an error inserting (so this will return false in that case)
        return (result != -1);
    }

    /**
     * Query Database
     */
    public Cursor select(String userSearch)
    {
        SQLiteDatabase db = getWritableDatabase();

        // returns a Cursor object containing the Result Set table produced by a Query
        return db.rawQuery(String.format("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s LIKE '%%%s%%'",
                COL_1, COL_3, COL_5, COL_2, COL_11, TABLE_NAME, COL_3, userSearch.trim()), null);
    }

    // --- UNDER SUPERVISION ---
    // truncate a table (delete all rows and reset autoincrement)
    public void resetTable()
    {
        SQLiteDatabase db = getWritableDatabase();

        // delete all rows from table
        db.execSQL(String.format("DELETE FROM %s", TABLE_NAME));

        // reset primary key's autoincrement
        db.execSQL(String.format("DELETE FROM SQLITE_SEQUENCE WHERE name='%s'", TABLE_NAME));
    }
}
