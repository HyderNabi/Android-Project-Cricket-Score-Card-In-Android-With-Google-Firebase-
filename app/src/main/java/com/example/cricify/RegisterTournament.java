package com.example.cricify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterTournament extends AppCompatActivity {
    private EditText TeamName,Address,contact;
    private ImageButton submit;
    DatabaseReference Tref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Tournaments");
    DatabaseReference user= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_register_tournament);

        initUI();
    }

    private void Register(){
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

        if(TextUtils.isEmpty(eT_hostP1.getText().toString()) || TextUtils.isEmpty(eT_hostP2.getText().toString()) || TextUtils.isEmpty(eT_hostP3.getText().toString()) || TextUtils.isEmpty(eT_hostP4.getText().toString())
                || TextUtils.isEmpty(eT_hostP5.getText().toString()) || TextUtils.isEmpty(eT_hostP6.getText().toString()) || TextUtils.isEmpty(eT_hostP7.getText().toString()) || TextUtils.isEmpty(eT_hostP8.getText().toString())
                || TextUtils.isEmpty(eT_hostP9.getText().toString()) || TextUtils.isEmpty(eT_hostP10.getText().toString()) || TextUtils.isEmpty(eT_hostP11.getText().toString()) ||
                TextUtils.isEmpty(TeamName.getText().toString()) || TextUtils.isEmpty(Address.getText().toString()) || TextUtils.isEmpty(contact.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterTournament.this);
            builder.setCancelable(true);
            builder.setMessage("INPUT FIELDS CAN'T BE EMPTY !");
            builder.setPositiveButton("OK",(DialogInterface.OnClickListener)(dialog, which)->{
                dialog.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }else{
            //enter data into database;


        }


    }

    private void initUI(){
        TeamName = (EditText) findViewById(R.id.TeamName);
        Address = (EditText) findViewById(R.id.Address);
        contact = (EditText) findViewById(R.id.contact);
        submit = (ImageButton) findViewById(R.id.submit);
    }

}