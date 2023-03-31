package com.example.keepersactivity.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepersactivity.R;

public class StockViewHolder extends RecyclerView.ViewHolder {

    public TextView stockTextView;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);
        stockTextView = itemView.findViewById(R.id.stockRowTextview);
    }
}
