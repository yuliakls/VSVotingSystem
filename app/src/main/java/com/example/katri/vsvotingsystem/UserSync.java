package com.example.katri.vsvotingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Katri on 07/01/2018.
 */

public class UserSync extends AsyncTask<String, Void, String> {
    public Activity activity;

    private EditText UserID;
    private EditText Password;

    public UserSync(Activity activity){
        this.activity = activity;

        this.UserID = (EditText) activity.findViewById(R.id.UserID);
        this.Password = (EditText) activity.findViewById(R.id.Password);
    }

    private ProgressDialog pDialog;

    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity,ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }

    protected String doInBackground(String... arg0) {
        String ID = "'" + UserID.getText().toString() + "'";
        String PS = "'" + Password.getText().toString() + "'";

        try {
            URL url = new URL("https://morning-anchorage-32230.herokuapp.com/login");

            JSONObject postDataParams = new JSONObject();

            postDataParams.put("UserID", ID);
            postDataParams.put("Password", PS);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            } else {
                return new String("False : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (pDialog.isShowing()) pDialog.dismiss();

        if(!result.equals("")){
            try {
                CreateUser(result);
                //((activity.getActivity())).foo();
                Intent intent = new Intent(activity,MobileVotingList.class);
                activity.startActivity(intent);
                activity.finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), "Error in getting user(json)", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(activity.getApplicationContext(), "Error: the user doesn't exist!", Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    public  void CreateUser(String jsonStr) throws JSONException {
        JSONObject JO  = new JSONObject(jsonStr);
        String id = JO.getString("UserID");
        String name = JO.getString("Name");
        String email = JO.getString("Email");
        String password = JO.getString("Password");
        boolean admin = Boolean.parseBoolean(JO.getString("Admin"));

        User NewUser = User.init(id,name,email,password,admin);
    }
}

