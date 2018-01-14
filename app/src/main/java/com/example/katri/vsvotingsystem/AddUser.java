package com.example.katri.vsvotingsystem;

/**
 * Created by Sali on 13/01/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



import javax.net.ssl.HttpsURLConnection;

public class AddUser extends Activity{

    public Activity activity;
    private EditText UserID;
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private boolean Admin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_add_user);

        this.activity = activity;
        this.UserID = findViewById(R.id.UserID);
        this.Name = findViewById(R.id.Name);
        this.Email =  findViewById(R.id.Email);
        this.Password =  findViewById(R.id.Password);

    }

    public void checkAddUser(final View arg0) {
        if ((UserID.getText().toString()).equals( "" ) || (Password.getText().toString()).equals( "" ) || (Name.getText().toString()).equals( "" ) || (Email.getText().toString()).equals( "" )) {
            Toast.makeText( getApplicationContext(), "Error: You Have one or more missing Deatails!!", Toast.LENGTH_LONG ).show();
        } else {
            AddUserSync aus = new AddUserSync( this );
            aus.execute();
        }
    }
}
