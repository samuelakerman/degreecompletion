package edu.brandeis.cs.cosi153.majortracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by melanie on 12/3/2017.
 */

public class AddMajor extends AppCompatActivity {

    private SQLiteDatabase db;
    private Context myContext;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.majors_adapter);
        myContext = this;
        email = getIntent().getExtras().getString("user_email");

        DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();

        Cursor cursDept = db.rawQuery("select * from "+helper.DEPARTMENTS_TABLE+" where "+helper.COL_DEPT_MAJOR+" = "+1+" order by "+helper.COL_DEPT_NAME,null);
        Log.v("Number of depts",cursDept.getCount()+"");
        Log.v("QUERY: ", "select * from "+helper.DEPARTMENTS_TABLE+" where "+helper.COL_DEPT_MAJOR+" = "+1+" order by "+helper.COL_DEPT_NAME);
        String[] fromDept = {helper.COL_DEPT_NAME};
        int[] toDept = {R.id.entry_department_name};

        SimpleCursorAdapter adapterDept = new SimpleCursorAdapter(this, R.layout.department_entry, cursDept, fromDept, toDept);
        Spinner majorsList = (Spinner) findViewById(R.id.selectMajorView);
        majorsList.setAdapter(adapterDept);

        Button confButton = (Button) findViewById(R.id.major_confirmation_button);
        confButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner majorsList = (Spinner) findViewById(R.id.selectMajorView);
                AppCompatTextView majorText = (AppCompatTextView)((LinearLayout) majorsList.getChildAt(0)).getChildAt(0);
                String majorName = majorText.getText().toString();
                Log.v("Major selected: ",majorName);

                String userIdQUery = "select * from users where user_email=\""+email+"\"";
                String majorIdQuery = "select * from departments where dept_name=\""+majorName+"\"";
                Log.v("String User",userIdQUery);
                Log.v("String Major",majorIdQuery);
                Log.v("useremail: ",email);

                Cursor cursUsId = db.rawQuery(userIdQUery,null);
                Cursor cursDeptId = db.rawQuery(majorIdQuery,null);

                cursUsId.moveToFirst();
                cursDeptId.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_USER_ID_M,cursUsId.getInt(0));
                values.put(DatabaseHelper.COL_DEPT_ID_M,cursDeptId.getInt(0));

                db.insert(DatabaseHelper.USERSMAJORS_TABLE,null,values);

                Intent returnIntent = getIntent();
                returnIntent.putExtra("majorTitle",majorName);
                setResult(1,returnIntent);
                finish();

            }
        });

    }
}
