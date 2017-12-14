package com.nyc.googlenowfeed.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nyc.googlenowfeed.MainActivity;
import com.nyc.googlenowfeed.R;
import com.nyc.googlenowfeed.models.HackerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    public void onBindViewHolder(final HackerViewHolder holder, int position) {
        holder.onBind(hackerModels.get(position));
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uri = holder.uri;

                intent.setData(Uri.parse(uri));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hackerModels.size();
    }

    public class HackerViewHolder extends RecyclerView.ViewHolder{

        private TextView title, url,points,author,date;
        private String uri;
        View view;
        private static final String TAG = "ViewHolder";

        public HackerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            url = itemView.findViewById(R.id.url);
            points = itemView.findViewById(R.id.points);
            author = itemView.findViewById(R.id.by);
            view = itemView;
            date = itemView.findViewById(R.id.time);
        }

        public void onBind(HackerModel hackerModel){
            title.setText(hackerModel.getTitle());
            uri = hackerModel.getUrl();
            String domain = domainFromUrl(uri);
            url.setText(domain);
            String pointsString = "" + hackerModel.getScore() + " points";
            points.setText(pointsString);
            String by = "by " + hackerModel.getBy();
            author.setText(by);
            String unixTime = String.valueOf(hackerModel.getTime());
//            Log.d(TAG, "unixTime "+unixTime);
            Long tsLong = System.currentTimeMillis()/1000;
//            Log.d(TAG,"tsLong "+String.valueOf(tsLong));
            long timeSincePost = tsLong - hackerModel.getTime();
            Log.d(TAG,"timeSincePost " + timeSincePost);
            double current = timeSincePost/60.0;
            Log.d(TAG,"current " + current);
            Log.d(TAG,"current time correct " + getDate(current));
//            Log.d(TAG,"currentTime "+currentTime);
            String currentTime;
            if (current < 60) {
                currentTime = getDate(current) + " minutes ago";
            } else {
                if (getDate(current) == 1) {
                    currentTime = getDate(current) + " hour ago";
                } else {
                    currentTime = getDate(current) + " hours ago";
                }
            }
            date.setText(""+ currentTime);
        }

        public String domainFromUrl(String url){
            String[] domainArray = url.split("/");
            String domain = domainArray[2];
            if(domain.startsWith("www.")){
                domain = domain.substring(4);
            }
            Log.d(TAG, domain);

            return domain;
        }
        private int getDate(double time) {
            if(time > 60){
                time = time/60;
            }

            return (int) time;
        }
    }
}
