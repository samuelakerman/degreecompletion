package edu.brandeis.cs.cosi153.majortracker;

/**
 * Created by samuel on 11/23/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
    public static final String COL_NAME = "user_name";
    public static final String COL_LASTNAME = "user_lastname";
    public static final String COL_EMAIL = "user_email";
    public static final String COL_CIPHERPASS = "cipherpass";
    private static final String CREATE_TABLE_USERS = "create table " + USERS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_NAME + " text, "+
            COL_LASTNAME + " text, "+
            COL_EMAIL + " text, " +
            COL_CIPHERPASS +" text);";
    //-----------------------------------departments table definition
    public static final String DEPARTMENTS_TABLE = "departments";
    public static final String COL_DEPT_NAME = "dept_name";
    public static final String COL_DEPT_CODE = "dept_code";
    public static final String COL_DEPT_MAJOR = "dept_major";
    public static final String COL_DEPT_MINOR = "dept_minor";

    private static final String CREATE_TABLE_DEPT = "create table " + DEPARTMENTS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_DEPT_NAME + " text, "+
            COL_DEPT_CODE  + " text, "+
            COL_DEPT_MAJOR + " integer, " +
            COL_DEPT_MINOR +" integer);";
    //-----------------------------------classes table definition
    public static final String CLASSES_TABLE = "classes";
    public static final String COL_CLASS_NAME = "class_name";
    public static final String COL_CLASS_CODE = "class_code";

    private static final String CREATE_TABLE_CLASSES = "create table " + CLASSES_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_CLASS_NAME + " text, "+
            COL_CLASS_CODE  + " text);";
    //-----------------------------------classes and majors_adapter table definition
    public static final String CLASSESMAJORS_TABLE = "classes_majors";
    public static final String COL_CLASS_ID= "class_id";
    public static final String COL_DEPT_ID = "major_id";

    private static final String CREATE_TABLE_CLASSESMAJORS = "create table " + CLASSESMAJORS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_CLASS_ID + " integer, "+
            COL_DEPT_ID  + " integer,"+
            " FOREIGN KEY ("+COL_CLASS_ID+") REFERENCES "+CLASSES_TABLE+"("+KEY_ID+")," +
            " FOREIGN KEY ("+COL_DEPT_ID+") REFERENCES "+DEPARTMENTS_TABLE+"("+KEY_ID+"));";

    //-----------------------------------student progress table definition
    public static final String PROGRESS_TABLE = "progress";
    public static final String COL_CLASSP_ID= "class_id";
    public static final String COL_USERP_ID = "user_id";
    private static final String CREATE_TABLE_PROGRESS = "create table " + PROGRESS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_CLASSP_ID + " integer, "+
            COL_USERP_ID  + " integer,"+
            " FOREIGN KEY ("+COL_CLASSP_ID+") REFERENCES "+CLASSES_TABLE+"("+KEY_ID+")," +
            " FOREIGN KEY ("+COL_USERP_ID+") REFERENCES "+USERS_TABLE+"("+KEY_ID+"));";

    //-----------------------------------users and majors_adapter table definition
    public static final String USERSMAJORS_TABLE = "users_majors";
    public static final String COL_USER_ID_M= "user_id";
    public static final String COL_DEPT_ID_M = "major_id";

    private static final String CREATE_TABLE_USERSMAJORS = "create table " + USERSMAJORS_TABLE + " (" + KEY_ID + " integer primary key, " +
            COL_USER_ID_M + " integer, "+
            COL_DEPT_ID_M  + " integer,"+
            " FOREIGN KEY ("+COL_USER_ID_M +") REFERENCES "+CLASSES_TABLE+"("+KEY_ID+")," +
            " FOREIGN KEY ("+COL_DEPT_ID_M+") REFERENCES "+DEPARTMENTS_TABLE+"("+KEY_ID+")" +
            "UNIQUE("+COL_USER_ID_M+", "+COL_DEPT_ID_M+") ON CONFLICT REPLACE);";

    SQLiteDatabase myDB;
    String allMajors = "majors.txt";
    String allMinors = "minors.txt";
    String allSubjects = "subject.json";
    String allClasses = "course.json";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        myDB = db;
        myDB.execSQL(CREATE_TABLE_USERS);
        myDB.execSQL(CREATE_TABLE_DEPT);
        myDB.execSQL(CREATE_TABLE_CLASSES);
        myDB.execSQL(CREATE_TABLE_CLASSESMAJORS);
        myDB.execSQL(CREATE_TABLE_PROGRESS);
        myDB.execSQL(CREATE_TABLE_USERSMAJORS);
        populateMajors();
        populateClasses();
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
        cursor.close();
    }

    private void populateMajors(){
        JSONParser parser = new JSONParser();
        try{
            //allSubjects has the names of all departments is in JSON format
            //allMajors containns the names of all majors_adapter, similarly for allMinors. Plain text
            InputStream isDepartments = myContext.getAssets().open(allSubjects);
            InputStream isMajors = myContext.getAssets().open(allMajors);
            InputStream isMinors = myContext.getAssets().open(allMinors);

            //read all files and store them on strings
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
            //for each entry in the JSON file, create a row in the departments database
            //setting/clearing flags if that department has a major, minor
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
                myDB.insert(DatabaseHelper.DEPARTMENTS_TABLE,null,values);
            }
        }
        catch(FileNotFoundException e){
        }
        catch(ParseException e){
        }
        catch(IOException e){
        }
    }
    private void populateClasses(){
        JSONParser parser = new JSONParser();
        try{
            InputStream isClasses = myContext.getAssets().open(allClasses);

            int sizeClasses = isClasses.available();
            byte[] bufferDept = new byte[sizeClasses];

            isClasses.read(bufferDept);
            isClasses.close();
            String jsonClasses = new String(bufferDept, "UTF-8");

            JSONArray jsonArray = (JSONArray) parser.parse(jsonClasses);
            //One class can give credits for more than one major
            //The classes table contains the name and code for each class
            //CLASSESMAJORS_TABLE links departments/majors_adapter with classes
            for(Object obj : jsonArray){
                JSONObject thisClass = (JSONObject) obj;
                String className = thisClass.get("name").toString();
                String classCode = thisClass.get("code").toString();

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_CLASS_NAME,className);
                values.put(DatabaseHelper.COL_CLASS_CODE,classCode);
                myDB.insert(DatabaseHelper.CLASSES_TABLE,null,values);

                JSONArray majorsArray = (JSONArray) parser.parse(thisClass.get("subjects").toString());
                //after the class is created in its table,
                //we create the association between said class and its department(s)/major(s)
                for(Object o : majorsArray){
                    JSONObject thisMajor = (JSONObject) o;
                    String majorCode = thisMajor.get("id").toString();

                    Cursor c = myDB.rawQuery("SELECT " + KEY_ID+" FROM " +DEPARTMENTS_TABLE+" WHERE "+COL_DEPT_CODE+"=\""+majorCode+"\";",null);
                    c.moveToFirst();

                    if(c.getCount()>0){
                        int majorKey = c.getInt(0);
                        c = myDB.rawQuery("SELECT " + KEY_ID+" FROM " +CLASSES_TABLE+" WHERE "+COL_CLASS_NAME+"=\""+className+"\";",null);
                        c.moveToFirst();
                        int classKey = c.getInt(0);

                        values = new ContentValues();
                        values.put(DatabaseHelper.COL_CLASS_ID,classKey);
                        values.put(DatabaseHelper.COL_DEPT_ID,majorKey);

                        myDB.insert(DatabaseHelper.CLASSESMAJORS_TABLE,null,values);

                        //Log.v(deptName+" "+deptID+" Major: "+isMajor+", Minor: "+isMinor," was added to the db");
                    }
                    c.close();
                    Log.v("Class added: ",className);
                }
            }
        }
        catch(FileNotFoundException e){
        }
        catch(ParseException e){
        }
        catch(IOException e){
        }
    }
}
