package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapterII adapter;
    ArrayList<ModelII> listMatch;
    private ProgressDialog progressDialog;

    DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_history);


        recyclerView = findViewById(R.id.matchList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listMatch = new ArrayList<>();
        adapter = new MyAdapterII(this ,listMatch );
        recyclerView.setAdapter(adapter);

        BatSummary();

    }

    private void BatSummary(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();//get current user
        if(thisuser != null){
            myref.child("Users").child(thisuser.getUid()).child("matches").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChildren()){
                        ArrayList<String> mymatches = new ArrayList<String>();
                        mymatches.clear();
                        mymatches = (ArrayList<String>) snapshot.getValue();
                        Collections.reverse(mymatches);
                        PrintMatches(mymatches);

                    }else{
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
                        builder.setCancelable(true);
                        builder.setMessage("You don't have created any match yet!");
                        builder.setPositiveButton("OK",(DialogInterface.OnClickListener)(dialog, which)->{
                            dialog.cancel();
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        listMatch.clear();
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void PrintMatches(ArrayList<String> mymatches) {
        listMatch.clear();
        myref.child("Scorify").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                for(int i=0;i<mymatches.size();i++) {
                    ModelII model = new ModelII();
                    model.MatchId = mymatches.get(i);
                    model.TeamNames = snapshot.child(mymatches.get(i)).child("HostTeamName").getValue(String.class) + " VS " + snapshot.child(mymatches.get(i)).child("VistorTeamName").getValue(String.class);
                    listMatch.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
