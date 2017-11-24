package edu.brandeis.cs.cosi153.majortracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by samuel on 11/24/17.
 */

public class Profile extends Activity {
    private String email;
    private Context thisContext;
    private DatabaseHelper dbHelper = new DatabaseHelper(this);
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        thisContext = this;
        db = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        Toast.makeText(thisContext,email ,Toast.LENGTH_LONG).show();

    }
}
