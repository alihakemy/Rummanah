package com.usmart.com.rummanah.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.usmart.com.rummanah.R;

import java.util.ArrayList;
import java.util.List;

import dataInLists.DataInProduct;

public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.ViewHolder> {

    private static final String TAG = RatesAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    public ArrayList<DataInProduct.Rates> dataArrayList;

    public RatesAdapter(Context ctx, ArrayList<DataInProduct.Rates> dataArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.dataArrayList = dataArrayList;
    }

    @Override
    public RatesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_high_rate, parent, false);
        RatesAdapter.ViewHolder holder = new RatesAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RatesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        DataInProduct.Rates rateItem = dataArrayList.get(position);

        holder.tv_name.setText(rateItem.user.name + "");
        holder.tv_date.setText(rateItem.created_at + "");
        holder.rb_rating.setRating((float) rateItem.rate);
        holder.tv_content.setText(rateItem.text);
    }

    public void refreshView(int position) {
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    //this method for recyclerview paging
    public void addProducts(ArrayList<DataInProduct.Rates> products) {
        for (DataInProduct.Rates product : products) {
            dataArrayList.add(product);
        }
        notifyDataSetChanged();
    }

    //this method for recyclerview paging
    public void addPagingSearch(List<DataInProduct.Rates> items) {
        for (DataInProduct.Rates item : items) {
            dataArrayList.add(item);
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_date, tv_content;
        RatingBar rb_rating;


        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            rb_rating = itemView.findViewById(R.id.rb_rating);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

}
