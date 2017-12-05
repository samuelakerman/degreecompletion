package edu.brandeis.cs.cosi153.majortracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by melanie on 12/4/2017.
 */

public class AddClasses extends AppCompatActivity {

    private SQLiteDatabase db;
    DatabaseHelper helper = new DatabaseHelper(this);
    Context myContext;
    /*private String classQuery="select a."+ helper.COL_CLASS_NAME+", a."+helper.COL_CLASS_CODE+" from "+helper.CLASSES_TABLE+" as a, "+helper.CLASSESMAJORS_TABLE+" as b, " + helper.DEPARTMENTS_TABLE+
            " as c \n where b."+helper.COL_CLASS_ID+" = a."+helper.KEY_ID+" and b."+helper.COL_DEPT_ID+" = c."+helper.KEY_ID+
            " order by a."+helper.COL_DEPT_NAME;*/
    //private String deptQuery = "select * from "+helper.DEPARTMENTS_TABLE+"as a, "+helper.CLASSES_TABLE;
    private String deptQuery = "select b._id, b.dept_name, count(b.dept_name) from classes as a, departments as b, classes_majors as c where c.class_id=a._id and c.major_id=b._id group by b.dept_name order by b.dept_name";
    private String email;
    private String deptName;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classes_view);
        myContext=this;
        db = helper.getWritableDatabase();
        email = getIntent().getExtras().getString("user_email");

        Log.v("QUERY: ",deptQuery);
        //Toast.makeText(this,classQuery ,Toast.LENGTH_LONG).show();
        final Cursor cursDept = db.rawQuery(deptQuery,null);
        Log.v("DEPARTMENT COUNT: ",cursDept.getCount()+"");

        String[] fromDept = {helper.COL_DEPT_NAME};
        int[] toDept = {R.id.entry_department_name};

        SimpleCursorAdapter adapterDept = new SimpleCursorAdapter(this, R.layout.department_entry, cursDept, fromDept, toDept);
        Spinner deptList = (Spinner) findViewById(R.id.selectDepartmentView);
        deptList.setAdapter(adapterDept);

        Button confButton = (Button) findViewById(R.id.class_confirmation_button);
        confButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner deptList = (Spinner) findViewById(R.id.selectDepartmentView);
                Spinner classList = (Spinner) findViewById(R.id.selectClassView);
                LinearLayout classLayout = (LinearLayout)((LinearLayout) classList.getChildAt(0)).getChildAt(0);


                className = ((TextView) classLayout.getChildAt(0)).getText().toString();
                String userIdQUery = "select * from users where user_email=\""+email+"\"";
                String majorIdQuery = "select * from classes where class_name=\""+className+"\"";
                Log.v("String User",userIdQUery);
                Log.v("String User",majorIdQuery);
                Log.v("useremail: ",email);
                Cursor cursUsId = db.rawQuery(userIdQUery,null);
                Cursor cursDeptId = db.rawQuery(majorIdQuery,null);
                cursUsId.moveToFirst();
                cursDeptId.moveToFirst();

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_USERP_ID,cursUsId.getInt(0));
                values.put(DatabaseHelper.COL_CLASS_ID,cursDeptId.getInt(0));

                db.insert(DatabaseHelper.PROGRESS_TABLE,null,values);

                Intent returnIntent = getIntent();
                setResult(0,returnIntent);
                finish();
            }
        });


        deptList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout dept = (LinearLayout) view;
                TextView deptName = (TextView) dept.getChildAt(0);
                String selectedClass = deptName.getText().toString();

                String classQuery = "select * from "+helper.CLASSES_TABLE+" as a, "+helper.CLASSESMAJORS_TABLE+" as b, " + helper.DEPARTMENTS_TABLE+
                        " as c \n where b."+helper.COL_CLASS_ID+" = a."+helper.KEY_ID+" and b."+helper.COL_DEPT_ID+" = c."+helper.KEY_ID+" and c."+helper.COL_DEPT_NAME+"=\""+selectedClass+"\""+
                        " order by "+helper.COL_DEPT_NAME+", "+helper.COL_CLASS_CODE;

                Cursor curs = db.rawQuery(classQuery,null);
                Log.v("NUM OF CLASSES",curs.getCount()+"");

                String[] from = {curs.getColumnName(1)};
                int[] to = {R.id.className};

                SimpleCursorAdapter adapterClass = new SimpleCursorAdapter(myContext, R.layout.classes_adapter, curs, from, to);
                Spinner classList = (Spinner) findViewById(R.id.selectClassView);
                classList.setAdapter(adapterClass);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}