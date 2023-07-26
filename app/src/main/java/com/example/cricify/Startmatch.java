package com.example.cricify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Startmatch extends AppCompatActivity {
    static int toss=1;
    static int opted=1;
    static String bat="BAT";
    static String bowl="BOWL";

    EditText Host_TeamName,Vistor_TeamName,overs;
    ImageButton submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_startmatch);

        submit = findViewById(R.id.ButtonStartA);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitData();
            }
        });


    }
    public void SubmitData()
    {
        Host_TeamName = (EditText) findViewById(R.id.host_team_name);
        Vistor_TeamName = (EditText) findViewById(R.id.vistor_team_name);
        overs = (EditText) findViewById(R.id.overs);
        if(TextUtils.isEmpty(Host_TeamName.getText().toString()) ||  TextUtils.isEmpty(Vistor_TeamName.getText().toString())  || TextUtils.isEmpty(overs.getText().toString()))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Startmatch.this);
            builder.setCancelable(true);
            builder.setMessage("INPUT FIELDS CAN'T BE EMPTY !");
            builder.setPositiveButton("OK",(DialogInterface.OnClickListener)(dialog, which)->{
                dialog.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Intent playerIntent=new Intent(this,Playername.class);

            String t=Integer.toString(toss);
            String to=Integer.toString(opted);

            if(toss==1&&opted==1 || toss==0&&opted==0)
            {
                playerIntent.putExtra("HostTeam", Host_TeamName.getText().toString());
                playerIntent.putExtra("VistorTeam", Vistor_TeamName.getText().toString());

            }
            else if(toss==1&&opted==0 || toss==0&&opted==1)
            {
                playerIntent.putExtra("HostTeam", Vistor_TeamName.getText().toString());
                playerIntent.putExtra("VistorTeam", Host_TeamName.getText().toString());
            }


            if(toss==1&&opted==1)
            {
                playerIntent.putExtra("TOSS", Host_TeamName.getText().toString());
                playerIntent.putExtra("OPT",bat);
            }
            else if(toss==1&&opted==0)
            {
                playerIntent.putExtra("TOSS", Host_TeamName.getText().toString());
                playerIntent.putExtra("OPT",bowl);
            }
            else if( toss==0&&opted==1)
            {
                playerIntent.putExtra("TOSS", Vistor_TeamName.getText().toString());
                playerIntent.putExtra("OPT",bat);
            }
            else if(toss==0&&opted==0)
            {
                playerIntent.putExtra("TOSS", Vistor_TeamName.getText().toString());
                playerIntent.putExtra("OPT",bowl);
            }

            playerIntent.putExtra("Overs", overs.getText().toString());
            playerIntent.putExtra("_Toss", Integer.toString(toss));
            playerIntent.putExtra("_Opt", Integer.toString(opted));
            startActivity(playerIntent);
        }



    }
    public void onRadioButtonClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rBtnHostTeam:
                if (checked)
                    // Pirates are the best
                    toss = 1;
                break;
            case R.id.rBtnVistorTeam:
                if (checked)
                    // Ninjas rule
                    toss = 0;
                break;
        }

    }
    public void onRadioButtonClicked1(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rBtnBat:
                if (checked)
                    // Pirates are the best
                    opted = 1;
                break;
            case R.id.rBtnBowl:
                if (checked)
                    // Ninjas rule
                    opted= 0;
                break;
        }

    }


}
