package com.example.cricify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class myAnotherViewHolder extends RecyclerView.ViewHolder{
        TextView teamNames,matchid;
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