package com.example.cricify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeMenu extends AppCompatActivity {
    CardView newMatch,match_id,myMatches,liveScore;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_homepage);

        newMatch= findViewById(R.id.newMatch);
        match_id= findViewById(R.id.match_id);
        myMatches= findViewById(R.id.myMatches);
        liveScore= findViewById(R.id.liveScore);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeMenu.this);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to Log Out?");
                builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which)->{

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        auth.signOut();
                        Intent intent = new Intent(HomeMenu.this,MainActivity.class);
                        startActivity(intent);
                    }

                });
                builder.setNegativeButton("No",(DialogInterface.OnClickListener)(dialog, which)->{
                    dialog.cancel();
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        newMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StartMatchintent=new Intent(HomeMenu.this,Startmatch.class);
                startActivity(StartMatchintent);
            }
        });
        match_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent matchcodeintent=new Intent(HomeMenu.this, MatchCode.class);
                startActivity(matchcodeintent);
            }
        });
        myMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StartMatchintent=new Intent(HomeMenu.this,History.class);
                startActivity(StartMatchintent);
            }
        });
        liveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StartMatchintent=new Intent(HomeMenu.this,LiveScore.class);
                startActivity(StartMatchintent);
            }
        });



    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeMenu.this);
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which)->{
            finishAffinity();
            finish();
        });
        builder.setNegativeButton("No",(DialogInterface.OnClickListener)(dialog, which)->{
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}

