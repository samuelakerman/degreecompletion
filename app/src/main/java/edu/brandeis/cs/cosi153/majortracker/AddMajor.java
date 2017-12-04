package edu.brandeis.cs.cosi153.majortracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by melanie on 12/3/2017.
 */

public class AddMajor extends AppCompatActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.majors_view);

        DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from "+helper.DEPARTMENTS_TABLE+" where "+helper.COL_DEPT_MAJOR+" = "+1,null);

        String[] from = {helper.COL_DEPT_NAME, helper.COL_DEPT_CODE};
        int[] to = {R.id.deptName, R.id.deptCode};


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.majors_adapter, c, from, to);
        ListView expenseList = (ListView) findViewById(R.id.majorView);
        expenseList.setAdapter(adapter);
    }
}
