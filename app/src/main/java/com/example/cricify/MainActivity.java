package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private ImageButton login,signup,livescore,matchid;
    private  TextView message;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance(); //Initiate Firebase instance

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, HomeMenu.class);
            startActivity(intent);
        }

        initializeUI();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("Wait...");
                Login();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(MainActivity.this,SignUpIntent.class);
                startActivity(signup);
            }
        });
        livescore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent livescore = new Intent(MainActivity.this,LiveScore.class);
                startActivity(livescore);
            }
        });
        matchid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent matchid = new Intent(MainActivity.this,MatchCode.class);
                startActivity(matchid);
            }
        });

    }

    public void Login() {
        String email, password;
        email = this.email.getText().toString();
        password = this.password.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            message.setText("Log In");
            Message("Fields Can't Be Empty!");
        }else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                message.setText("Log In");
                                Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeMenu.class);
                                startActivity(intent);
                            }
                            else {
                                message.setText("Log In");
                                Message("Login failed! Please try again later");

                            }
                        }
            });
        }


    }
    private void Message(String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setMessage(Message);
        builder.setPositiveButton("OK",(DialogInterface.OnClickListener)(dialog, which)->{
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializeUI() {
       email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        message = findViewById(R.id.loginMessage);
        livescore = findViewById(R.id.livescore);
        matchid = findViewById(R.id.matchid);

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
