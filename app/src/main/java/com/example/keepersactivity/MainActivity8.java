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
import android.widget.Toast;

import com.example.keepersactivity.Model.SellModel;
import com.example.keepersactivity.ViewHolder.SellViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity8 extends AppCompatActivity {

    ImageView backButton;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<SellModel> options;
    FirebaseRecyclerAdapter<SellModel, SellViewHolder> adapter;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    String userID;
    DatabaseReference reference1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        userID = mCurrentUser.getUid();

        reference1 = FirebaseDatabase.getInstance().getReference().child(userID).child("sellReport");

        backButton = findViewById(R.id.imageView3);
        recyclerView = findViewById(R.id.sellReportRecyclerView);
        progressBar = findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity4.class);
                startActivity(intent);
            }
        });

        options = new FirebaseRecyclerOptions.Builder<SellModel>().setQuery(reference1,SellModel.class).build();
        adapter = new FirebaseRecyclerAdapter<SellModel, SellViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onBindViewHolder(@NonNull SellViewHolder holder, int position, @NonNull SellModel model) {
                holder.productName.setText(model.getProductName());
                holder.productPrice.setText("Product Price: "+model.getProductPrice());
                holder.productUnit.setText("Unit: "+model.getProductUnit()+" "+model.getProductType());
                holder.productProfit.setText("Profit: "+model.getProfit());
            }

            @NonNull
            @Override
            public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sell,parent,false);
                return new SellViewHolder(view);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
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