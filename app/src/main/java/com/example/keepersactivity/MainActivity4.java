package com.example.keepersactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity implements View.OnClickListener {

    private TextView welcomeBack, userName;
    private ImageView buyButton, sellButton, stockButton, expenseButton, reportButton, logoutButton;

    FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private static final String KEY_FAST_NAME = "fastName";
    String userID;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        userID = mCurrentUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child(userID).child("totalProfit");

        welcomeBack = findViewById(R.id.welcomeBack);
        userName = findViewById(R.id.userName);
        buyButton = findViewById(R.id.buybtn);
        sellButton = findViewById(R.id.sale_button);
        stockButton = findViewById(R.id.stock_button);
        expenseButton = findViewById(R.id.expense_button);
        reportButton = findViewById(R.id.report_button);
        logoutButton = findViewById(R.id.logout_button);

        welcomeBack.setOnClickListener(this);
        userName.setOnClickListener(this);
        buyButton.setOnClickListener(this);
        sellButton.setOnClickListener(this);
        stockButton.setOnClickListener(this);
        expenseButton.setOnClickListener(this);
        reportButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);


        loadUserName();
        MyProfit();


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buybtn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity5.class);
            startActivity(intent);
        } else if (v.getId() == R.id.sale_button) {
            Intent intent = new Intent(getApplicationContext(), SaleProductSelect.class);
            startActivity(intent);

        } else if (v.getId() == R.id.stock_button) {
            Intent intent = new Intent(getApplicationContext(), MainActivity7.class);
            startActivity(intent);
        } else if (v.getId() == R.id.expense_button) {
            Intent intent = new Intent(getApplicationContext(), ProfitReport.class);
            startActivity(intent);
        } else if (v.getId() == R.id.report_button) {
            Intent intent = new Intent(getApplicationContext(), MainActivity8.class);
            startActivity(intent);
        } else if (v.getId() == R.id.logout_button) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(MainActivity4.this, "Are you sure you want to logout?");
        }

    }

    private void loadUserName() {
        DocumentReference documentReference = firestore.collection("userData").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    userName.setText(documentSnapshot.getString(KEY_FAST_NAME));

                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

    }

    private void MyProfit() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("myProfit", "0");
                    reference.setValue(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class ViewDialog {
        public void showDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.logout_dialog);


            TextView yesButton = dialog.findViewById(R.id.btn_yes);
            TextView noButton = dialog.findViewById(R.id.btn_no);

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

}