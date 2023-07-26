package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class CreateMatch extends AppCompatActivity {
    static String matchKEY;
    
    private String hostTeamName,vistorTeamName;
    private int Overs;
    String toss,opted;
    private String h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11;
    private String v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11;

    private ArrayList<String> mymatches = new ArrayList<String>();
    TextView keyView;
    Button _continue,_continue2;
    ImageView share;
    DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Scorify");
    DatabaseReference user= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");

    Intent intent;
    
    private ArrayList<String> hostTeam;
    private ArrayList<String> vistorTeam;
    private String[] pushKeyID_one = new String[11];
    private String[] pushKeyID_two = new String[11];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_create_match);


        initializeUI();
        _continue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateMatch.this);
                builder.setCancelable(true);
                builder.setMessage("Are You Sure You Want To Create Match?");
                builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog,which)->{
                    _continue.setVisibility(View.GONE);
                    _continue2.setVisibility(View.VISIBLE);
                    createMatchID();
                } );

                builder.setNegativeButton("No",(DialogInterface.OnClickListener) (dialog,which)->{
                    dialog.cancel();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,matchKEY);
                startActivity(Intent.createChooser(shareIntent,"Share Match ID Using"));
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createMatchID(){
        matchKEY = new String(myref.push().getKey());   
        keyView.setText(matchKEY);
        myref = myref.child(matchKEY);
        InsertMatch(matchKEY);
        getData();
        share.setVisibility(View.VISIBLE);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getData(){
        intent = getIntent();
        hostTeamName = intent.getStringExtra("HostTeam");
        vistorTeamName = intent.getStringExtra("VisitorTeam");
        Overs = Integer.parseInt(intent.getStringExtra("Overs"));

        h1 = intent.getStringExtra("H1");
        h2 = intent.getStringExtra("H2");
        h3 = intent.getStringExtra("H3");
        h4 = intent.getStringExtra("H4");
        h5 = intent.getStringExtra("H5");
        h6 = intent.getStringExtra("H6");
        h7 = intent.getStringExtra("H7");
        h8 = intent.getStringExtra("H8");
        h9 = intent.getStringExtra("H9");
        h10 = intent.getStringExtra("H10");
        h11 = intent.getStringExtra("H11");

        v1 = intent.getStringExtra("V1");
        v2 = intent.getStringExtra("V2");
        v3 = intent.getStringExtra("V3");
        v4 = intent.getStringExtra("V4");
        v5 = intent.getStringExtra("V5");
        v6 = intent.getStringExtra("V6");
        v7 = intent.getStringExtra("V7");
        v8 = intent.getStringExtra("V8");
        v9 = intent.getStringExtra("V9");
        v10 = intent.getStringExtra("V10");
        v11 = intent.getStringExtra("V11");

        toss = intent.getStringExtra("TOSS");
        opted = intent.getStringExtra("OPT");








        myref.child("tosswon").setValue(toss);
        myref.child("tossopt").setValue(opted);
        
        hostTeam = new ArrayList<String>();
        vistorTeam = new ArrayList<String>();

        hostTeam.add(h1);
        hostTeam.add(h2);
        hostTeam.add(h3);
        hostTeam.add(h4);
        hostTeam.add(h5);
        hostTeam.add(h6);
        hostTeam.add(h7);
        hostTeam.add(h8);
        hostTeam.add(h9);
        hostTeam.add(h10);
        hostTeam.add(h11);

        vistorTeam.add(v1);
        vistorTeam.add(v2);
        vistorTeam.add(v3);
        vistorTeam.add(v4);
        vistorTeam.add(v5);
        vistorTeam.add(v6);
        vistorTeam.add(v7);
        vistorTeam.add(v8);
        vistorTeam.add(v9);
        vistorTeam.add(v10);
        vistorTeam.add(v11);

        HashMap<String,Object>[] TeamOnePlayerRow = new HashMap[11];

        for(int i = 0; i<11; i++){
            TeamOnePlayerRow[i] = new HashMap<>();
            TeamOnePlayerRow[i].put("Name",hostTeam.get(i));
            TeamOnePlayerRow[i].put("Runs",0);
            TeamOnePlayerRow[i].put("Boundaries",0);
            TeamOnePlayerRow[i].put("Balls",0);
            TeamOnePlayerRow[i].put("Sixes",0);
            TeamOnePlayerRow[i].put("StrikeRate",0);
            TeamOnePlayerRow[i].put("RunsGiven",0);
            TeamOnePlayerRow[i].put("WicketsTaken",0);
            TeamOnePlayerRow[i].put("Overs",0);
            TeamOnePlayerRow[i].put("EconomyRate",0);
            TeamOnePlayerRow[i].put("TakenBy","");
            TeamOnePlayerRow[i].put("status","");
            pushKeyID_one[i] = myref.child("Team1").push().getKey();
            myref.child("Team1").child(pushKeyID_one[i]).setValue(TeamOnePlayerRow[i]);

        }

        HashMap<String,Object>[] TeamTwoPlayerRow = new HashMap[11];
        for(int i=0;i<11;i++){
            TeamTwoPlayerRow[i] = new HashMap<>();
            TeamTwoPlayerRow[i].put("Name",vistorTeam.get(i));
            TeamTwoPlayerRow[i].put("Runs",0);
            TeamTwoPlayerRow[i].put("Boundaries",0);
            TeamTwoPlayerRow[i].put("Balls",0);
            TeamTwoPlayerRow[i].put("Sixes",0);
            TeamTwoPlayerRow[i].put("StrikeRate",0);
            TeamTwoPlayerRow[i].put("RunsGiven",0);
            TeamTwoPlayerRow[i].put("WicketsTaken",0);
            TeamTwoPlayerRow[i].put("Overs",0);
            TeamTwoPlayerRow[i].put("EconomyRate",0);
            TeamTwoPlayerRow[i].put("TakenBy","");
            TeamTwoPlayerRow[i].put("status","");
            pushKeyID_two[i] = myref.child("Team2").push().getKey();
            myref.child("Team2").child(pushKeyID_two[i]).setValue(TeamTwoPlayerRow[i]);

        }

        myref.child("Overs").setValue(Overs);
        myref.child("HostTeamName").setValue(hostTeamName);
        myref.child("VistorTeamName").setValue(vistorTeamName);
        myref.child("Runrate").setValue(0.0);
        myref.child("inning").setValue("Inning1");
        myref.child("thisOver").setValue("");

        HashMap<String,Object> inningdata1 = new HashMap<String,Object>();
        inningdata1.put("CurBat1",pushKeyID_one[0]);
        inningdata1.put("CurBat2",pushKeyID_one[1]);
        inningdata1.put("CurBowler","");
        inningdata1.put("CurrentOvers",0);
        inningdata1.put("Totalruns",0);
        inningdata1.put("WicketsOut",0);
        inningdata1.put("Extras","");
        inningdata1.put("FallOfWicket","");
        inningdata1.put("ballByBall","");
        myref.child("Inning1").setValue(inningdata1);

        HashMap<String,Object> inningdata2 = new HashMap<String,Object>();
        inningdata2.put("CurBat1",pushKeyID_two[0]);
        inningdata2.put("CurBat2",pushKeyID_two[1]);
        inningdata2.put("CurBowler","");
        inningdata2.put("CurrentOvers",0);
        inningdata2.put("Totalruns",0);
        inningdata2.put("WicketsOut",0);
        inningdata2.put("Extras","");
        inningdata2.put("FallOfWicket","");
        inningdata2.put("ballByBall","");
        myref.child("Inning2").setValue(inningdata2);

        myref.child("Inning1").child("Batsmens_inningone").setValue("");
        myref.child("Inning1").child("Bowlers_inningone").setValue("");

        myref.child("Inning1").child("Bowlers_inningtwo").setValue("");
        myref.child("Inning1").child("Batsmens_inningtwo").setValue("");


        myref.child("team1_keys").setValue(Arrays.asList(pushKeyID_one));
        myref.child("team2_keys").setValue(Arrays.asList(pushKeyID_two));
        myref.child("team1_name").setValue(hostTeam);
        myref.child("team2_name").setValue(vistorTeam);

        myref.child("TemporaryData").child("team1_keys").setValue(Arrays.asList(pushKeyID_one));
        myref.child("TemporaryData").child("team2_keys").setValue(Arrays.asList(pushKeyID_two));
        myref.child("TemporaryData").child("team1").setValue(hostTeam);
        myref.child("TemporaryData").child("team2").setValue(vistorTeam);



        myref.child("State").setValue("premature");
        myref.child("localOver").setValue(0);
        myref.child("strike").setValue(true);
        myref.child("isFinished").setValue(false);


        //Current Date and Time of the match
        LocalDateTime datetime =  LocalDateTime.now();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
        try{
            Date mydate = format.parse(datetime.toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            //Date
            String date = dateFormat.format(mydate);
            //Time
            String time = timeFormat.format(mydate);

            myref.child("Date").setValue(date);
            myref.child("Time").setValue(time);

        }catch(Exception e){/*Parse Exception*/};


        _continue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manager = new Intent(getApplicationContext(), Manager.class);
                manager.putExtra("KEY", matchKEY);
                startActivity(manager);
            }
        });
    }

    private void initializeUI(){
        keyView = (TextView) findViewById(R.id.matchID);
        _continue = (Button) findViewById(R.id._continue);
        _continue2 = (Button) findViewById(R.id._continue2);
        share = findViewById(R.id.share);
    }

    private void InsertMatch(String matchID){
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        user = user.child(thisuser.getUid()).child("matches");
        if (thisuser != null) {
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChildren()){
                        mymatches.clear();
                        mymatches = (ArrayList<String>) snapshot.getValue();
                        mymatches.add(matchID);
                        user.setValue(mymatches);

                    }else{
                        mymatches.add(matchID);
                        user.setValue(mymatches);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Something Went Wrong! Please try again later", Toast.LENGTH_LONG).show();
        }

    }


}