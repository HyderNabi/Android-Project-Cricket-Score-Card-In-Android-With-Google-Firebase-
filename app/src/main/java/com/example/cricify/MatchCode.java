package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchCode extends AppCompatActivity {
    private EditText mcode;
    private ImageButton go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_matchcode);
        go = findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Go();
            }
        });


    }

    public void Go() {
        mcode = (EditText) findViewById(R.id.MatchCode);

        if (TextUtils.isEmpty(mcode.getText().toString())) {
            message("Enter Valid Match ID!");
        } else if (mcode.getText().toString().length() < 20 || mcode.getText().toString().length() > 20) {
            message("Match ID SHOULD BE 20 CHARACTERS LONG!");
        } else {
            checkifNodeExistsinFirebase();
        }
    }

    private void checkifNodeExistsinFirebase(){
        DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Scorify");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(mcode.getText().toString()).exists()){
                    Intent Submitintent = new Intent(getApplicationContext(), userScoreCard.class);
                    Submitintent.putExtra("KEY",mcode.getText().toString());
                    startActivity(Submitintent);
                }else{
                    message("Enter Valid Match ID!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void message(String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchCode.this);
        builder.setCancelable(true);
        builder.setMessage(Message);
        builder.setPositiveButton("OK",(DialogInterface.OnClickListener)(dialog, which)->{
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}