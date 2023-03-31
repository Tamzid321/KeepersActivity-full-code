package com.example.keepersactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity3 extends AppCompatActivity {

    private EditText fastNameInput, lastNameInput, passwordInput, numberInput, shopNameInput;
    private ImageView registerButton, backButton;
    private CheckBox checkBox;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    private static final String TAG = "MainActivity";
    private static final String KEY_FAST_NAME = "fastName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "email";
    private static final String KEY_SHOP_NAME = "shopName";

    String email, fastName, lastName, password, shopName, userID;
    ProgressBar progressBar;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        fastNameInput = findViewById(R.id.fastNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        passwordInput = findViewById(R.id.passwordInput);
        numberInput = findViewById(R.id.numberInput);
        shopNameInput = findViewById(R.id.shobName);
        registerButton = findViewById(R.id.loginbtn);
        backButton = findViewById(R.id.backButton);
        checkBox = findViewById(R.id.checkBox);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = numberInput.getText().toString();
                fastName = fastNameInput.getText().toString().trim();
                lastName = lastNameInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();
                shopName = shopNameInput.getText().toString().trim();

                if (!fastName.isEmpty() || !lastName.isEmpty() || !password.isEmpty() || !shopName.isEmpty() || !email.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    registerButton.setClickable(false);
                    registerButton.setBackgroundResource(R.drawable.click_button_bg);

                    RegisterUser();

                } else {
                    Toast.makeText(MainActivity3.this, "Please fill in the form to continue.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



      private void RegisterUser(){
          mAuth.createUserWithEmailAndPassword(email, password)
                  .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()) {

                              userID = mAuth.getCurrentUser().getUid();
                              Map<String, Object> note = new HashMap<>();
                              note.put(KEY_FAST_NAME, fastName);
                              note.put(KEY_LAST_NAME, lastName);
                              note.put(KEY_PASSWORD, password);
                              note.put(KEY_PHONE, email);
                              note.put(KEY_SHOP_NAME, shopName);
                              db.collection("userData").document(userID).set(note)
                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void aVoid) {
                                              sendUserToHome();
                                              progressBar.setVisibility(View.INVISIBLE);
                                          }
                                      })
                                      .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              progressBar.setVisibility(View.INVISIBLE);
                                              Toast.makeText(MainActivity3.this, "Error!"+e, Toast.LENGTH_SHORT).show();
                                              Log.d(TAG, e.toString());
                                          }
                                      });
                              progressBar.setVisibility(View.INVISIBLE);
                              registerButton.setBackgroundResource(R.drawable.group_496);
                              registerButton.setClickable(false);

                          } else {
                              registerButton.setBackgroundResource(R.drawable.group_496);
                              registerButton.setClickable(false);
                              progressBar.setVisibility(View.INVISIBLE);
                              Toast.makeText(MainActivity3.this, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                              Log.w(TAG, "createUserWithEmail:failure", task.getException());

                          }
                      }
                  });
        }
    private void sendUserToHome() {
        Intent homeIntent = new Intent(MainActivity3.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

}