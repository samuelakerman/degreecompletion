package edu.brandeis.cs.cosi153.majortracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by melanie on 12/4/2017.
 */

public class AddClasses extends AppCompatActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classes_view);

        DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from "+helper.CLASSES_TABLE+" as a, "+helper.CLASSESMAJORS_TABLE+" as b, " + helper.DEPARTMENTS_TABLE+
                " as c \n where b."+helper.COL_CLASS_ID+" = a."+helper.KEY_ID+" and b."+helper.COL_DEPT_ID+" = c."+helper.KEY_ID+
                " order by "+helper.COL_DEPT_NAME+", "+helper.COL_CLASS_CODE,null);

        String[] from = {helper.COL_DEPT_NAME, helper.COL_CLASS_NAME, helper.COL_CLASS_CODE};
        int[] to = {R.id.classDept, R.id.className, R.id.classCode};


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.classes_adapter, c, from, to);
        ListView classList = (ListView) findViewById(R.id.classView);
        classList.setAdapter(adapter);
    }

}