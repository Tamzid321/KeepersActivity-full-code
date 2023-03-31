package com.example.keepersactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity6 extends AppCompatActivity {

    private ImageView backButton, saleButton;
    private EditText productNameInput, qualityInput, unitInput, priceInput;
    private CheckBox kg, pcs, lir;
    private ProgressBar progressBar;
    String productName, quantity, unit, price, userID, unitType, position, profit;
    FirebaseFirestore firestore;
    DatabaseReference reference;
    FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;
    String oldProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        position = getIntent().getStringExtra("position");
        productName = getIntent().getStringExtra("productName");
        quantity = getIntent().getStringExtra("productQuantity");
        unit = getIntent().getStringExtra("productUnit");
        unitType = getIntent().getStringExtra("productUnitType");
        price = getIntent().getStringExtra("productPrice");
        profit = getIntent().getStringExtra("profit");


        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        userID = mCurrentUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child(userID);
        reference.child("totalProfit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                oldProfit = String.valueOf(snapshot.child("myProfit").getValue());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton = findViewById(R.id.stock_bkbtn);
        saleButton = findViewById(R.id.buyButton);
        productNameInput = findViewById(R.id.productName);
        qualityInput = findViewById(R.id.quantity);
        unitInput = findViewById(R.id.unit);
        priceInput = findViewById(R.id.price);
        progressBar = findViewById(R.id.progressBar);
        kg = findViewById(R.id.kg);
        pcs = findViewById(R.id.pcs);
        lir = findViewById(R.id.ltr);

        productNameInput.setText(productName);
        qualityInput.setText(quantity);

        if (unitType.equals("kg")){
            kg.setChecked(true);
        } else if (unitType.equals("pcs")){
            pcs.setChecked(true);
        } else if (unitType.equals("ltr")){
            lir.setChecked(true);
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                startActivity(intent);

            }
        });
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saleButton.setBackgroundResource(R.drawable.click_button_bg);
                saleButton.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                String saleUnit = unitInput.getText().toString();
                reference = FirebaseDatabase.getInstance().getReference().child(userID).child("products").child(position);
                int totalUnit = Integer.valueOf(unit) - Integer.valueOf(saleUnit);


                Map<String, Object> map = new HashMap<>();
                map.put("productUnit",String.valueOf(totalUnit));
                FirebaseDatabase.getInstance().getReference().child(userID).child("products").child(position)
                        .updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateProfit();
                                updateTotalProfit();
                                storeSellReport();
                                Toast.makeText(MainActivity6.this, "Sell successfully complete", Toast.LENGTH_SHORT).show();
                                unitInput.setText("");
                                priceInput.setText("");
                                saleButton.setBackgroundResource(R.drawable.sale);
                                saleButton.setClickable(true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity6.this, "Sell not complete\n"+e, Toast.LENGTH_SHORT).show();
                        saleButton.setBackgroundResource(R.drawable.sale);
                        saleButton.setClickable(true);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });


            }
        });




    }

    private void updateProfit(){
        int unitPrice = Integer.valueOf(unitInput.getText().toString()) * Integer.valueOf(price);
        int sellprice = Integer.parseInt(priceInput.getText().toString()) * Integer.parseInt(unitInput.getText().toString());

        int totalSaleProfit = sellprice - unitPrice;
        int oldProfitPlusNewProfit = Integer.valueOf(profit) + totalSaleProfit;
        Map<String, Object> map = new HashMap<>();
        map.put("profit",String.valueOf(oldProfitPlusNewProfit));
        FirebaseDatabase.getInstance().getReference().child(userID).child("products").child(position)
                .updateChildren(map);
    }
    private void updateTotalProfit(){

        int unitPrice = Integer.valueOf(unitInput.getText().toString()) * Integer.valueOf(price);
        int sellprice = Integer.parseInt(priceInput.getText().toString()) * Integer.parseInt(unitInput.getText().toString());

        int totalSaleProfit = sellprice - unitPrice;
        int ddd = Integer.valueOf(oldProfit) + totalSaleProfit;
        HashMap<String, Object> map = new HashMap<>();
        map.put("myProfit",String.valueOf(ddd));
        FirebaseDatabase.getInstance().getReference().child(userID).child("totalProfit")
                .updateChildren(map);

    }
    private void storeSellReport(){

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child(userID).child("sellReport").push();
        int unitPrice = Integer.valueOf(unitInput.getText().toString()) * Integer.valueOf(price);
        int sellprice = Integer.parseInt(priceInput.getText().toString()) * Integer.parseInt(unitInput.getText().toString());

        int totalSaleProfit = sellprice - unitPrice;

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productName);
        map.put("productQuantity", quantity);
        map.put("productUnit", unitInput.getText().toString());
        map.put("productType", unitType);
        map.put("productPrice", priceInput.getText().toString());
        map.put("profit", String.valueOf(totalSaleProfit));
        reference1.updateChildren(map);

    }
}