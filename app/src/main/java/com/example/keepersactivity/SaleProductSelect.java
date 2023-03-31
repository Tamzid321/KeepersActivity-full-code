package com.example.keepersactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.keepersactivity.Model.BuyModel;
import com.example.keepersactivity.ViewHolder.StockViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SaleProductSelect extends AppCompatActivity {

    private ImageView backButton;
    private RecyclerView recyclerView;

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore firestore;
    DatabaseReference reference;
    FirebaseRecyclerOptions<BuyModel> options;
    FirebaseRecyclerAdapter<BuyModel, StockViewHolder> adapter;
    String userID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_product_select);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        userID = mCurrentUser.getUid();


        reference = FirebaseDatabase.getInstance().getReference().child(userID).child("products");



        backButton = findViewById(R.id.stock_bkbtn);
        recyclerView = findViewById(R.id.stockRecyclerView);
        progressBar = findViewById(R.id.progressBar6);
        progressBar.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity5.class);
                startActivity(intent);
                finish();
            }
        });

        options = new FirebaseRecyclerOptions.Builder<BuyModel>().setQuery(reference,BuyModel.class).build();
        adapter = new FirebaseRecyclerAdapter<BuyModel, StockViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onBindViewHolder(@NonNull StockViewHolder holder, int position, @NonNull BuyModel model) {

                holder.stockTextView.setText(model.productUnit+" "+model.productType+" "+model.getProductQuantity()+" Quality"+" "+model.productName+" available in your stock");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        adapter.getRef(position).getKey();

                        Intent intent = new Intent(getApplicationContext(),MainActivity6.class);
                        intent.putExtra("productName",model.productName);
                        intent.putExtra("productQuantity",model.productQuantity);
                        intent.putExtra("productUnit",model.productUnit);
                        intent.putExtra("productUnitType",model.productType);
                        intent.putExtra("productPrice",model.productPrice);
                        intent.putExtra("position",adapter.getRef(position).getKey());
                        intent.putExtra("profit",model.getProfit());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stock,parent,false);
                return new StockViewHolder(view);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter == null){
            adapter.startListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter == null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter == null){
            adapter.stopListening();
        }
    }
}