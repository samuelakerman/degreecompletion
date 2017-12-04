package edu.brandeis.cs.cosi153.majortracker;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sebastian on 12/1/17.
 */

public class Profile extends AppCompatActivity {

    private ArrayList<ObjectEntry> data = new ArrayList<>();
    private ListView listView;
    private ProfileAdapter adapter;
    private DatabaseHelper dbHelper;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        email = getIntent().getExtras().getString("user_email");

        adapter = new ProfileAdapter(this, data);
        listView = (ListView) findViewById(R.id.myListView);
        TextView nameView = (TextView) findViewById(R.id.textViewUserName);
        Cursor c = db.rawQuery("select "+dbHelper.COL_NAME+" from "+dbHelper.USERS_TABLE+" where "+dbHelper.COL_EMAIL+" = "+"\""+email+"\"",null);
        c.moveToFirst();
        nameView.setText(c.getString(0));
        c.close();

        final Button addMajor = (Button) findViewById(R.id.buttonAddMajor);
        final Button addClass = (Button) findViewById(R.id.buttonAddClass);

        listView.setAdapter(adapter);

        addMajor.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Profile.this,AddMajor.class);
                startActivity(intent);
            }
        });
        addClass.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendMessage(v);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            ObjectEntry item = new ObjectEntry(intent.getStringExtra("Major Name"));
            data.add(item);
            listView = (ListView) findViewById(R.id.myListView);
            // listView.setAdapter(adapter);
        }
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, ClassesList.class);
        TextView textView = (TextView) findViewById(R.id.textViewMajor);

        String message = textView.getText().toString();
        intent.putExtra("major",message);
        startActivity(intent);
    }

//    for (int i = 0; i < listView.data.size();       i++) {//Loop it to get the values
//        if (data.get(i)) {
//            // Call the Intent Here
//            Intent intent = new Intent(Profile.this, ClassesList.class);
//            intent.putExtra(EXTRA_MESSAGE, Strings[i]);
//            startActivity(intent);
//        }
//    }
}