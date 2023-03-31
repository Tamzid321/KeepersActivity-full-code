package com.example.keepersactivity.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepersactivity.R;

public class SellViewHolder extends RecyclerView.ViewHolder {
    public TextView productName, productPrice, productUnit, productProfit;
    public SellViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.sellProductName);
        productPrice = itemView.findViewById(R.id.sellPrice);
        productUnit = itemView.findViewById(R.id.sellUnit);
        productProfit = itemView.findViewById(R.id.sellProfit);

    }
}
