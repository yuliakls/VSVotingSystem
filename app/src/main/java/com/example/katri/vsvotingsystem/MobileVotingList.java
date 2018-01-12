package com.example.katri.vsvotingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MobileVotingList extends AppCompatActivity {
    User CurrentUser;
    ArrayList<HashMap<String, String>> voteList;
    ArrayList<Voting> UserVotings;
    public TextView data;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_voting_list);

        UserVotings = new ArrayList<>();
        CurrentUser = CurrentUser.getInstance();
        voteList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        data = (TextView) findViewById(R.id.hellouser);

        data.setText("Hello " + CurrentUser.GetUserName());

        new GetContacts().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView listText = (TextView) view.findViewById(R.id.name);
                String text = listText.getText().toString();

                //Toast.makeText(getApplicationContext(),"------- "+ i,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MobileVotingList.this, MobileCandidateList.class);
                intent.putExtra("VoteNum", "1");
                startActivity(intent);
            }
        });
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MobileVotingList.this,ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String UserID = "'"+ CurrentUser.GetUserID() + "'";
            String url ="https://morning-anchorage-32230.herokuapp.com/getvotes"+"?UserID=" + UserID;



            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONArray UserVotings = new JSONArray(jsonStr);

                    for (int i = 0; i < UserVotings.length(); i++) {
                        JSONObject c = UserVotings.getJSONObject(i);

                        String id = c.getString("VoteNum");
                        String start = c.getString("Start");
                        String end = c.getString("Finish");
                        String name = c.getString("VoteName");
                        String description = c.getString("VoteDescription");

                        Log.i("error------------",start);

                        try {
                            GetUser(id,start,end,name,description);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        HashMap<String, String> contact = new HashMap<>();
                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("start", start);
                        contact.put("end", end);
                        contact.put("description", description);
                        // adding vote to vote list
                        voteList.add(contact);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
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
                    MobileVotingList.this, voteList,
                    R.layout.list_item, new String[]{"name", "start",
                    "end","description"}, new int[]{R.id.name,
                    R.id.start,R.id.end, R.id.description});

            lv.setAdapter(adapter);
        }

        public  void GetUser(String id,String start,String end,String name,String description) throws ParseException {
            Voting NewVote = new Voting(Integer.valueOf(id),name,description,start,end);
            UserVotings.add(NewVote);
        }
    }
}
