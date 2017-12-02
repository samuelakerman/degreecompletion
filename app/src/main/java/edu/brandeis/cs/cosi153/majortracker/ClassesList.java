package edu.brandeis.cs.cosi153.majortracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Created by sebastian on 12/1/17.
 */

public class ClassesList  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classes_list);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(Profile.EXTRA_MESSAGE);



        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textViewMajor);
        textView.setText(message);
    }
}

