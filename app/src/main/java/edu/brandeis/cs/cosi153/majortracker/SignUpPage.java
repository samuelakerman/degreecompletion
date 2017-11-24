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

public class SignUpPage extends Activity {
    Context thisContext= this;
    private EditText name;
    private EditText email;
    private EditText passw1;
    private EditText passw2;
    private DatabaseHelper dbHelper = new DatabaseHelper(this);
    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        db = dbHelper.getWritableDatabase();

        Button btn_signup_enter=(Button) findViewById(R.id.btn_signup_enter);
        name = (EditText)  findViewById(R.id.nameInput);
        email = (EditText)  findViewById(R.id.emailInput);
        passw1 = (EditText)  findViewById(R.id.passwordInput);
        passw2 = (EditText)  findViewById(R.id.passwordInputRepeat);

        btn_signup_enter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(name.getText().length()==0)
                    Toast.makeText(thisContext,"Please, provide your name" ,Toast.LENGTH_LONG).show();
                else if(email.getText().length()<7 && !email.getText().toString().contains("@"))
                    Toast.makeText(thisContext,"Please, provide a valid email" ,Toast.LENGTH_LONG).show();
                else if(passw1.getText().toString().length()<6)
                    Toast.makeText(thisContext,"Password must have at least 6 characters" ,Toast.LENGTH_LONG).show();
                else if(!passw1.getText().toString().equals(passw2.getText().toString()))
                    Toast.makeText(thisContext,"Passwords do not match" ,Toast.LENGTH_LONG).show();
                else if(db.rawQuery("SELECT * FROM users where email=\""+email.getText().toString()+"\";",null).getCount()==1)
                    Toast.makeText(thisContext,"User already exists. Please login." ,Toast.LENGTH_LONG).show();
                else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COL_NAME,name.getText().toString());
                    values.put(DatabaseHelper.COL_EMAIL,email.getText().toString());
                    //TODO implement password ciphering
                    values.put(DatabaseHelper.COL_CIPHERPASS,passw1.getText().toString());
                    db.insert(DatabaseHelper.USERS_TABLE,null,values);

                    Intent intent = new Intent(thisContext,Profile.class);
                    intent.putExtra("user_email",email.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
