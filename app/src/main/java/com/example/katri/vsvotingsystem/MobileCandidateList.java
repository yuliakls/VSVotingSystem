package com.example.katri.vsvotingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
///this is comment
public class MobileCandidateList extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;
    String User;
    String ExistingVote;
    ArrayList<HashMap<String, String>> voteList;
    public TextView data;
    User CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_candidate_list);

        Intent intent = getIntent();
        //User = intent.getStringExtra("UserID");

        CurrentUser = CurrentUser.getInstance();
        ExistingVote = intent.getStringExtra("VoteNum");

        voteList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        data = (TextView) findViewById(R.id.hellouser);
        data.setText("Hello " + CurrentUser.GetUserName());

        new GetContacts().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView listText = (TextView) view.findViewById(R.id.name);
                //String text = listText.getText().toString();
                new SendRequest().execute();
            }
        });
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public class SendRequest extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MobileCandidateList.this,ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        protected String doInBackground(String... arg0) {
            BallotKeyBuilder Key = new BallotKeyBuilder(MobileCandidateList.this.getApplicationContext());
            //Log.i("key------------------",obj.BuildKey());

            String ID = "'" + CurrentUser.GetUserID() + "'";
            String VN = "'" + ExistingVote + "'";
            String CandidateID = "'4'";

            String answer = "false";

            try {
                URL url = new URL("https://morning-anchorage-32230.herokuapp.com/uservoted");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("UserID", ID);
                postDataParams.put("VoteNum", VN);

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
                    answer = sb.toString();
                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }


            if(answer.equals("true")) {
                try {
                    Log.i("Yeahhhhhhhh",answer);
                    URL url = new URL("https://morning-anchorage-32230.herokuapp.com/sendballot");

                    JSONObject postDataParams = new JSONObject();

                    postDataParams.put("CandidateID", "'3'");
                    postDataParams.put("VoteNum", "'1'");
                    postDataParams.put("VoteKey", "'totototototo'");


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

                        Log.i("Yeahhhh2222222",sb.toString());

                        return sb.toString();
                    } else {
                        return new String("false : " + responseCode);
                    }
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
            else return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing()) pDialog.dismiss();

            if(!result.equals("")){
                //boolean answer = Boolean.parseBoolean(result);
                Log.i("-------------------",result);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error in server result!", Toast.LENGTH_LONG).show();
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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MobileCandidateList.this,ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String VoteNum = "'"+ExistingVote+"'";
            String url ="https://morning-anchorage-32230.herokuapp.com/getcandidates"+"?VoteNum=" + VoteNum;

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONArray UserVotings = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < UserVotings.length(); i++) {
                        JSONObject c = UserVotings.getJSONObject(i);

                        String id = c.getString("CandidateID");
                        String name = c.getString("CandidateName");


                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);

                        // adding contact to contact list
                        voteList.add(contact);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MobileCandidateList.this, voteList,
                    R.layout.list_candidates_item, new String[]{"name"}, new int[]{R.id.name});

            lv.setAdapter(adapter);
        }

    }
}
