package edu.brandeis.cs.cosi153.majortracker;

/**
 * Created by samuel on 11/23/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by samuel on 11/19/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "majortracker";
    public static final int DATABASE_VERSION = 1;
    Context myContext;
    //-----------------------------------users table definition
    public static final String USERS_TABLE = "users";
    public static final String KEY_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_LASTNAME = "lastname";
    public static final String COL_EMAIL = "email";
    public static final String COL_CIPHERPASS = "cipherpass";
    private static final String CREATE_TABLE_USERS = "create table " + USERS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_NAME + " text, "+
            COL_LASTNAME + " text, "+
            COL_EMAIL + " text, " +
            COL_CIPHERPASS +" text);";
    //-----------------------------------departments table definition
    public static final String DEPARTMENTS_TABLE = "departments";
    public static final String COL_DEPT_NAME = "name";
    public static final String COL_DEPT_CODE = "code";
    public static final String COL_DEPT_MAJOR = "major";
    public static final String COL_DEPT_MINOR = "minor";

    private static final String CREATE_TABLE_DEPT = "create table " + DEPARTMENTS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_DEPT_NAME + " text, "+
            COL_DEPT_CODE  + " text, "+
            COL_DEPT_MAJOR + " integer, " +
            COL_DEPT_MINOR +" integer);";

    SQLiteDatabase myDB;
    String allMajors = "majors.txt";
    String allMinors = "minors.txt";
    String allSubjects = "subject.json";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        myDB = db;
        myDB.execSQL(CREATE_TABLE_USERS);
        myDB.execSQL(CREATE_TABLE_DEPT);

        JSONParser parser = new JSONParser();
        try{
            InputStream isDepartments = myContext.getAssets().open(allSubjects);
            InputStream isMajors = myContext.getAssets().open(allMajors);
            InputStream isMinors = myContext.getAssets().open(allMinors);

            int sizeDepartments = isDepartments.available();
            int sizeMajors = isMajors.available();
            int sizeMinors = isMinors.available();
            byte[] bufferDept = new byte[sizeDepartments];
            byte[] bufferMaj = new byte[sizeMajors];
            byte[] bufferMin = new byte[sizeMinors];

            isDepartments.read(bufferDept);
            isDepartments.close();
            String jsonDept = new String(bufferDept, "UTF-8");
            isMajors.read(bufferMaj);
            isMajors.close();
            String majorsString = new String(bufferMaj, "UTF-8");
            isMinors.read(bufferMin);
            isMinors.close();
            String minorsString = new String(bufferMin, "UTF-8");

            JSONArray jsonArray = (JSONArray) parser.parse(jsonDept);

            for(Object obj : jsonArray){
                JSONObject department = (JSONObject) obj;
                String deptName = department.get("name").toString();
                String deptID = department.get("id").toString();
                Boolean isMajor = majorsString.contains(deptName);
                Boolean isMinor = minorsString.contains(deptName);

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_DEPT_NAME,deptName);
                values.put(DatabaseHelper.COL_DEPT_CODE,deptID);
                values.put(DatabaseHelper.COL_DEPT_MAJOR,isMajor ? 1:0);
                values.put(DatabaseHelper.COL_DEPT_MINOR,isMinor ? 1:0);
                db.insert(DatabaseHelper.DEPARTMENTS_TABLE,null,values);

                Log.v(deptName+" "+deptID+" Major: "+isMajor+", Minor: "+isMinor," was added to the db");
            }
        }
        catch(FileNotFoundException e){
        }
        catch(ParseException e){
        }
        catch(IOException e){
        }
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
