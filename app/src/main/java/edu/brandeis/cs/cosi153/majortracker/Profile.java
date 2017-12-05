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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        email = getIntent().getExtras().getString("user_email");

        majorAdapter = new ProfileAdapter(this, data);
        listView = (ListView) findViewById(R.id.myListView);

        TextView nameView = (TextView) findViewById(R.id.textViewUserName);
        Cursor c = db.rawQuery("select * from "+dbHelper.USERS_TABLE+" where "+dbHelper.COL_EMAIL+" = "+"\""+email+"\"",null);
        c.moveToFirst();
        nameView.setText(c.getString(1));
        c.close();

        Cursor majors = db.rawQuery("select * from departments as d, users as u, users_majors as m " +
                "where m.major_id=d._id and m.user_id=u._id ",null);
        Log.v("Total majors for user: ", majors.getCount()+"");
        majors.moveToFirst();
        while(!majors.isAfterLast()){
            Cursor classesTaken = db.rawQuery("select count(*) from classes_majors as c, classes as cl, departments as d, progress as p, users as us\n" +
                    "where d.dept_name=\"" + majors.getString(1)+"\" and d._id =c.major_id and c.class_id = cl._id and cl._id=p.class_id and us.user_email=\""+email+"\"",null);
            classesTaken.moveToFirst();
            Log.v("Number of "+majors.getString(1)+" classes taken: ",classesTaken.getString(0)+"");
            int totalNoClassesMajorTaken = Integer.valueOf(classesTaken.getString(0));

            ObjectEntry entry = new ObjectEntry(majors.getString(1));
            majorAdapter.progress.put(majors.getString(1),Integer.valueOf(classesTaken.getString(0)));

            data.add(entry);
            majorAdapter.notifyDataSetChanged();
            majors.moveToNext();
        }
        majors.close();


        final Button addMajor = (Button) findViewById(R.id.buttonAddMajor);
        final Button addClass = (Button) findViewById(R.id.buttonAddClass);

        listView.setAdapter(majorAdapter);

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
        refreshBars();
        Log.v("Request Code",requestCode+"");
        if (requestCode == 1) {

        }
        else if(requestCode==0){
            Log.v("DEPARTMENT",intent.getStringExtra("department")+"");
            Log.v("CLASS",intent.getStringExtra("newClass")+"");
        }

    }
    private void refreshBars(){
        data.clear();
        majorAdapter.progress.clear();
        Cursor majors = db.rawQuery("select * from departments as d, users as u, users_majors as m " +
                "where m.major_id=d._id and m.user_id=u._id ",null);
        Log.v("Total majors for user: ", majors.getCount()+"");
        majors.moveToFirst();
        while(!majors.isAfterLast()){
            Cursor classesTaken = db.rawQuery("select count(*) from classes_majors as c, classes as cl, departments as d, progress as p, users as us\n" +
                    "where d.dept_name=\"" + majors.getString(1)+"\" and d._id =c.major_id and c.class_id = cl._id and cl._id=p.class_id and us.user_email=\""+email+"\"",null);
            classesTaken.moveToFirst();
            Log.v("Number of "+majors.getString(1)+" classes taken: ",classesTaken.getString(0)+"");
            int totalNoClassesMajorTaken = Integer.valueOf(classesTaken.getString(0));
            Log.v("CLASSES TAKEN #",totalNoClassesMajorTaken+"");

            ObjectEntry entry = new ObjectEntry(majors.getString(1));

            majorAdapter.progress.put(majors.getString(1),Integer.valueOf(classesTaken.getString(0)));
            classesTaken.close();

            data.add(entry);
            majorAdapter.notifyDataSetChanged();
            majors.moveToNext();
        }
        majors.close();

        }



    /**
     * Called when the user taps the Send button
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

}