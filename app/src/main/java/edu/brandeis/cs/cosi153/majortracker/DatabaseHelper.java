package edu.brandeis.cs.cosi153.majortracker;

/**
 * Created by samuel on 11/23/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by samuel on 11/19/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "majortracker";
    public static final int DATABASE_VERSION = 1;
    Context myContext;
    public static final String USERS_TABLE = "users";
    public static final String KEY_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_LASTNAME = "lastname";
    public static final String COL_EMAIL = "email";
    public static final String COL_CIPHERPASS = "cipherpass";
    private static final String DATABASE_CREATE = "create table " + USERS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_NAME + " text, "+
            COL_LASTNAME + " text, "+
            COL_EMAIL + " text, " +
            COL_CIPHERPASS +"text);";
    SQLiteDatabase myDB;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        myDB = db;
        myDB.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTAbles();
        onCreate(myDB);
    }

    public void dropAllTAbles(){
        Cursor cursor = myDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while(!cursor.isAfterLast()){
            String name = cursor.getString(0);
            myDB.execSQL("DROP TABLE " + name+";");
        }
    }
}
