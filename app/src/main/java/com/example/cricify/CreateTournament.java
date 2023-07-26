package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateTournament extends AppCompatActivity {
    private EditText TourName,TourVanue,teamsNO,fees,type,date;
    private ImageButton submit;
    private String TourKey;

    private ArrayList<String> myTours = new ArrayList<String>();

    DatabaseReference Tref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Tournaments");
    DatabaseReference user= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_create_tournament);

        InitializeUI();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTournament.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SubmitData();
            }
        });



    }

    private void InitializeUI(){
        TourName = (EditText) findViewById(R.id.TourName);
        TourVanue = (EditText) findViewById(R.id.TourVanue);
        teamsNO = (EditText) findViewById(R.id.teamsNO);
        type = (EditText) findViewById(R.id.type);
        date = (EditText) findViewById(R.id.date);
        fees = (EditText) findViewById(R.id.fees);
        submit = (ImageButton) findViewById(R.id.submit);
    }


    public void SubmitData() {

        if (TextUtils.isEmpty(TourName.getText().toString()) || TextUtils.isEmpty(TourVanue.getText().toString()) || TextUtils.isEmpty(teamsNO.getText().toString()) || TextUtils.isEmpty(type.getText().toString()) || TextUtils.isEmpty(date.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateTournament.this);
            builder.setCancelable(true);
            builder.setMessage("INPUT FIELDS CAN'T BE EMPTY !");
            builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            //Create a Tournament in Tournament Branch
            TourKey = Tref.push().getKey();

            //Enter Tour Data

            Tref = Tref.child(TourKey);//propogate to this tour

            Tref.child("TourName").setValue(TourName.getText().toString());
            Tref.child("TourVanue").setValue(TourVanue.getText().toString());
            Tref.child("teamsNO").setValue(teamsNO.getText().toString());
            Tref.child("type").setValue(type.getText().toString());
            Tref.child("date").setValue(date.getText().toString());
            Tref.child("fees").setValue(fees.getText().toString());

            Tref.child("status").setValue("Open");

            Tref.child("RegesteredTeams").setValue(0);

            //insert this Tour in the user branch
            InsertTour(TourKey);


        }
    }
    private void InsertTour(String TourID){
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        user = user.child(thisuser.getUid()).child("tournaments");
        if (thisuser != null) {
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChildren()){
                        myTours.clear();
                        myTours = (ArrayList<String>) snapshot.getValue();
                        myTours.add(TourID);
                        user.setValue(myTours);

                    }else{
                        myTours.add(TourID);
                        user.setValue(myTours);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Something Went Wrong! Please try again later", Toast.LENGTH_LONG).show();
        }

    }
}