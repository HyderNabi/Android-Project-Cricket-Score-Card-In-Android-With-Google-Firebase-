package com.example.cricify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Playername extends AppCompatActivity {
    static String hostname;
    static String vistorname;
    static String overs;
    static int toss=0;
    static int opted=0;
    static String passtoss;
    static String passOpted;

    ImageButton submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_playername);

        submit = findViewById(R.id.ButtonLoginP);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDetails();
            }
        });
    }
    public void sendDetails()
    {
        EditText eT_hostP1=(EditText) findViewById(R.id.eVHostP1P);
        EditText eT_hostP2=(EditText) findViewById(R.id.eVHostP2P);
        EditText eT_hostP3=(EditText) findViewById(R.id.eVHostP3P);
        EditText eT_hostP4=(EditText) findViewById(R.id.eVHostP4P);
        EditText eT_hostP5=(EditText) findViewById(R.id.eVHostP5P);
        EditText eT_hostP6=(EditText) findViewById(R.id.eVHostP6P);
        EditText eT_hostP7=(EditText) findViewById(R.id.eVHostP7P);
        EditText eT_hostP8=(EditText) findViewById(R.id.eVHostP8P);
        EditText eT_hostP9=(EditText) findViewById(R.id.eVHostP9P);
        EditText eT_hostP10=(EditText) findViewById(R.id.eVHostP10P);
        EditText eT_hostP11=(EditText) findViewById(R.id.eVHostP11P);

        EditText eT_vistP1=(EditText) findViewById(R.id.eVVistP1P);
        EditText eT_vistP2=(EditText) findViewById(R.id.eVVistP2P);
        EditText eT_vistP3=(EditText) findViewById(R.id.eVVisitP3P);
        EditText eT_vistP4=(EditText) findViewById(R.id.eVVistP4P);
        EditText eT_vistP5=(EditText) findViewById(R.id.eVVistP5P);
        EditText eT_vistP6=(EditText) findViewById(R.id.eVVistP6P);
        EditText eT_vistP7=(EditText) findViewById(R.id.eVVistP7P);
        EditText eT_vistP8=(EditText) findViewById(R.id.eVVistP8P);
        EditText eT_vistP9=(EditText) findViewById(R.id.eVVistP9P);
        EditText eT_vistP10=(EditText) findViewById(R.id.eVVistP10P);
        EditText eT_vistP11=(EditText) findViewById(R.id.eVVistP11P);

        Intent i=getIntent();

        hostname=i.getStringExtra("HostTeam");
        vistorname=i.getStringExtra("VistorTeam");
        overs=i.getStringExtra("Overs");
        passtoss=i.getStringExtra("TOSS");
        passOpted=i.getStringExtra("OPT");

        toss = Integer.parseInt(i.getStringExtra("_Toss"));
        opted = Integer.parseInt(i.getStringExtra("_Opt"));

        if(TextUtils.isEmpty(eT_hostP1.getText().toString()) || TextUtils.isEmpty(eT_hostP2.getText().toString()) || TextUtils.isEmpty(eT_hostP3.getText().toString()) || TextUtils.isEmpty(eT_hostP4.getText().toString())
        || TextUtils.isEmpty(eT_hostP5.getText().toString()) || TextUtils.isEmpty(eT_hostP6.getText().toString()) || TextUtils.isEmpty(eT_hostP7.getText().toString()) || TextUtils.isEmpty(eT_hostP8.getText().toString())
        || TextUtils.isEmpty(eT_hostP9.getText().toString()) || TextUtils.isEmpty(eT_hostP10.getText().toString()) || TextUtils.isEmpty(eT_hostP11.getText().toString())
                ||
                TextUtils.isEmpty(eT_vistP1.getText().toString()) || TextUtils.isEmpty(eT_vistP2.getText().toString()) || TextUtils.isEmpty(eT_vistP3.getText().toString()) || TextUtils.isEmpty(eT_vistP4.getText().toString())
                || TextUtils.isEmpty(eT_vistP5.getText().toString()) || TextUtils.isEmpty(eT_vistP6.getText().toString()) || TextUtils.isEmpty(eT_vistP7.getText().toString()) || TextUtils.isEmpty(eT_vistP8.getText().toString())
                || TextUtils.isEmpty(eT_vistP9.getText().toString()) || TextUtils.isEmpty(eT_vistP10.getText().toString()) || TextUtils.isEmpty(eT_vistP11.getText().toString())

        ){

            Toast.makeText(getApplicationContext(),"INPUT FIELDS CAN'T BE EMPTY !",Toast.LENGTH_SHORT).show();

        }else{
            Intent adminintent1=new Intent(this,CreateMatch.class);

            adminintent1.putExtra("HostTeam", hostname);
            adminintent1.putExtra("VisitorTeam", vistorname);
            adminintent1.putExtra("Overs", overs);
            adminintent1.putExtra("TOSS",passtoss);
            adminintent1.putExtra("OPT",passOpted);

            if(toss==1&&opted==1 || toss==0&&opted==0)
            {
                adminintent1.putExtra("H1", eT_hostP1.getText().toString());
                adminintent1.putExtra("H2", eT_hostP2.getText().toString());
                adminintent1.putExtra("H3", eT_hostP3.getText().toString());
                adminintent1.putExtra("H4", eT_hostP4.getText().toString());
                adminintent1.putExtra("H5", eT_hostP5.getText().toString());
                adminintent1.putExtra("H6", eT_hostP6.getText().toString());
                adminintent1.putExtra("H7", eT_hostP7.getText().toString());
                adminintent1.putExtra("H8", eT_hostP8.getText().toString());
                adminintent1.putExtra("H9", eT_hostP9.getText().toString());
                adminintent1.putExtra("H10",eT_hostP10.getText().toString());
                adminintent1.putExtra("H11",eT_hostP11.getText().toString());

                adminintent1.putExtra("V1", eT_vistP1.getText().toString());
                adminintent1.putExtra("V2", eT_vistP2.getText().toString());
                adminintent1.putExtra("V3", eT_vistP3.getText().toString());
                adminintent1.putExtra("V4", eT_vistP4.getText().toString());
                adminintent1.putExtra("V5", eT_vistP5.getText().toString());
                adminintent1.putExtra("V6", eT_vistP6.getText().toString());
                adminintent1.putExtra("V7", eT_vistP7.getText().toString());
                adminintent1.putExtra("V8", eT_vistP8.getText().toString());
                adminintent1.putExtra("V9", eT_vistP9.getText().toString());
                adminintent1.putExtra("V10",eT_vistP10.getText().toString());
                adminintent1.putExtra("V11",eT_vistP11.getText().toString());
            }
            else if(toss==1&&opted==0 || toss==0&&opted==1)
            {
                adminintent1.putExtra("V1", eT_hostP1.getText().toString());
                adminintent1.putExtra("V2", eT_hostP2.getText().toString());
                adminintent1.putExtra("V3", eT_hostP3.getText().toString());
                adminintent1.putExtra("V4", eT_hostP4.getText().toString());
                adminintent1.putExtra("V5", eT_hostP5.getText().toString());
                adminintent1.putExtra("V6", eT_hostP6.getText().toString());
                adminintent1.putExtra("V7", eT_hostP7.getText().toString());
                adminintent1.putExtra("V8", eT_hostP8.getText().toString());
                adminintent1.putExtra("V9", eT_hostP9.getText().toString());
                adminintent1.putExtra("V10",eT_hostP10.getText().toString());
                adminintent1.putExtra("V11",eT_hostP11.getText().toString());

                adminintent1.putExtra("H1", eT_vistP1.getText().toString());
                adminintent1.putExtra("H2", eT_vistP2.getText().toString());
                adminintent1.putExtra("H3", eT_vistP3.getText().toString());
                adminintent1.putExtra("H4", eT_vistP4.getText().toString());
                adminintent1.putExtra("H5", eT_vistP5.getText().toString());
                adminintent1.putExtra("H6", eT_vistP6.getText().toString());
                adminintent1.putExtra("H7", eT_vistP7.getText().toString());
                adminintent1.putExtra("H8", eT_vistP8.getText().toString());
                adminintent1.putExtra("H9", eT_vistP9.getText().toString());
                adminintent1.putExtra("H10",eT_vistP10.getText().toString());
                adminintent1.putExtra("H11",eT_vistP11.getText().toString());
            }

            startActivity(adminintent1);
        }




    }

}
