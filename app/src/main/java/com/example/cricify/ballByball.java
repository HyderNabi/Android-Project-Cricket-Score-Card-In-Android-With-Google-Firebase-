package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ballByball extends AppCompatActivity {
    private TextView FTeam,STeam;
    private DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Scorify");
    private Intent i;
    private String matchKEY;

    private RecyclerView myFRecyclerView,mySRecyclerView;
    private MyOverAdapter F_adapter,S_adapter;
    private ArrayList<String> F_overlist,S_overlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_ball_byball);

        i = getIntent();
        matchKEY = i.getStringExtra("KEY");
        myref = myref.child(matchKEY);


        myFRecyclerView = findViewById(R.id.recycler_view);
        mySRecyclerView = findViewById(R.id.recycler_view2);

        myFRecyclerView.setHasFixedSize(true);
        mySRecyclerView.setHasFixedSize(true);

        myFRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mySRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        F_overlist = new ArrayList<>();
        S_overlist = new ArrayList<>();

        F_adapter = new MyOverAdapter(this ,F_overlist);
        S_adapter = new MyOverAdapter(this ,S_overlist);

        myFRecyclerView.setAdapter(F_adapter);
        mySRecyclerView.setAdapter(S_adapter);

        initUI();
        process();

    }

    private void process() {
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    FTeam.setText(snapshot.child("HostTeamName").getValue(String.class));
                    STeam.setText(snapshot.child("VistorTeamName").getValue(String.class));

                    F_overlist.clear();
                    S_overlist.clear();

                    for(DataSnapshot item:snapshot.child("Inning1").child("ballByBall").getChildren()){
                        F_overlist.add(item.getValue(String.class));
                    }
                    for(DataSnapshot item:snapshot.child("Inning2").child("ballByBall").getChildren()){
                        S_overlist.add(item.getValue(String.class));
                    }
                    F_adapter.notifyDataSetChanged();
                    S_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUI(){
        FTeam = (TextView) findViewById(R.id.FTname);
        STeam = (TextView) findViewById(R.id.STname);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}