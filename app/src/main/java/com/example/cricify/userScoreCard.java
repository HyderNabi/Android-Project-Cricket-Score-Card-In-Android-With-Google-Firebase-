package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class userScoreCard extends AppCompatActivity {

    private RecyclerView recyclerView1,recyclerView2;
    private MyAdapter adapter1,adapter2;
    private ArrayList<Model> batlist,bowllist;

    private String matchKEY;

    private String inning;
    private Boolean strike;

    private String BatTeam;
    private String BowlTeam;

    public int wicketsOUt;
    public int currentOver;

    public String vistorTeam,hostTeam;

    TextView First_BatsmanName,Second_BatsmanName,Runs,Wickets_Out,Current_Over;
    TextView Run_Rate,Current_Bowler,Inning_Status,Runs_OnStrike,Runs_NOnStrike;
    TextView Balls_NOnStrike,Balls_OnStrike,SixesNonStrike,SixesonStrike,FoursNonStrike,FoursonStrike;
    TextView BowEconomy,SROnStrike, SRNOnStrike, BowOvers,BowRuns,BowWickets,thisOver;
    TextView strikeCurA, strikeCurB,TeamName,matchStatus,wonBy,runsNeeded,toss_Opt,toss_won,total_Over,playedBW;


    Intent i;


    DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Scorify");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_user_score_card);

        i = getIntent();
        matchKEY = i.getStringExtra("KEY");
        myref = myref.child(matchKEY);
        initializeUI();
        finalizeUI();

        recyclerView1 = findViewById(R.id.recycler_view);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        batlist = new ArrayList<>();
        adapter1 = new MyAdapter(this ,batlist );
        recyclerView1.setAdapter(adapter1);

       recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        bowllist = new ArrayList<>();
        adapter2 = new MyAdapter(this ,bowllist );
        recyclerView2.setAdapter(adapter2);




    }

    public void initializeUI(){
        First_BatsmanName = (TextView) findViewById(R.id.FBatName);
        Second_BatsmanName = (TextView) findViewById(R.id.SBatName);
        Runs = (TextView)findViewById(R.id.Runs);
        Wickets_Out = (TextView) findViewById((R.id.wicketsOut));
        Current_Over = (TextView) findViewById(R.id.currentOver);
        Run_Rate = (TextView)findViewById(R.id.runRate);
        Current_Bowler = (TextView)findViewById(R.id.CurBowler);
        Inning_Status = (TextView) findViewById(R.id.InningStatus);
        Runs_OnStrike = (TextView) findViewById(R.id.RunsOnStrike);
        Runs_NOnStrike = (TextView) findViewById(R.id.RunsNOnStrike);
        Balls_OnStrike = (TextView) findViewById(R.id.BallsOnStrike);
        Balls_NOnStrike = (TextView) findViewById(R.id.BallsNOnStrike);
        FoursonStrike = (TextView) findViewById((R.id.Fours_onStrike));
        FoursNonStrike = (TextView) findViewById((R.id.Fours_NonStrike));
        SixesonStrike = (TextView) findViewById((R.id.Sixes_onStrike));
        SixesNonStrike = (TextView) findViewById((R.id.Sixes_NonStrike));
        SROnStrike = (TextView) findViewById((R.id.SR_OnStrike));
        SRNOnStrike = (TextView) findViewById((R.id.SR_NOnStrike));
        BowEconomy = (TextView) findViewById((R.id.Bow_Economy));
        BowOvers = (TextView) findViewById((R.id.Bow_Overs));
        BowRuns = (TextView) findViewById((R.id.Bow_Runs));
        BowWickets = (TextView) findViewById((R.id.Bow_Wickets));
        thisOver = (TextView) findViewById((R.id.this_Over));
        strikeCurA = (TextView) findViewById((R.id.strikeCurA));
        strikeCurB = (TextView) findViewById((R.id.strikeCurB));
        TeamName = (TextView) findViewById((R.id.TeamName));

        matchStatus = (TextView) findViewById(R.id.matchStatus);
        wonBy = (TextView) findViewById(R.id.wonBy);
        runsNeeded = (TextView) findViewById(R.id.runsNeeded);

        total_Over = (TextView) findViewById((R.id.totalOver));
        toss_won = (TextView) findViewById((R.id.tosswon));
        toss_Opt = (TextView) findViewById((R.id.tossOpt));
        playedBW = (TextView) findViewById((R.id.playedBW));


    }
    public void finalizeUI(){
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    inning = dataSnapshot.child("inning").getValue(String.class);
                    if(inning.equals("Inning1")){
                        BatTeam = "Team1";
                        BowlTeam = "Team2";
                    }else{
                        BatTeam = "Team2";
                        BowlTeam = "Team1";
                    }

                    String p1 = dataSnapshot.child(inning).child("CurBat1").getValue(String.class);
                    String p2 = dataSnapshot.child(inning).child("CurBat2").getValue(String.class);

                    String p3 = dataSnapshot.child(inning).child("Totalruns").getValue(Integer.class).toString();
                    String p4 = dataSnapshot.child(inning).child("WicketsOut").getValue(Integer.class).toString();

                    wicketsOUt = Integer.parseInt(p4);

                    Integer p5 = dataSnapshot.child(inning).child("CurrentOvers").getValue(Integer.class);
                    currentOver = p5;
                    String p6 = dataSnapshot.child("Runrate").getValue(Double.class).toString();
                    String p7 = dataSnapshot.child(inning).child("CurBowler").getValue(String.class);
                    String p8 = dataSnapshot.child("inning").getValue(String.class);

                    First_BatsmanName.setText(dataSnapshot.child(BatTeam).child(p1).child("Name").getValue(String.class));
                    Second_BatsmanName.setText(dataSnapshot.child(BatTeam).child(p2).child("Name").getValue(String.class));
                    Runs.setText(p3);
                    Wickets_Out.setText(p4);
                    Current_Over.setText(FormatOvers(p5));
                    Run_Rate.setText(p6);

                    Current_Bowler.setText(dataSnapshot.child(BowlTeam).child(p7).child("Name").getValue(String.class));


                    Runs_OnStrike.setText(dataSnapshot.child(BatTeam).child(p1).child("Runs").getValue(Integer.class).toString());
                    Runs_NOnStrike.setText(dataSnapshot.child(BatTeam).child(p2).child("Runs").getValue(Integer.class).toString());


                    Balls_OnStrike.setText(dataSnapshot.child(BatTeam).child(p1).child("Balls").getValue(Integer.class).toString());
                    Balls_NOnStrike.setText(dataSnapshot.child(BatTeam).child(p2).child("Balls").getValue(Integer.class).toString());

                    FoursonStrike.setText(dataSnapshot.child(BatTeam).child(p1).child("Boundaries").getValue(Integer.class).toString());
                    FoursNonStrike.setText(dataSnapshot.child(BatTeam).child(p2).child("Boundaries").getValue(Integer.class).toString());

                    SixesonStrike.setText(dataSnapshot.child(BatTeam).child(p1).child("Sixes").getValue(Integer.class).toString());
                    SixesNonStrike.setText(dataSnapshot.child(BatTeam).child(p2).child("Sixes").getValue(Integer.class).toString());

                    SROnStrike.setText(dataSnapshot.child(BatTeam).child(p1).child("StrikeRate").getValue(Double.class).toString());
                    SRNOnStrike.setText(dataSnapshot.child(BatTeam).child(p2).child("StrikeRate").getValue(Double.class).toString());
                    BowEconomy.setText(dataSnapshot.child(BowlTeam).child(p7).child("EconomyRate").getValue(Double.class).toString());

                    BowWickets.setText(dataSnapshot.child(BowlTeam).child(p7).child("WicketsTaken").getValue(Integer.class).toString());
                    BowRuns.setText(dataSnapshot.child(BowlTeam).child(p7).child("RunsGiven").getValue(Integer.class).toString());
                    BowOvers.setText(FormatOvers(dataSnapshot.child(BowlTeam).child(p7).child("Overs").getValue(Integer.class)));

                    thisOver.setText(dataSnapshot.child("thisOver").getValue(String.class));
                    strike = dataSnapshot.child("strike").getValue(Boolean.class);

                    if(p8.equals("Inning1")) {
                        Inning_Status.setText("1st Inning");
                        TeamName.setText(dataSnapshot.child("HostTeamName").getValue(String.class).toString());
                    }else {
                        Inning_Status.setText("2nd Inning");
                        TeamName.setText(dataSnapshot.child("VistorTeamName").getValue(String.class).toString());
                    }
                    if(strike){
                        strikeCurA.setText("*");
                        strikeCurB.setText("");

                    }else{
                        strikeCurA.setText("");
                        strikeCurB.setText("*");
                    }

                    total_Over.setText(dataSnapshot.child("Overs").getValue(Integer.class).toString());
                    toss_won.setText(dataSnapshot.child("tosswon").getValue(String.class));
                    toss_Opt.setText(dataSnapshot.child("tossopt").getValue(String.class));

                    hostTeam = dataSnapshot.child("HostTeamName").getValue(String.class).toString();
                    vistorTeam = dataSnapshot.child("VistorTeamName").getValue(String.class).toString();

                    playedBW.setText(hostTeam+" vs "+vistorTeam);

                    StatusPanel();
                    BatSummary();

                }catch(Exception e){};



            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
    private String FormatOvers(int overs){
        String over = String.valueOf(overs/6);
        over += "."+overs%6;
        return over;
    }
    private void BatSummary(){
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                batlist.clear();
                bowllist.clear();
                for(DataSnapshot snap : snapshot.child(BatTeam).getChildren()) {
                    Model model1 = snap.getValue(Model.class);
                    batlist.add(model1);
                }
                adapter1.notifyDataSetChanged();

                for(DataSnapshot snap : snapshot.child(BowlTeam).getChildren()) {
                    Model model2 = snap.getValue(Model.class);
                    bowllist.add(model2);
                }

                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void StatusPanel(){
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int first_inning_runs = snapshot.child("Inning1").child("Totalruns").getValue(Integer.class);
                int second_inning_runs = snapshot.child("Inning2").child("Totalruns").getValue(Integer.class);
                int total_overs = snapshot.child("Overs").getValue(Integer.class);

               if(snapshot.child("isFinished").getValue(Boolean.class)){
                   matchStatus.setText("CLOSED");
                   runsNeeded.setVisibility(View.GONE);
                   if(wonBy.getVisibility() == View.GONE){wonBy.setVisibility(View.VISIBLE);}
                   if(second_inning_runs>=(first_inning_runs+1)){
                       wonBy.setText("Match Won By "+vistorTeam+" By "+(11-wicketsOUt)+ " Wickets");
                   }else if(second_inning_runs == first_inning_runs){
                       wonBy.setText("Draw Match");
                   }else{
                       wonBy.setText("Match Won By "+hostTeam+" By "+((first_inning_runs+1)-second_inning_runs)+" Runs");
                   }
               }else{
                   matchStatus.setText("OPEN");
                   if(snapshot.child("inning").getValue(String.class).equals("Inning2")){
                       if(runsNeeded.getVisibility() == View.GONE){runsNeeded.setVisibility(View.VISIBLE);}
                       runsNeeded.setText(((first_inning_runs+1)-second_inning_runs)+" Runs Needed By "+((total_overs*6)-currentOver)+" Balls");
                   }

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
