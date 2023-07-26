package com.example.cricify;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
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

    private Button generatePDF,ballByball;

    TextView First_BatsmanName,Second_BatsmanName,Runs,Wickets_Out,Current_Over,extras,fow;
    TextView Run_Rate,Current_Bowler,Inning_Status,Runs_OnStrike,Runs_NOnStrike;
    TextView Balls_NOnStrike,Balls_OnStrike,SixesNonStrike,SixesonStrike,FoursNonStrike,FoursonStrike;
    TextView BowEconomy,SROnStrike, SRNOnStrike, BowOvers,BowRuns,BowWickets,thisOver;
    TextView strikeCurA, strikeCurB,TeamName,matchStatus,wonBy,runsNeeded,toss_Opt,toss_won,total_Over,playedBW;
    private TextView played_teamName,played_score,played_overs,played_extras,played_fow,date,time,yettobat;
    private RelativeLayout Played_panel;


    Intent i;


    DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Scorify");

    private String FilePath;
    File file;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            FilePath = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Cricbook/";
        }
        else
        {

            FilePath = Environment.getExternalStorageDirectory().getPath()+"/Cricbook/";
        }
        file = new File(FilePath);
        file.mkdirs();
        file = new File(file.toString(),matchKEY+".pdf");

        ballByball = findViewById(R.id.ballByball);
        ballByball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userScoreCard.this, ballByball.class);
                intent.putExtra("KEY",matchKEY);
                startActivity(intent);
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePDF() {


        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1100,800,1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();

                Paint paint = new Paint();

                int x = 0;
                int y = 0;
                canvas.drawText(wonBy.getText().toString(), x, y+= 15, paint);
                canvas.drawText(date.getText().toString(), x, y+= 15, paint);
                canvas.drawText(time.getText().toString(), x, y+= 15, paint);
                canvas.drawText("Played Between : " + playedBW.getText().toString(), x, y+= 15, paint);
                canvas.drawText("Toss Won by : "+toss_won.getText().toString(),x,y+=15,paint);
                canvas.drawText("Opted To : "+toss_Opt.getText().toString(),x,y+=15,paint);
                canvas.drawText("Total Overs : "+total_Over.getText().toString(),x,y+=15,paint);

                canvas.drawText("Ist Inning Details : ",x,y+=15,paint);
                x+=10;
                canvas.drawText("Team Name : "+snapshot.child("HostTeamName").getValue(String.class),x,y+=15,paint);
                canvas.drawText("Runs : "+snapshot.child("Inning1").child("Totalruns").getValue(Integer.class),x,y+=15,paint);
                canvas.drawText("Wickets : "+snapshot.child("Inning1").child("WicketsOut").getValue(Integer.class),x,y+=15,paint);
                canvas.drawText("Overs : "+FormatOvers(snapshot.child("Inning1").child("CurrentOvers").getValue(Integer.class)),x,y+=15,paint);
                canvas.drawText("Extras : "+snapshot.child("Inning1").child("Extras").getValue(String.class),x,y+=15,paint);
                canvas.drawText("FallOfWickets : "+snapshot.child("Inning1").child("FallOfWicket").getValue(String.class),x,y+=15,paint);


                x-=10;

                canvas.drawText("2nd Inning Details : ",x,y+=15,paint);
                x+=10;
                canvas.drawText("Team Name : "+snapshot.child("VistorTeamName").getValue(String.class),x,y+=15,paint);
                canvas.drawText("Runs : "+snapshot.child("Inning2").child("Totalruns").getValue(Integer.class),x,y+=15,paint);
                canvas.drawText("Wickets : "+snapshot.child("Inning2").child("WicketsOut").getValue(Integer.class),x,y+=15,paint);
                canvas.drawText("Overs : "+FormatOvers(snapshot.child("Inning2").child("CurrentOvers").getValue(Integer.class)),x,y+=15,paint);
                canvas.drawText("Extras : "+snapshot.child("Inning2").child("Extras").getValue(String.class),x,y+=15,paint);
                canvas.drawText("FallOfWickets : "+snapshot.child("Inning2").child("FallOfWicket").getValue(String.class),x,y+=15,paint);

                x-=10;

                canvas.drawText("1st Team Summary : ",x,y+=15,paint);
                x+=10;

                ArrayList<Model> teamone = new ArrayList<Model>();
                for(DataSnapshot snap : snapshot.child("Team1").getChildren()) {
                    Model model1 = snap.getValue(Model.class);
                    teamone.add(model1);
                }

                ArrayList<Model> teamtwo = new ArrayList<Model>();
                for(DataSnapshot snap : snapshot.child("Team2").getChildren()) {
                    Model model1 = snap.getValue(Model.class);
                    teamtwo.add(model1);
                }

                for(int i=0;i<teamone.size();i++){
                    Model model= teamone.get(i);
                    if(!model.getTakenBy().equals("")){
                        canvas.drawText(model.getName()+" =  [Status : "+model.getStatus()+"]   [Runs : "+model.getRuns()+"]   [Balls : "+model.getBalls()+"]   [Fours : "+model.getBoundaries()+"]   [Sixes : "+model.getSixes()+"]   [Strike Rate : "+model.getStrikeRate()+"]   [Overs : "+model.getOvers()+"]   [Runs Given : "+model.getRunsGiven()+"]   [Wickets : "+model.getWicketsTaken()+"]   [Economy Rate : "+model.getEconomyRate()+"]   [TakenBy : "+model.getTakenBy()+"]",x,y+=20,paint);

                    }else{
                        canvas.drawText(model.getName()+" =  [Status : "+model.getStatus()+"]   [Runs : "+model.getRuns()+"]   [Balls : "+model.getBalls()+"]   [Fours : "+model.getBoundaries()+"]   [Sixes : "+model.getSixes()+"]   [Strike Rate : "+model.getStrikeRate()+"]   [Overs : "+model.getOvers()+"]   [Runs Given : "+model.getRunsGiven()+"]   [Wickets : "+model.getWicketsTaken()+"]   [Economy Rate : "+model.getEconomyRate()+"]",x,y+=20,paint);
                    }

                }

                x-=10;

                canvas.drawText("2nd Team Summary : ",x,y+=15,paint);
                x+=10;

                for(int i=0;i<teamtwo.size();i++) {
                    Model model = teamtwo.get(i);
                    if(!model.getTakenBy().equals("")){
                        canvas.drawText(model.getName()+" =  [Status : "+model.getStatus()+"]   [Runs : "+model.getRuns()+"]   [Balls : "+model.getBalls()+"]   [Fours : "+model.getBoundaries()+"]   [Sixes : "+model.getSixes()+"]   [Strike Rate : "+model.getStrikeRate()+"]   [Overs : "+model.getOvers()+"]   [Runs Given : "+model.getRunsGiven()+"]   [Wickets : "+model.getWicketsTaken()+"]   [Economy Rate : "+model.getEconomyRate()+"]   [TakenBy : "+model.getTakenBy()+"]",x,y+=20,paint);

                    }else{
                        canvas.drawText(model.getName()+" =  [Status : "+model.getStatus()+"]   [Runs : "+model.getRuns()+"]   [Balls : "+model.getBalls()+"]   [Fours : "+model.getBoundaries()+"]   [Sixes : "+model.getSixes()+"]   [Strike Rate : "+model.getStrikeRate()+"]   [Overs : "+model.getOvers()+"]   [Runs Given : "+model.getRunsGiven()+"]   [Wickets : "+model.getWicketsTaken()+"]   [Economy Rate : "+model.getEconomyRate()+"]",x,y+=20,paint);
                    }
                }

                pdfDocument.finishPage(page);
                PdfDocument.Page page1 = pdfDocument.startPage(pageInfo);
                Canvas canvas1 = page1.getCanvas();

                x = 0;
                y = 0;
                ArrayList<String> ballbyball_firstinning = (ArrayList<String>)snapshot.child("Inning1").child("ballByBall").getValue();
                ArrayList<String> ballbyball_secondinning = (ArrayList<String>)snapshot.child("Inning2").child("ballByBall").getValue();

                canvas1.drawText("First Inning Score Over by Over",x,y+=15,paint);
                x+=10;
                int i =  1;
                for(String myover:ballbyball_firstinning){
                    canvas1.drawText("Over "+i+++": "+myover,x,y+=15,paint);
                }
                pdfDocument.finishPage(page1);

                PdfDocument.Page page2 = pdfDocument.startPage(pageInfo);
                Canvas canvas2 = page2.getCanvas();

                x = 0;
                y = 0;
                canvas2.drawText("Second Inning Score Over by Over",x,y+=15,paint);
                x+=10;
                i =  1;
                for(String myover:ballbyball_secondinning){
                    canvas2.drawText("Over "+i+++": "+myover,x,y+=15,paint);
                }
                pdfDocument.finishPage(page2);

                try{
                    pdfDocument.writeTo((new FileOutputStream(file)));
                    Toast.makeText(getApplicationContext(), ""+FilePath+"/"+matchKEY, Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to generate", Toast.LENGTH_LONG).show();
                    Log.e("mes",""+e.getMessage());
                }
                pdfDocument.close();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }


    public void initializeUI(){
        First_BatsmanName = (TextView) findViewById(R.id.FBatName);
        Second_BatsmanName = (TextView) findViewById(R.id.SBatName);
        Runs = (TextView)findViewById(R.id.Runs);
        extras = (TextView) findViewById(R.id.extras);
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

        played_teamName = (TextView) findViewById(R.id.Played_TeamName) ;
        played_score = (TextView) findViewById(R.id.Played_score) ;
        played_overs = (TextView) findViewById(R.id.Played_overs) ;
        played_extras = (TextView) findViewById(R.id.Played_extras) ;
        played_fow = (TextView) findViewById(R.id.Played_fow);

        yettobat = (TextView)findViewById(R.id.yettobat);

        fow = (TextView)findViewById(R.id.fow);

        date = findViewById(R.id.Date);
        time = findViewById(R.id.Time);

        Played_panel = findViewById(R.id.Played_panel);

        generatePDF = findViewById(R.id.generatePDF);


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
                    date.setText("Date : "+dataSnapshot.child("Date").getValue(String.class));
                    time.setText("Time : "+dataSnapshot.child("Time").getValue(String.class));

                    String p1 = dataSnapshot.child(inning).child("CurBat1").getValue(String.class);
                    String p2 = dataSnapshot.child(inning).child("CurBat2").getValue(String.class);

                    String p3 = dataSnapshot.child(inning).child("Totalruns").getValue(Integer.class).toString();
                    String p4 = dataSnapshot.child(inning).child("WicketsOut").getValue(Integer.class).toString();

                    extras.setText(dataSnapshot.child(inning).child("Extras").getValue(String.class));
                    fow.setText(dataSnapshot.child(inning).child("FallOfWicket").getValue(String.class).toString());

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
                        try{

                            ArrayList<String> Temp_name_one = (ArrayList<String>)dataSnapshot.child("TemporaryData").child("team1").getValue();
                            String ytb = "";
                            for(int i = 0;i<Temp_name_one.size();i++){
                                ytb += (i+1)+".  "+Temp_name_one.get(i)+"\n";
                            }
                            yettobat.setText(ytb);
                        }catch(Exception e){
                            yettobat.setText("");
                        }

                    }else {
                        Inning_Status.setText("2nd Inning");
                        TeamName.setText(dataSnapshot.child("VistorTeamName").getValue(String.class).toString());
                        try{
                            ArrayList<String> Temp_name_two = (ArrayList<String>)dataSnapshot.child("TemporaryData").child("team2").getValue();
                            String ytb = "";
                            for(int i = 0;i<Temp_name_two.size();i++){
                                ytb += (i+1)+".  "+Temp_name_two.get(i)+"\n";
                            }
                            yettobat.setText(ytb);
                        }catch (Exception e){
                            yettobat.setText("");
                        }




                        if(Played_panel.getVisibility() == View.GONE){
                            Played_panel.setVisibility(View.VISIBLE);
                        }

                        played_teamName.setText(dataSnapshot.child("HostTeamName").getValue(String.class).toString());
                        played_score.setText(dataSnapshot.child("Inning1").child("Totalruns").getValue(Integer.class).toString()+"/"+dataSnapshot.child("Inning1").child("WicketsOut").getValue(Integer.class).toString());
                        played_overs.setText(FormatOvers(dataSnapshot.child("Inning1").child("CurrentOvers").getValue(Integer.class))+"/"+dataSnapshot.child("Overs").getValue(Integer.class).toString());
                        played_extras.setText(dataSnapshot.child("Inning1").child("Extras").getValue(String.class));
                        played_fow.setText(dataSnapshot.child("Inning1").child("FallOfWicket").getValue(String.class).toString());

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
                try{
                    int first_inning_runs = snapshot.child("Inning1").child("Totalruns").getValue(Integer.class);
                    int second_inning_runs = snapshot.child("Inning2").child("Totalruns").getValue(Integer.class);
                    int total_overs = snapshot.child("Overs").getValue(Integer.class);

                    if(snapshot.child("isFinished").getValue(Boolean.class)){
                        generatePDF.setVisibility(View.VISIBLE);
                        generatePDF.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(userScoreCard.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                                AlertDialog.Builder builder = new AlertDialog.Builder(userScoreCard.this);
                                builder.setCancelable(true);
                                builder.setMessage("Are you sure you want to download PDF?");
                                builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which)->{
                                    generatePDF();
                                });
                                builder.setNegativeButton("No",(DialogInterface.OnClickListener)(dialog, which)->{
                                    dialog.cancel();
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        });
                        matchStatus.setText("CLOSED");
                        runsNeeded.setVisibility(View.GONE);
                        if(wonBy.getVisibility() == View.GONE){wonBy.setVisibility(View.VISIBLE);}
                        if(second_inning_runs>=(first_inning_runs+1)){
                            wonBy.setText("Match Won By "+vistorTeam+" By "+(10-wicketsOUt)+ " Wickets");
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

                }catch(Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        this.finish();
    }
}
