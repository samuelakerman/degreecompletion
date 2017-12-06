package edu.brandeis.cs.cosi153.majortracker;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sebastian on 12/1/17.
 */

public class Profile extends AppCompatActivity {

    private ArrayList<ObjectEntry> data = new ArrayList<>();
    private ListView listView;
    private ProfileAdapter majorAdapter;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String email;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        email = getIntent().getExtras().getString("user_email");
        boolean newUser = getIntent().getExtras().getBoolean("new_user");

        majorAdapter = new ProfileAdapter(this, data);
        listView = (ListView) findViewById(R.id.myListView);

        helloName();
        showBars();

        final Button addMajor = (Button) findViewById(R.id.buttonAddMajor);
        final Button addClass = (Button) findViewById(R.id.buttonAddClass);

        addMajor.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Profile.this,AddMajor.class);
                intent.putExtra("user_email",email.toString());
                startActivityForResult(intent,1);

            }
        });
        addClass.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //sendMessage(v);
                Intent intent = new Intent(Profile.this,AddClasses.class);
                intent.putExtra("user_email",email.toString());
                startActivityForResult(intent,0);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        showBars();
        Log.v("Request Code",requestCode+"");
        if (requestCode == 1) {

        }
        else if(requestCode==0){
            Log.v("DEPARTMENT",intent.getStringExtra("department")+"");
            Log.v("CLASS",intent.getStringExtra("newClass")+"");
        }

    }
    private void showBars(){
        majorAdapter.clear();
        data.clear();
        majorAdapter = new ProfileAdapter(this, data);
        listView = (ListView) findViewById(R.id.myListView);
        listView.setAdapter(majorAdapter);
        data.clear();
        majorAdapter.progress.clear();

        Cursor majors = db.rawQuery("select * from departments as d, users as u, users_majors as m " +
                "where m.major_id=d._id and m.user_id=u._id and u.user_email=\""+email+"\"",null);
        Log.v("Total majors for user: ", majors.getCount()+"");
        majors.moveToFirst();
        while(!majors.isAfterLast()){
                       String classesTaken = "select count(*) from "+dbHelper.PROGRESS_TABLE+" as pro, "+dbHelper.USERS_TABLE+" as us, "+dbHelper.CLASSESMAJORS_TABLE+" as cm, "+dbHelper.DEPARTMENTS_TABLE+" as dept, "+
                    dbHelper.CLASSES_TABLE+" as cl where pro."+dbHelper.COL_USERP_ID+" = us."+dbHelper.KEY_ID+" and pro."+dbHelper.COL_CLASSP_ID+" = cm."+dbHelper.COL_CLASS_ID+" and cm."+ dbHelper.COL_CLASS_ID+
                    " = cl."+dbHelper.KEY_ID+" and cm."+dbHelper.COL_DEPT_ID+" = dept."+dbHelper.KEY_ID+" and us."+dbHelper.COL_EMAIL+" = \""+email+"\" and dept."+dbHelper.COL_DEPT_NAME+" = \""+majors.getString(1)+"\"";
            Cursor classesMajors = db.rawQuery(classesTaken,null);
            Log.v("QUERY:",classesTaken);
            classesMajors.moveToFirst();
            Log.v("Number of "+majors.getString(1)+" classes taken: ",classesMajors.getString(0)+"");
            int totalNoClassesMajorTaken = Integer.valueOf(classesMajors.getString(0));
            Log.v("CLASSES TAKEN #",totalNoClassesMajorTaken+"");

            ObjectEntry entry = new ObjectEntry(majors.getString(1));
            data.add(entry);
            entry.progress=totalNoClassesMajorTaken;
            majorAdapter.progress.put(majors.getString(1),totalNoClassesMajorTaken);
            classesMajors.close();


            majorAdapter.notifyDataSetChanged();
            majors.moveToNext();
        }
        majors.close();

        }

    /**
     * Called when the user taps the Details button
     */
    public void sendMessage(View view) {

        int position = listView.getPositionForView((View) view.getParent());
        ObjectEntry e = (ObjectEntry) listView.getItemAtPosition(position);

        Intent intent = new Intent(Profile.this, ClassesList.class);

        String message = e.getMajorName();

        intent.putExtra("majorDetails",message);
        intent.putExtra("userEmail",email);
        Log.v("Sending message: ","Opening details for "+message);
        startActivity(intent);
    }

    /**
    /* Called when user taps the Delete button
    */
    public void deleteItem(View view) {

        int position = listView.getPositionForView((View) view.getParent());
        ObjectEntry e = (ObjectEntry) listView.getItemAtPosition(position);
        data.remove(e);

        String message = e.getMajorName();

        Cursor c = db.rawQuery("select * from "+dbHelper.DEPARTMENTS_TABLE+" where "+dbHelper.COL_DEPT_NAME+" =\""+message+"\"",null);
        c.moveToFirst();
        String deptId = c.getString(0);
        Log.v("DEPARTMENT TO DELETE: ",deptId);

        SQLiteDatabase dbChange = dbHelper.getWritableDatabase();
        dbChange.execSQL("delete from "+dbHelper.USERSMAJORS_TABLE+" where "+dbHelper.COL_USER_ID_M+" = "+userId+" and "+dbHelper.COL_DEPT_ID_M+" = "+deptId);

        majorAdapter.notifyDataSetChanged();
        db = dbHelper.getReadableDatabase();

    }

    public void helloName(){
        TextView nameView = (TextView) findViewById(R.id.textViewUserName);
        Cursor c = db.rawQuery("select * from "+dbHelper.USERS_TABLE+" where "+dbHelper.COL_EMAIL+" = "+"\""+email+"\"",null);
        c.moveToFirst();
        userId = c.getString(0);
        nameView.setText(c.getString(1));
        c.close();
    }
}