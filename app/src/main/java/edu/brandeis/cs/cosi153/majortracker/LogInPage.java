package edu.brandeis.cs.cosi153.majortracker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by samuel on 11/23/17.
 */

public class LogInPage  extends Activity{
    private DatabaseHelper dbHelper = new DatabaseHelper(this);
    private SQLiteDatabase db;
    private EditText email;
    private EditText password;
    private Context thisContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        db = dbHelper.getWritableDatabase();
        thisContext = this;

        Button btn_login_enter=(Button) findViewById(R.id.btn_login_enter);

        email = (EditText)  findViewById(R.id.emailInputLogin);
        password = (EditText)  findViewById(R.id.passwordInputLogin);

        btn_login_enter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Cursor c = db.rawQuery("SELECT * FROM users where "+dbHelper.COL_EMAIL+"=\""+email.getText().toString()+"\";",null);
                c.moveToFirst();
                if(c.getCount()==0)
                    Toast.makeText(thisContext,"User does not exist. Please, sign up" ,Toast.LENGTH_LONG).show();
                else if(!c.getString(4).toString().equals(password.getText().toString())){
                    Toast.makeText(thisContext,"Invalid password. Try again" ,Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(thisContext,Profile.class);
                    intent.putExtra("user_email",email.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }
}
