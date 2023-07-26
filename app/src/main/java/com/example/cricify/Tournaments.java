package com.example.cricify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Tournaments extends AppCompatActivity {
    private CardView newTour,allTour,myTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_tournaments);

        newTour = findViewById(R.id.newTour);
        allTour = findViewById(R.id.allTour);
        myTour = findViewById(R.id.myTour);


        newTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createTour = new Intent(Tournaments.this,CreateTournament.class);
                startActivity(createTour);
            }
        });
    }
}