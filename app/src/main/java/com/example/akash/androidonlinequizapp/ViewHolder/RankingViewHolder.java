package com.example.akash.androidonlinequizapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.akash.androidonlinequizapp.Interface.ItemClickListener;
import com.example.akash.androidonlinequizapp.R;

/**
 * Created by akash on 16/5/18.
 */

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txt_name,txt_score;
    private ItemClickListener itemClickListener;

    public RankingViewHolder(View itemView) {
        super(itemView);
        txt_name = (TextView) itemView.findViewById(R.id.txt_name);
        txt_score = (TextView) itemView.findViewById(R.id.txt_score);
        itemView.setOnClickListener(this);
    }

    public void setTxt_name(TextView txt_name) {
        this.txt_name = txt_name;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
