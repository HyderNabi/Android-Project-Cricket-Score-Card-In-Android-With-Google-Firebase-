package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Manager extends AppCompatActivity
{
    static String matchKEY;

    static double Overs;

    static ArrayList<String> hostTeam,vistorTeam; //Names of Two Teams



    static int FirstInningRuns;


    static String inning;
    static int totalRuns;
    static int currentOver;
    static int wicketsOut;
    static double runRate;
    static String ThisOver;
    static Boolean strike;
    static int localOver;

    static String BatTeam;
    static String BowlTeam;



    static String BatsmanOnStrike = "";
    static String BatsmanNonStrike = "";
    static String CurrentBowler = "";

    static int BatsmanOnStrike_runs;
    static int BatsmanOnStrike_balls;
    static int BatsmanOnStrike_fours;
    static int BatsmanOnStrike_sixes;
    static double BatsmanOnStrike_strikerate;

    static int BatsmanNonStrike_runs;
    static int BatsmanNonStrike_balls;
    static int BatsmanNonStrike_fours;
    static int BatsmanNonStrike_sixes;
    static double BatsmanNonStrike_strikerate;

    static int CurrentBowler_runs;
    static int CurrentBowler_overs;
    static int CurrentBowler_wickets;
    static double CurrentBowler_economyrate;



    static ArrayList<String> pushKeyID_one = new ArrayList<String>();
    static ArrayList<String> pushKeyID_two = new ArrayList<String>();

    static ArrayList<String> Temp_key_one = new ArrayList<String>();
    static ArrayList<String> Temp_key_two = new ArrayList<String>();
    static ArrayList<String> Temp_name_one = new ArrayList<String>();
    static ArrayList<String> Temp_name_two = new ArrayList<String>();



    TextView First_BatsmanName,Second_BatsmanName,Runs,Wickets_Out,Current_Over;
    TextView Run_Rate,Current_Bowler,Inning_Status,Runs_OnStrike,Runs_NOnStrike;
    TextView Balls_NOnStrike,Balls_OnStrike,SixesNonStrike,SixesonStrike,FoursNonStrike,FoursonStrike;
    TextView BowEconomy,SROnStrike, SRNOnStrike, BowOvers,BowRuns,BowWickets,thisOver;
    TextView strikeCurA, strikeCurB,TeamName,toss_Opt,toss_won,total_Over;

    ImageView summary;


    Intent i;

    String State;


    DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Scorify");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_manager);

        i = getIntent();
        matchKEY = i.getStringExtra("KEY");
        myref = myref.child(matchKEY);

        //EXECUTION STARTS FROM HERE :::::::::
        initializeUI();//CAST VIEWS ON UI TO JAVA CODE
        finalizeUI();
        getInning();    //RESTORE THE STATE OF THE INNING


        Button one_Btn =findViewById(R.id.ButtonRun_one);
        one_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateState(1,1);
                ThisOver = ThisOver+"  "+1;
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(1);
                    if(localOver%6 == 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";
                        SelectBowler();
                    }
                }
            }

        });

        Button two_Btn =findViewById(R.id.ButtonRun_two);
        two_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateState(2,1);
                ThisOver = ThisOver+"  "+2;
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(2);
                    if(localOver%6 == 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";
                        SelectBowler();
                    }
                }

            }

        });

        Button three_Btn =findViewById(R.id.ButtonRun_three);
        three_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateState(3,1);
                ThisOver = ThisOver+"  "+3;
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(3);
                    if(localOver%6 == 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";

                        SelectBowler();
                    }
                }

            }

        });

        Button four_Btn =findViewById(R.id.ButtonRun_four);
        four_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateState(4,1);
                if(strike){
                    BatsmanOnStrike_fours+=1;
                }else{
                    BatsmanNonStrike_fours+=1;
                }
                ThisOver = ThisOver+"  "+4;
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(4);
                    if(localOver%6 == 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";
                        SelectBowler();
                    }
                }

            }

        });

        Button six_Btn =findViewById(R.id.ButtonRun_six);
        six_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateState(6,1);
                if(strike){
                    BatsmanOnStrike_sixes+=1;
                }else{
                    BatsmanNonStrike_sixes+=1;
                }
                ThisOver = ThisOver+"  "+6;
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(6);
                    if(localOver%6 == 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";

                        SelectBowler();
                    }
                }

            }

        });

        Button five_Btn =findViewById(R.id.ButtonRun_five);
        five_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateState(5,1);

                ThisOver = ThisOver+"  "+5;
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(5);
                    if(localOver%6 == 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";

                        SelectBowler();
                    }
                }

            }

        });

        Button dot_Btn =findViewById(R.id.ButtonRun_dot);
        dot_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateState(0,1);
                ThisOver = ThisOver+"  *";
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(0);
                    if(localOver%6 == 0 && localOver != 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";

                        SelectBowler();
                    }
                }

            }

        });

        Button wide_Btn =findViewById(R.id.ButtonRun_wide);
        wide_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText extras = (EditText)findViewById(R.id.Extras);
                String valueofTextField = String.valueOf(extras.getText());
                extras.setText("");
                Integer runs = (valueofTextField.equals(""))? 1 : Integer.parseInt(valueofTextField);
                totalRuns = totalRuns+runs;
                currentOver = currentOver+0;
                try{
                    runRate = totalRuns/(currentOver/6);
                }catch (ArithmeticException e){
                    try{runRate = totalRuns/currentOver;}catch(Exception exp){runRate = totalRuns;};
                }
                CurrentBowler_runs+=runs;
                try{CurrentBowler_economyrate = CurrentBowler_runs/(CurrentBowler_overs/6);}catch(Exception e){};
                ThisOver = ThisOver+"  Wd("+runs+")";
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(0);
                    if(localOver%6 == 0 && localOver != 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";

                        SelectBowler();
                    }
                }

            }

        });

        Button noball_Btn =findViewById(R.id.ButtonRun_noball);
        noball_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText extras = (EditText)findViewById(R.id.Extras);
                String valueofTextField = String.valueOf(extras.getText());
                extras.setText("");
                Integer runs = (valueofTextField.equals(""))? 1 : Integer.parseInt(valueofTextField);

                if(strike == true) {//Decide on strike
                    if(runs != 1){
                        BatsmanOnStrike_runs += runs-1;
                    }
                    BatsmanOnStrike_balls += 0;
                    try{BatsmanOnStrike_strikerate = (BatsmanOnStrike_runs*100) / BatsmanOnStrike_balls;}catch(Exception e){};
                }else{
                    if(runs != 1){
                        BatsmanNonStrike_runs += runs-1;
                    }
                    BatsmanNonStrike_balls += 0;
                    try{BatsmanNonStrike_strikerate = (BatsmanNonStrike_runs*100) / BatsmanNonStrike_balls;}catch(Exception e){};
                }
                totalRuns = totalRuns+runs;
                currentOver = currentOver+0;
                try{
                    runRate = totalRuns/(currentOver/6);
                }catch (ArithmeticException e){
                    try{runRate = totalRuns/currentOver;}catch(Exception exp){runRate = totalRuns;};
                }
                CurrentBowler_runs+=runs;
                try{CurrentBowler_economyrate = CurrentBowler_runs/(CurrentBowler_overs/6);}catch(Exception e){};
                ThisOver = ThisOver+"  NB("+runs+")";

                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(runs-1);
                    if(localOver%6 == 0 && localOver != 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";

                        SelectBowler();
                    }
                }

            }

        });

        Button byes_Btn =findViewById(R.id.ButtonRun_byes);
        byes_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText extras = (EditText)findViewById(R.id.Extras);
                String valueofTextField = String.valueOf(extras.getText());
                extras.setText("");
                Integer runs = (valueofTextField.equals(""))? 1 : Integer.parseInt(valueofTextField);
                totalRuns = totalRuns+runs;
                currentOver = currentOver+1;
                localOver++;
                try{
                    runRate = totalRuns/(currentOver/6);
                }catch (ArithmeticException e){
                    try{runRate = totalRuns/currentOver;}catch(Exception exp){runRate = totalRuns;};
                }
                CurrentBowler_runs+=runs;
                try{CurrentBowler_economyrate = CurrentBowler_runs/(CurrentBowler_overs/6);}catch(Exception e){};
                ThisOver = ThisOver+"  B("+runs+")";

                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(runs);
                    if(localOver%6 == 0 && localOver != 0){
                        ChangeStrike(1);//rotate Strike
                        ThisOver = "";

                        SelectBowler();
                    }
                }

            }

        });

        Button wicket_Btn =findViewById(R.id.ButtonRun_wicket);
        wicket_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText extras = (EditText)findViewById(R.id.Extras);
                String valueofTextField = String.valueOf(extras.getText());
                extras.setText("");
                Integer runs = (valueofTextField.equals(""))? 0 : Integer.parseInt(valueofTextField);
                UpdateState(runs,1);
                CurrentBowler_wickets += 1;
                wicketsOut+=1;
                ThisOver = ThisOver+"  W";
                UpdateFireBase();
                checkIfWon();
                if(CheckIfInningIsOver()){
                    ThisOver = "";
                    strike = true;
                    UpdateFireBase();
                    Changeinning();
                }else{
                    ChangeStrike(runs);
                    if(localOver%6 == 0){
                        ChangeStrike(0);//rotate Strike
                        ThisOver = "";
                        SelectBowler();
                    }
                    SelectBatsman();//SELECT NEW BATSMAN
                }

            }

        });

        Button swap_Btn =findViewById(R.id.swap);
        swap_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeStrike(1);
                UpdateFireBase();
                finalizeUI();
            }
        });
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent summary = new Intent(Manager.this,userScoreCard.class);
                summary.putExtra("KEY",matchKEY);
                startActivity(summary);
            }
        });

    }
    private void Changeinning(){
        if(inning.equals("Inning1")){
            AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
            builder.setCancelable(false);
            builder.setMessage("First Inning Finish! Start Second inning!");
            builder.setPositiveButton("Continue",(DialogInterface.OnClickListener)(dialog,which)->{
                myref.child("State").setValue("premature");
                State = "premature";
                SecondInning();
            } );

            AlertDialog dialog = builder.create();
            dialog.show();

        }else if(inning.equals("Inning2")){
            AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
            builder.setCancelable(false);
            builder.setMessage("Match Finish! Go To Match Summary!");
            builder.setPositiveButton("Continue",(DialogInterface.OnClickListener)(dialog,which)->{
                myref.child("isFinished").setValue(true);
                Intent manager = new Intent(Manager.this,userScoreCard.class);
                manager.putExtra("KEY",matchKEY);
                startActivity(manager);
            } );

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }
    private boolean CheckIfInningIsOver(){
        if((currentOver/6) == Overs || wicketsOut == 10){
            return true;
        }else {
            return false;
        }
    }
    private void UpdateState(int runs,int balls){
        totalRuns = totalRuns+runs;
        if(strike == true) {//Decide on strike
            BatsmanOnStrike_runs += runs;
            BatsmanOnStrike_balls += balls;
            try{BatsmanOnStrike_strikerate = (BatsmanOnStrike_runs*100) / BatsmanOnStrike_balls;}catch(Exception e){};
        }else{
            BatsmanNonStrike_runs += runs;
            BatsmanNonStrike_balls += balls;
            try{BatsmanNonStrike_strikerate = (BatsmanNonStrike_runs*100) / BatsmanNonStrike_balls;}catch(Exception e){};
        }
        currentOver = currentOver+balls;
        localOver+=balls;
        try{
            runRate = totalRuns/(currentOver/6);
        }catch (ArithmeticException e){
            try{runRate = totalRuns/currentOver;}catch(Exception exp){};
        }

        CurrentBowler_runs+=runs;
        CurrentBowler_overs+=balls;
        try{CurrentBowler_economyrate = CurrentBowler_runs/(CurrentBowler_overs/6);}catch(Exception e){};
    }
    private void FirstInning(){
        myref.child("Inning1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    BatsmanOnStrike = task.getResult().child("CurBat1").getValue(String.class);
                    BatsmanNonStrike =  task.getResult().child("CurBat2").getValue(String.class);
                    BatTeam = "Team1";
                    BowlTeam = "Team2";
                    SetUP();
                }
                else {
                    Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG! TRY AGAIN!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void SecondInning(){

        myref.child("Inning2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    BatsmanOnStrike = task.getResult().child("CurBat1").getValue(String.class);
                    BatsmanNonStrike =  task.getResult().child("CurBat2").getValue(String.class);
                    CurrentBowler_runs = 0;
                    CurrentBowler_overs = 0;
                    CurrentBowler_economyrate = 0.0;
                    CurrentBowler_wickets = 0;
                    inning = "Inning2";
                    BatTeam = "Team2";
                    BowlTeam = "Team1";
                    SetUP();
                }
                else {
                    Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG! TRY AGAIN!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void SetUP(){
        myref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    totalRuns = task.getResult().child(inning).child("Totalruns").getValue(Integer.class);

                    currentOver = task.getResult().child(inning).child("CurrentOvers").getValue(Integer.class);

                    wicketsOut = task.getResult().child(inning).child("WicketsOut").getValue(Integer.class);

                    runRate = task.getResult().child("Runrate").getValue(Integer.class);

                    ThisOver = task.getResult().child("thisOver").getValue(String.class);

                    localOver = task.getResult().child("localOver").getValue(Integer.class);

                    strike = task.getResult().child("strike").getValue(Boolean.class);

                    if(inning.equals("Inning2")){
                        FirstInningRuns = task.getResult().child("Inning1").child("Totalruns").getValue(Integer.class);
                    }

                    BatsmanOnStrike_runs = task.getResult().child(BatTeam).child(BatsmanOnStrike).child("Runs").getValue(Integer.class);
                    BatsmanOnStrike_balls = task.getResult().child(BatTeam).child(BatsmanOnStrike).child("Balls").getValue(Integer.class);
                    BatsmanOnStrike_fours = task.getResult().child(BatTeam).child(BatsmanOnStrike).child("Boundaries").getValue(Integer.class);
                    BatsmanOnStrike_sixes = task.getResult().child(BatTeam).child(BatsmanOnStrike).child("Sixes").getValue(Integer.class);
                    BatsmanOnStrike_strikerate = task.getResult().child(BatTeam).child(BatsmanOnStrike).child("StrikeRate").getValue(Integer.class);

                    BatsmanNonStrike_runs = task.getResult().child(BatTeam).child(BatsmanNonStrike).child("Runs").getValue(Integer.class);
                    BatsmanNonStrike_balls = task.getResult().child(BatTeam).child(BatsmanNonStrike).child("Balls").getValue(Integer.class);
                    BatsmanNonStrike_fours = task.getResult().child(BatTeam).child(BatsmanNonStrike).child("Boundaries").getValue(Integer.class);
                    BatsmanNonStrike_sixes = task.getResult().child(BatTeam).child(BatsmanNonStrike).child("Sixes").getValue(Integer.class);
                    BatsmanNonStrike_strikerate = task.getResult().child(BatTeam).child(BatsmanNonStrike).child("StrikeRate").getValue(Integer.class);



                    hostTeam = (ArrayList<String>)task.getResult().child("team1_name").getValue();
                    vistorTeam = (ArrayList<String>)task.getResult().child("team2_name").getValue();


                    pushKeyID_one =(ArrayList<String>) task.getResult().child("team1_keys").getValue();
                    pushKeyID_two =(ArrayList<String>) task.getResult().child("team2_keys").getValue();

                    Temp_key_one = (ArrayList<String>) task.getResult().child("TemporaryData").child("team1_keys").getValue();
                    Temp_key_two = (ArrayList<String>)task.getResult().child("TemporaryData").child("team2_keys").getValue();
                    Temp_name_one = (ArrayList<String>)task.getResult().child("TemporaryData").child("team1").getValue();
                    Temp_name_two =(ArrayList<String>) task.getResult().child("TemporaryData").child("team2").getValue();


                    if(State.equals("premature")){
                        if(inning.equals("Inning1")){
                            int i=0;
                            while(i<2){
                                Temp_key_one.remove(0);
                                Temp_name_one.remove(0);
                                i++;
                            }
                        }else if(inning.equals("Inning2")){
                            int i=0;
                            while(i<2){
                                Temp_key_two.remove(0);
                                Temp_name_two.remove(0);
                                i++;
                            }
                        }
                        if(inning.equals("Inning2")){
                            CurrentBowler = pushKeyID_one.get(10);
                        }
                        SelectBowler();

                    }else{
                        CurrentBowler = task.getResult().child(inning).child("CurBowler").getValue(String.class);
                        CurrentBowler_runs = task.getResult().child(BowlTeam).child(CurrentBowler).child("RunsGiven").getValue(Integer.class);
                        CurrentBowler_overs = task.getResult().child(BowlTeam).child(CurrentBowler).child("Overs").getValue(Integer.class);
                        CurrentBowler_wickets = task.getResult().child(BowlTeam).child(CurrentBowler).child("WicketsTaken").getValue(Integer.class);
                        CurrentBowler_economyrate = task.getResult().child(BowlTeam).child(CurrentBowler).child("EconomyRate").getValue(Double.class);
                    }
                    myref.child("State").setValue("mature");
                    finalizeUI();


                }
                else {
                    Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG! TRY AGAIN!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void ChangeStrike(int runs){
        if(runs%2 != 0){
            if(strike){
                strike = false;
                myref.child("strike").setValue(strike);
            }else{
                strike = true;
                myref.child("strike").setValue(strike);
            }
        }
    }
    private void UpdateFireBase(){

        HashMap<String,Object> inningdata = new HashMap<String,Object>();
        inningdata.put("CurBat1",BatsmanOnStrike);
        inningdata.put("CurBat2",BatsmanNonStrike);
        inningdata.put("CurBowler",CurrentBowler);
        inningdata.put("CurrentOvers",currentOver);
        inningdata.put("Totalruns",totalRuns);
        inningdata.put("WicketsOut",wicketsOut);
        myref.child(inning).setValue(inningdata);


        myref.child("Runrate").setValue(runRate);
        myref.child("inning").setValue(inning);
        myref.child("thisOver").setValue(ThisOver);
        myref.child("localOver").setValue(localOver);
        myref.child("strike").setValue(strike);

        myref.child(BatTeam).child(BatsmanOnStrike).child("Runs").setValue(BatsmanOnStrike_runs);
        myref.child(BatTeam).child(BatsmanOnStrike).child("Balls").setValue(BatsmanOnStrike_balls);
        myref.child(BatTeam).child(BatsmanOnStrike).child("Boundaries").setValue(BatsmanOnStrike_fours);
        myref.child(BatTeam).child(BatsmanOnStrike).child("Sixes").setValue(BatsmanOnStrike_sixes);
        myref.child(BatTeam).child(BatsmanOnStrike).child("StrikeRate").setValue(BatsmanOnStrike_strikerate);

        myref.child(BatTeam).child(BatsmanNonStrike).child("Runs").setValue(BatsmanNonStrike_runs);
        myref.child(BatTeam).child(BatsmanNonStrike).child("Balls").setValue(BatsmanNonStrike_balls);
        myref.child(BatTeam).child(BatsmanNonStrike).child("Boundaries").setValue(BatsmanNonStrike_fours);
        myref.child(BatTeam).child(BatsmanNonStrike).child("Sixes").setValue(BatsmanNonStrike_sixes);
        myref.child(BatTeam).child(BatsmanNonStrike).child("StrikeRate").setValue(BatsmanNonStrike_strikerate);

        myref.child(BowlTeam).child(CurrentBowler).child("RunsGiven").setValue(CurrentBowler_runs);
        myref.child(BowlTeam).child(CurrentBowler).child("WicketsTaken").setValue(CurrentBowler_wickets);
        myref.child(BowlTeam).child(CurrentBowler).child("EconomyRate").setValue(CurrentBowler_economyrate);
        myref.child(BowlTeam).child(CurrentBowler).child("Overs").setValue(CurrentBowler_overs);

        myref.child("TemporaryData").child("team1_keys").setValue(Temp_key_one);
        myref.child("TemporaryData").child("team2_keys").setValue(Temp_key_two);
        myref.child("TemporaryData").child("team1").setValue(Temp_name_one);
        myref.child("TemporaryData").child("team2").setValue(Temp_name_two);


    }
    private void SelectBatsman(){
        if(inning.equals("Inning1")){
            String listItems[] = new String[Temp_name_one.size()];

            for(int i=0;i<Temp_name_one.size();i++)
                listItems[i] = Temp_name_one.get(i);

            int checkedItem = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
            builder.setTitle("CHOOSE NEXT BATSMAN");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(strike)
                        BatsmanOnStrike = Temp_key_one.get(which);
                    else
                        BatsmanNonStrike = Temp_key_one.get(which);

                    Temp_name_one.remove(which);
                    Temp_key_one.remove(which);
                    UpdateBatsmensState();
                    dialog.dismiss();
                }


            });
            builder.show();

        }else{
            String listItems[] = new String[Temp_name_two.size()];

            for(int i=0;i<Temp_name_two.size();i++)
                listItems[i] = Temp_name_two.get(i);

            int checkedItem = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
            builder.setTitle("CHOOSE NEXT BATSMAN");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("gg",Temp_key_two.get(which));
                    if(strike)
                        BatsmanOnStrike = Temp_key_two.get(which);
                    else
                        BatsmanNonStrike = Temp_key_two.get(which);

                    Temp_name_two.remove(which);
                    Temp_key_two.remove(which);
                    UpdateBatsmensState();
                    dialog.dismiss();
                }


            });
            builder.show();

        }
    }
    private void SelectBowler(){
        if(inning.equals("Inning2")){
            String listItems[] = new String[11];
            for(int i=0;i<11;i++)
                listItems[i] = hostTeam.get(i);

            int checkedItem = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
            builder.setTitle("CHOOSE NEXT BOWLER");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CurrentBowler = pushKeyID_one.get(which);
                    UpdateBowlerStatusFromFireBase();
                    dialog.dismiss();
                }


            });
            builder.show();

        }else{
            String listItems[] = new String[11];

            for(int i=0;i<11;i++)
                listItems[i] = vistorTeam.get(i);

            int checkedItem = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
            builder.setTitle("CHOOSE NEXT BOWLER");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CurrentBowler = pushKeyID_two.get(which);
                    UpdateBowlerStatusFromFireBase();
                    dialog.dismiss();
                }


            });
            builder.show();
        }
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
        total_Over = (TextView) findViewById((R.id.totalOver));
        toss_won = (TextView) findViewById((R.id.tosswon));
        toss_Opt = (TextView) findViewById((R.id.tossOpt));
        summary = findViewById(R.id.submit);

    }
    public void finalizeUI(){
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    String p1 = dataSnapshot.child(inning).child("CurBat1").getValue(String.class);
                    String p2 = dataSnapshot.child(inning).child("CurBat2").getValue(String.class);

                    String p3 = dataSnapshot.child(inning).child("Totalruns").getValue(Integer.class).toString();
                    String p4 = dataSnapshot.child(inning).child("WicketsOut").getValue(Integer.class).toString();
                    Integer p5 = dataSnapshot.child(inning).child("CurrentOvers").getValue(Integer.class);
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
    private void UpdateBowlerStatusFromFireBase(){
        myref.child(BowlTeam).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    CurrentBowler_runs = task.getResult().child(CurrentBowler).child("RunsGiven").getValue(Integer.class);
                    CurrentBowler_overs = task.getResult().child(CurrentBowler).child("Overs").getValue(Integer.class);
                    CurrentBowler_wickets = task.getResult().child(CurrentBowler).child("WicketsTaken").getValue(Integer.class);
                    CurrentBowler_economyrate = task.getResult().child(CurrentBowler).child("EconomyRate").getValue(Double.class);
                    UpdateFireBase();   //Save changes in firebase
                    localOver = 0;
                }
                else {
                    Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG! TRY AGAIN!",Toast.LENGTH_LONG).show();
                }

            }
        });



    }
    private void UpdateBatsmensState(){
        if(strike){
            BatsmanOnStrike_runs = 0;
            BatsmanOnStrike_balls = 0;
            BatsmanOnStrike_fours = 0;
            BatsmanOnStrike_sixes = 0;
            BatsmanOnStrike_strikerate = 0;

        }else{
            BatsmanNonStrike_runs = 0;
            BatsmanNonStrike_balls = 0;
            BatsmanNonStrike_fours = 0;
            BatsmanNonStrike_sixes = 0;
            BatsmanNonStrike_strikerate = 0;

        }
        UpdateFireBase(); //Save Changes in firebase
    }
    private void getInning(){
        myref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().child("isFinished").getValue(Boolean.class)){

                    }else{
                        inning = task.getResult().child("inning").getValue(String.class);
                        Overs = task.getResult().child("Overs").getValue(Integer.class);
                        State = task.getResult().child("State").getValue(String.class);
                        if(inning.equals("Inning1")){
                            FirstInning();//START/INITIALIZE THE FIRST ACTIVITY
                        }else if(inning.equals("Inning2")){
                            SecondInning();////START/INITIALIZE THE SECOND ACTIVITY
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG! TRY AGAIN!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void checkIfWon(){
        if(inning.equals("Inning2")){
            int host_runs = FirstInningRuns+1;
            int vistor_runs = totalRuns;
            if(vistor_runs >= host_runs){
                AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
                builder.setCancelable(false);
                builder.setMessage("Match Finish! Go To Match Summary!");
                builder.setPositiveButton("Continue",(DialogInterface.OnClickListener)(dialog,which)->{
                    myref.child("isFinished").setValue(true);
                    Intent manager = new Intent(Manager.this,userScoreCard.class);
                    manager.putExtra("KEY",matchKEY);
                    startActivity(manager);
                } );

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to go to Home?");
        builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which)->{
            Intent intent = new Intent(Manager.this,HomeMenu.class);
            startActivity(intent);
        });
        builder.setNegativeButton("No",(DialogInterface.OnClickListener)(dialog, which)->{
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
