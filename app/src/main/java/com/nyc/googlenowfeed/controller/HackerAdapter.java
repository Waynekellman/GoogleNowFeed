package com.nyc.googlenowfeed.controller;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nyc.googlenowfeed.R;
import com.nyc.googlenowfeed.models.HackerModel;

import java.util.ArrayList;

/**
 * Created by Wayne Kellman on 12/13/17.
 */

public class HackerAdapter extends RecyclerView.Adapter<HackerAdapter.HackerViewHolder>{
    private ArrayList<HackerModel> hackerModels;

    public HackerAdapter(ArrayList<HackerModel> hackerModels) {
        this.hackerModels = hackerModels;
        notifyDataSetChanged();
    }

    @Override
    public HackerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, parent, false);
        return new HackerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HackerViewHolder holder, int position) {
        holder.onBind(hackerModels.get(position));
    }

    @Override
    public int getItemCount() {
        return hackerModels.size();
    }

    public class HackerViewHolder extends RecyclerView.ViewHolder{

        private TextView title, url;

        public HackerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            url = itemView.findViewById(R.id.url);
        }

        public void onBind(HackerModel hackerModel){
            title.setText(hackerModel.getTitle());
            url.setText(hackerModel.getUrl());
        }
    }
}
