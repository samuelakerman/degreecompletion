package edu.brandeis.cs.cosi153.majortracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button btn_login;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login=(Button) findViewById(R.id.btn_login);
        btn_signup=(Button) findViewById(R.id.btn_signup);

        //This will create and populate the database during the app installation
        //avoiding lags while the user is interacting with the app
        SharedPreferences ratePrefs = getSharedPreferences("First Update", 0);
        if (!ratePrefs.getBoolean("FrstTime", false)) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.getWritableDatabase();

            SharedPreferences.Editor edit = ratePrefs.edit();
            edit.putBoolean("FrstTime", true);
            edit.commit();
        }

        btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,LogInPage.class);
                startActivity(intent);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,SignUpPage.class);
                startActivity(intent);

            }
        });
    }
}