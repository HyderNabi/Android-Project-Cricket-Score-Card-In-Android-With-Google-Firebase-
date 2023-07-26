package com.example.cricify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignUpIntent extends AppCompatActivity {

    private EditText email,password,cPassword;
    private ImageButton signup;
    private FirebaseAuth mAuth;
    private TextView message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_newacc);


        mAuth = FirebaseAuth.getInstance();

        initializeUI();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                message.setText("Wait...");
                loginUserAccount();
            }

        });
    }

    public void loginUserAccount() {
        String email, password,cpassword;
        email = this.email.getText().toString();
        password = this.password.getText().toString();
        cpassword = this.cPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)) {
            message.setText("Sign Up");
            Message("Fields Can't Be Empty!");
        }else {
            if(password.length() >=6){
                if(password.equals(cpassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        message.setText("Sign Up");
                                        InsertUser();
                                        Intent intent = new Intent(SignUpIntent.this, HomeMenu.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        message.setText("Sign Up");
                                        Message("Sign Up failed! Please try again later");
                                    }
                                }
                            });
                }else{
                    message.setText("Sign Up");
                    Message("Password Does'nt Match!");
                }
            }else{
                message.setText("Sign Up");
                Message("Length Of Password Should Be At Least 6 Characters!");
            }

        }

    }
    private void initializeUI()
    {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.cpassword);
        signup = findViewById(R.id.signup);
        message = findViewById(R.id.loginMessage);

    }
    private void InsertUser(){
        DatabaseReference myref= FirebaseDatabase.getInstance("https://sh-scorebase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myref.child(user.getUid()).child("matches").setValue("");
        }else{
            Toast.makeText(getApplicationContext(), "Something Went Wrong! Please try again later", Toast.LENGTH_LONG).show();
        }
    }

    private void Message(String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpIntent.this);
        builder.setCancelable(true);
        builder.setMessage(Message);
        builder.setPositiveButton("OK",(DialogInterface.OnClickListener)(dialog, which)->{
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
