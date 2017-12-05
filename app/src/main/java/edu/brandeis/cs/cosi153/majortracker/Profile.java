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

        majorAdapter = new ProfileAdapter(this, data);
        listView = (ListView) findViewById(R.id.myListView);

        TextView nameView = (TextView) findViewById(R.id.textViewUserName);
        Cursor c = db.rawQuery("select * from "+dbHelper.USERS_TABLE+" where "+dbHelper.COL_EMAIL+" = "+"\""+email+"\"",null);
        c.moveToFirst();
        userId = c.getString(0);
        nameView.setText(c.getString(1));
        c.close();

        Cursor majors = db.rawQuery("select * from departments as d, users as u, users_majors as m " +
                "where m.major_id=d._id and m.user_id=u._id ",null);
        Log.v("Total majors for user: ", majors.getCount()+"");
        majors.moveToFirst();
        while(!majors.isAfterLast()){
            ObjectEntry entry = new ObjectEntry(majors.getString(1));
            data.add(entry);
            majorAdapter.notifyDataSetChanged();
            majors.moveToNext();
        }

        refreshBars();

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
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        refreshBars();
        if (requestCode == 1) {
            String majorTitle = intent.getStringExtra("majorTitle");
            if(majorAdapter.contains(majorTitle)){
                Toast.makeText(this,"Major already exists" ,Toast.LENGTH_LONG).show();
            }
            else{
                ObjectEntry entry = new ObjectEntry(majorTitle);
                data.add(entry);
                majorAdapter.notifyDataSetChanged();
            }
        }

    }
    private void refreshBars(){
        Log.v("****************",majorAdapter.getCount()+"");
        for(int i=0; i<majorAdapter.getCount(); i++){
            View item = majorAdapter.getView(i,null,listView);
            ProgressBar bar = (ProgressBar) item.findViewById(R.id.progressBarMajor);
            String maj = ((TextView) item.findViewById(R.id.textViewMajor)).getText().toString();

            Cursor classesTaken = db.rawQuery("select count(*) from classes_majors as c, classes as cl, departments as d, progress as p, users as us\n" +
                    "where d.dept_name=\"" + maj+"\" and d._id =c.major_id and c.class_id = cl._id and cl._id=p.class_id and us.user_email=\""+email+"\"",null);
            classesTaken.moveToFirst();
            Log.v("While0 ",classesTaken.getString(0)+"");
            int totalNoClassesMajorTaken = Integer.valueOf(classesTaken.getString(0));

            //bar.setProgress(0); // call these two methods before setting progress.
            //bar.setMax(0);
            //bar.setMax(100);
            bar.setProgress(totalNoClassesMajorTaken/10);
            //bar.refreshDrawableState();

        }
    }
}