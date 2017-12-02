package edu.brandeis.cs.cosi153.majortracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sebastian on 12/1/17.
 */

public class Profile extends AppCompatActivity {

    private ArrayList<ObjectEntry> data = new ArrayList<ObjectEntry>();
    private ListView listView;
    private ProfileAdapter adapter;
    public static final String EXTRA_MESSAGE = "edu.brandeis.cs.mariamoncaleano.progress.MESSAGE";
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        adapter = new ProfileAdapter(this, data);
        listView = (ListView) findViewById(R.id.myListView);

        // final Button addMajor = (Button) findViewById(R.id.buttonAddMajor);
        //final Button addClass = (Button) findViewById(R.id.buttonAddClass);

        listView.setAdapter(adapter);
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
        intent.putExtra(EXTRA_MESSAGE, message);
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