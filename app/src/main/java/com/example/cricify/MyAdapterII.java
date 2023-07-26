package com.example.cricify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapterII extends RecyclerView.Adapter<MyAdapterII.myAnotherViewHolder> {

    ArrayList<ModelII> mList;
    Context context;
    DatabaseReference myref;

    public MyAdapterII(Context context , ArrayList<ModelII> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public myAnotherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemmatch , parent ,false);
        return new myAnotherViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myAnotherViewHolder holder, int position) {
        ModelII model = mList.get(position);
        holder.teamNames.setText(model.getTeamNames());
        holder.matchid.setText(model.getMatchId());
        StatusPanel(holder,model);
    }

    private void StatusPanel(myAnotherViewHolder holder ,ModelII model){
       myref = FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Scorify").child(model.getMatchId());
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    holder.date.setText("Date : "+snapshot.child("Date").getValue(String.class));
                    holder.time.setText("Time : "+snapshot.child("Time").getValue(String.class));
                    int first_inning_runs = snapshot.child("Inning1").child("Totalruns").getValue(Integer.class);
                    int second_inning_runs = snapshot.child("Inning2").child("Totalruns").getValue(Integer.class);
                    int wicketsOUt = snapshot.child("Inning2").child("WicketsOut").getValue(Integer.class);
                    int currentOver= snapshot.child("Inning2").child("CurrentOvers").getValue(Integer.class);
                    int total_overs = snapshot.child("Overs").getValue(Integer.class);
                    String vistorTeam = snapshot.child("VistorTeamName").getValue(String.class);
                    String hostTeam = snapshot.child("HostTeamName").getValue(String.class);

                    if(snapshot.child("isFinished").getValue(Boolean.class)){
                        holder.matchStatus.setText("STATUS : CLOSED");
                        if(holder.wonbyStatus.getVisibility() == View.GONE){holder.wonbyStatus.setVisibility(View.VISIBLE);}
                        if(second_inning_runs>=(first_inning_runs+1)){
                            holder.wonbyStatus.setText("Match Won By "+vistorTeam+" By "+(10-wicketsOUt)+ " Wickets");
                        }else if(second_inning_runs == first_inning_runs){
                            holder.wonbyStatus.setText("Draw Match");
                        }else{
                            holder.wonbyStatus.setText("Match Won By "+hostTeam+" By "+((first_inning_runs+1)-second_inning_runs)+" Runs");
                        }
                    }else{
                        holder.matchStatus.setText("STATUS : OPEN");
                        if(snapshot.child("inning").getValue(String.class).equals("Inning2")){
                            if(holder.wonbyStatus.getVisibility() == View.GONE){holder.wonbyStatus.setVisibility(View.VISIBLE);}
                            holder.wonbyStatus.setText(((first_inning_runs+1)-second_inning_runs)+" Runs Needed By "+((total_overs*6)-currentOver)+" Balls");
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
    public int getItemCount() {
        return mList.size();
    }

    public static class myAnotherViewHolder extends RecyclerView.ViewHolder{
        TextView teamNames,matchid,matchStatus,wonbyStatus,time ,date;
        ImageButton check_details,delete_match;
        DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();


        public myAnotherViewHolder(@NonNull View itemView) {
            super(itemView);

            teamNames = itemView.findViewById(R.id.teamNames);
            matchid = itemView.findViewById(R.id.matchid);
            //buttons
            check_details = itemView.findViewById(R.id.check_details);
            delete_match = itemView.findViewById(R.id.delete_match);
            matchStatus = itemView.findViewById(R.id.matchStatus);
            wonbyStatus = itemView.findViewById(R.id.wonbyStatus);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);


            delete_match.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setCancelable(true);
                    builder.setMessage("Are You Sure You Want To Delete Match?");
                    builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which)->{
                        DatabaseReference myLref = myref;
                        myref.child("Users").child(thisuser.getUid()).child("matches").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.hasChildren()){
                                    ArrayList<String> mymatches = new ArrayList<String>();
                                    mymatches.clear();
                                    mymatches = (ArrayList<String>) snapshot.getValue();
                                    mymatches.remove(matchid.getText().toString());
                                    myLref.child("Scorify").child(matchid.getText().toString()).removeValue();
                                    myref.child("Users").child(thisuser.getUid()).child("matches").setValue(mymatches);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } );

                    builder.setNegativeButton("No",(DialogInterface.OnClickListener) (dialog,which)->{
                        dialog.cancel();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            check_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent manager = new Intent(view.getContext(),Manager.class);
                    manager.putExtra("KEY",matchid.getText().toString());
                    view.getContext().startActivity(manager);

                }
            });

        }

    }
}