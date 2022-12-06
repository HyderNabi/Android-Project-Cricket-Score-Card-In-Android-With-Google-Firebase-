package com.example.cricify;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LiveScore extends AppCompatActivity {
    private String URL = "https://api.cricapi.com/v1/currentMatches?apikey=21e2eb2c-336f-4fff-a909-730c345d91b9&offset=0";

    private RecyclerView recyclerView;
    private LiveMatchAdapter adapter;
    private ArrayList<LiveMatchModel> LiveList,temp1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_live_score);

        recyclerView = findViewById(R.id.live_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LiveList = new ArrayList<>();
        temp1 = new ArrayList<>();
        adapter = new LiveMatchAdapter(this ,LiveList );
        recyclerView.setAdapter(adapter);

       loadData();

        ImageButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

    }

    private void loadData(){
        LiveList.clear();
        temp1.clear();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest dataRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                try{
                    String result = new JSONObject(response).getString("status");
                    progressDialog.dismiss();
                    if(result.equals("failure")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LiveScore.this);
                        builder.setCancelable(true);
                        builder.setMessage("Server Unreachable...Try Again!");
                        builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which)->{
                            loadData();
                        });
                        builder.setNegativeButton("No",(DialogInterface.OnClickListener) (dialog,which)->{
                            dialog.cancel();
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                        JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            try{
                                //Name of the Match
                                String name = jsonArray.getJSONObject(i).getString("name");

                                //Match Type
                                String matchType = jsonArray.getJSONObject(i).getString("matchType");

                                //Match Status
                                String status = jsonArray.getJSONObject(i).getString("status");

                                //Match Venue
                                String venue = jsonArray.getJSONObject(i).getString("venue");

                                //Match Date and Time
                                String datetime = jsonArray.getJSONObject(i).getString("dateTimeGMT");

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
                                Date mydate = format.parse(datetime);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

                                //Date
                                String date = dateFormat.format(mydate);
                                //Time
                                String time = timeFormat.format(mydate);

                                //Team Names
                                String teams[] = new String[2];
                                JSONArray teamsJsonArray = jsonArray.getJSONObject(i).getJSONArray("teams");
                                for(int j=0;j<teams.length;j++){
                                    teams[j] = teamsJsonArray.getString(j);
                                }

                                //Team logo urls
                                JSONArray teamInfo = jsonArray.getJSONObject(i).getJSONArray("teamInfo");
                                String URLS[] = new String[teamInfo.length()];
                                for(int m=0;m<teamInfo.length();m++){
                                    URLS[m] = teamInfo.getJSONObject(m).getString("img");
                                }



                                //Match Score
                                JSONArray score = jsonArray.getJSONObject(i).getJSONArray("score");
                                String runs[] = new String[score.length()];
                                String wickets[] = new String[score.length()];
                                String overs[] = new String[score.length()];
                                for(int k=0;k<score.length();k++){
                                    runs[k] = score.getJSONObject(k).getString("r");
                                    wickets[k] = score.getJSONObject(k).getString("w");
                                    overs[k] = score.getJSONObject(k).getString("o");
                                }

                                //Match Started and Ended
                                String matchStarted = jsonArray.getJSONObject(i).getString("matchStarted");
                                String matchEnded = jsonArray.getJSONObject(i).getString("matchEnded");

                                LiveMatchModel model = new LiveMatchModel(name, matchType, status, venue, date, time, teams, URLS, runs, wickets, overs, matchEnded, matchStarted);

                                if(matchEnded.equals("false")){
                                    if(status.contains("No result") || status.contains("Match tied") || status.contains("Match Tied") || status.contains("No Result"))
                                    {
                                        temp1.add(model);
                                    }else{
                                        LiveList.add(model);
                                    }

                                }else{
                                    temp1.add(model);
                                }



                            }catch (Exception e){}
                        }
                        for(int i=0;i<temp1.size();i++){
                            LiveList.add(temp1.get(i));
                        }
                        adapter.notifyDataSetChanged();

                    }

                }catch(Exception e){}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(LiveScore.this);
                builder.setCancelable(true);
                builder.setMessage("Timeout...Reload?");
                builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which)->{
                    loadData();
                });
                builder.setNegativeButton("No",(DialogInterface.OnClickListener) (dialog,which)->{
                    dialog.cancel();
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(dataRequest);

    }
}