package edu.brandeis.cs.cosi153.majortracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by sebastian on 12/1/17.
 */

public class ClassesList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classes_list);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra("majorDetails");
        String email = intent.getStringExtra("userEmail");

        // Capture the majors_view's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.majorName);
        textView.setText(message);
        //

        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "select * from "+helper.PROGRESS_TABLE+" as pro, "+helper.USERS_TABLE+" as us, "+helper.CLASSESMAJORS_TABLE+" as cm, "+helper.DEPARTMENTS_TABLE+" as dept, "+
                helper.CLASSES_TABLE+" as cl where pro."+helper.COL_USERP_ID+" = us."+helper.KEY_ID+" and pro."+helper.COL_CLASSP_ID+" = cm."+helper.COL_CLASS_ID+" and cm."+ helper.COL_CLASS_ID+
                " = cl."+helper.KEY_ID+" and cm."+helper.COL_DEPT_ID+" = dept."+helper.KEY_ID+" and us."+helper.COL_EMAIL+" = \""+email+"\" and dept."+helper.COL_DEPT_NAME+" = \""+message+"\"";
        Log.v("QUERY:",query);

        Cursor curs = db.rawQuery(query,null);

        String[] from = {curs.getColumnName(18),curs.getColumnName(17)};
        int[] to = {R.id.detailsClassCode,R.id.detailsClassName};

        SimpleCursorAdapter adapterClass = new SimpleCursorAdapter(this, R.layout.details_adapter, curs, from, to);
        ListView coursesTaken = (ListView) findViewById(R.id.listViewCourses);
        coursesTaken.setAdapter(adapterClass);


    }
}

