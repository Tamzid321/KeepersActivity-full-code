package com.example.keepersactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keepersactivity.Model.BuyModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ProfitReport extends AppCompatActivity {

    private TextView totalProfit, submitButton;
    private EditText othersCost;
    FirebaseFirestore firestore;
    DatabaseReference reference;
    FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;
    String userID, myProfit;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_report);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        userID = mCurrentUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child(userID).child("products");

        totalProfit = findViewById(R.id.expenseProfit);
        submitButton = findViewById(R.id.expenseSubmitButton);
        othersCost = findViewById(R.id.expenseEdittext);
        progressBar = findViewById(R.id.progressBar2);

        reference = FirebaseDatabase.getInstance().getReference().child(userID);
        reference.child("totalProfit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myProfit = String.valueOf(snapshot.child("myProfit").getValue());
                totalProfit.setText(myProfit);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                submitButton.setClickable(false);
                submitButton.setBackgroundResource(R.drawable.click_button_bg);
                String othersCostAmount = othersCost.getText().toString();
                int addOthersCost = Integer.valueOf(myProfit) - Integer.valueOf(othersCostAmount);
                HashMap<String, Object> map = new HashMap<>();
                map.put("myProfit", String.valueOf(addOthersCost));
                FirebaseDatabase.getInstance().getReference().child(userID).child("totalProfit")
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        submitButton.setBackgroundResource(R.drawable.expanse_btn_bg);
                        progressBar.setVisibility(View.GONE);
                        submitButton.setClickable(true);
                        othersCost.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfitReport.this, "Failed " + e, Toast.LENGTH_SHORT).show();
                        submitButton.setClickable(true);
                    }
                });


            }
        });

    }

}