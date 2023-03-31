package com.example.keepersactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.keepersactivity.Model.BuyModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity5 extends AppCompatActivity {

    private ImageView backButton, buyButton;
    private EditText productNameInput, qualityInput, unitInput, priceInput;
    private CheckBox kg, pcs, lir;
    private ProgressBar progressBar;
    String productName, quantity, unit, price, userID, unitType;
    FirebaseFirestore firestore;
    DatabaseReference reference;
    FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        userID = mCurrentUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child(userID).child("products").push();


        backButton = findViewById(R.id.stock_bkbtn);
        buyButton = findViewById(R.id.buyButton);
        productNameInput = findViewById(R.id.productName);
        qualityInput = findViewById(R.id.quantity);
        unitInput = findViewById(R.id.unit);
        priceInput = findViewById(R.id.price);
        progressBar = findViewById(R.id.progressBar);
        kg = findViewById(R.id.kg);
        pcs = findViewById(R.id.pcs);
        lir = findViewById(R.id.ltr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                startActivity(intent);

            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = productNameInput.getText().toString().trim();
                quantity = qualityInput.getText().toString().trim();
                unit = unitInput.getText().toString().trim();
                price = priceInput.getText().toString().trim();


                if (!productName.isEmpty() || !quantity.isEmpty() || !unit.isEmpty() || !price.isEmpty()){
                    if (kg.isChecked()){
                        progressBar.setVisibility(View.VISIBLE);
                        buyButton.setClickable(false);
                        buyButton.setBackgroundResource(R.drawable.click_button_bg);
                        unitType = "kg";


                        Map<String, Object> map = new HashMap<>();
                        map.put("productName", productName);
                        map.put("productQuantity", quantity);
                        map.put("productUnit", unit);
                        map.put("productType", unitType);
                        map.put("productPrice", price);
                        map.put("profit", "0");
                        reference.updateChildren(map);


                        productNameInput.setText("");
                        qualityInput.setText("");
                        unitInput.setText("");
                        priceInput.setText("");
                        kg.setChecked(false);
                        Toast.makeText(MainActivity5.this, "Buy successfully complete", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        buyButton.setClickable(true);
                        buyButton.setBackgroundResource(R.drawable.buy_button);


                    } else if (pcs.isChecked()){
                        progressBar.setVisibility(View.VISIBLE);
                        buyButton.setClickable(false);
                        buyButton.setBackgroundResource(R.drawable.click_button_bg);
                        unitType = "pcs";

                        Map<String, Object> map = new HashMap<>();
                        map.put("productName", productName);
                        map.put("productQuantity", quantity);
                        map.put("productUnit", unit);
                        map.put("productType", unitType);
                        map.put("productPrice", price);
                        map.put("profit", "0");
                        reference.updateChildren(map);

                        productNameInput.setText("");
                        qualityInput.setText("");
                        unitInput.setText("");
                        priceInput.setText("");
                        pcs.setChecked(false);
                        Toast.makeText(MainActivity5.this, "Buy successfully complete", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        buyButton.setClickable(true);
                        buyButton.setBackgroundResource(R.drawable.buy_button);

                    } else if (lir.isChecked()){
                        progressBar.setVisibility(View.VISIBLE);
                        buyButton.setClickable(false);
                        buyButton.setBackgroundResource(R.drawable.click_button_bg);
                        unitType = "ltr";

                        String key = reference.getKey();
                        Map<String, Object> map = new HashMap<>();
                        map.put("productName", productName);
                        map.put("productQuantity", quantity);
                        map.put("productUnit", unit);
                        map.put("productType", unitType);
                        map.put("productPrice", price);
                        map.put("profit", "0");
                        reference.updateChildren(map);


                        productNameInput.setText("");
                        qualityInput.setText("");
                        unitInput.setText("");
                        priceInput.setText("");
                        lir.setChecked(false);
                        Toast.makeText(MainActivity5.this, "Buy successfully complete", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        buyButton.setClickable(true);
                        buyButton.setBackgroundResource(R.drawable.buy_button);
                    } else {
                        Toast.makeText(MainActivity5.this, "Please fill up the all from", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity5.this, "Please fill up the all from", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}