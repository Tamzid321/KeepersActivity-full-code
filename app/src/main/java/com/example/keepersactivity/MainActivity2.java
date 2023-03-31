package com.example.keepersactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private TextView loginButton;
    private TextView register;
    private ProgressBar progressBar;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;

    private static final String KEY_FAST_NAME = "fastName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "email";
    private static final String KEY_SHOP_NAME = "shopName";
    String firebaseUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();



        emailInput = findViewById(R.id.editTextNumber);
        passwordInput = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginbtn);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity3.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(MainActivity2.this, "Please fill in the form to continue.", Toast.LENGTH_SHORT).show();
                } else {

                        progressBar.setVisibility(View.VISIBLE);
                        loginButton.setEnabled(false);
                        loginButton.setBackgroundResource(R.drawable.click_button_bg);

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isComplete()){
                                    sendUserToHome();

                                } else {
                                    Toast.makeText(MainActivity2.this, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser != null){
            sendUserToHome();
        }
    }
    private void sendUserToHome() {
        Intent homeIntent = new Intent(MainActivity2.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
