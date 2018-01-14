package com.example.katri.vsvotingsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MobileLogin extends AppCompatActivity {
    private EditText UserID;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_login);

        UserID = (EditText) findViewById(R.id.UserID);
        Password = (EditText) findViewById(R.id.Password);
    }

    public void checkLogin(final View arg0) {
        if((UserID.getText().toString()).equals("") || (Password.getText().toString()).equals("")){
            Toast.makeText(getApplicationContext(), "Error: there is missing details!", Toast.LENGTH_LONG).show();
        }
        else {
            UserSync us = new UserSync(this);
            us.execute();
            //new SendRequest().execute();
        }
    }


    public void tatata(){
        Toast.makeText(getApplicationContext(), "tatata", Toast.LENGTH_LONG).show();
    }
/*
    public class SendRequest extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MobileLogin.this,ProgressDialog.STYLE_SPINNER);
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
                conn.setReadTimeout(15000 );
                conn.setConnectTimeout(15000);
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
                    return new String("false : " + responseCode);
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
                    GetUser(result);
                    Intent intent = new Intent(MobileLogin.this,MobileVotingList.class);
                    startActivity(intent);
                    MobileLogin.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error in getting user(json)", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Error: the user doesn't exist!", Toast.LENGTH_LONG).show();
            }
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
    public  void GetUser(String jsonStr) throws JSONException {
        JSONObject JO = null;
        JO = new JSONObject(jsonStr);
        String id = JO.getString("UserID");
        String name = JO.getString("Name");
        String email = JO.getString("Email");
        String password = JO.getString("Password");
        boolean admin = Boolean.parseBoolean(JO.getString("Admin"));
        User NewUser = User.init(id,name,email,password,admin);
    }
    */
}