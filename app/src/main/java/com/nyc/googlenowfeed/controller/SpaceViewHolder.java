package com.nyc.googlenowfeed.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nyc.googlenowfeed.R;
import com.nyc.googlenowfeed.controller2.SpaceStationAdapter;
import com.nyc.googlenowfeed.models2.SpaceStationModels;

/**
 * Created by D on 12/18/17.
 */

public class SpaceViewHolder extends RecyclerView.ViewHolder {

    private TextView message;
    private TextView timeStamp;
    private TextView latitude;
    private TextView longitude;

    public SpaceViewHolder(View itemView) {
        super(itemView);

        message = (TextView) itemView.findViewById(R.id.message);
        timeStamp = (TextView) itemView.findViewById(R.id.time_stamp);
        latitude = (TextView) itemView.findViewById(R.id.latitude);
        longitude = (TextView) itemView.findViewById(R.id.longitude);
    }

    public void onBind(SpaceStationModels space) {
        message.setText("Message : " + space.getMessage());
        timeStamp.setText("Time : " + space.getTimestamp());
        latitude.setText(String.valueOf(space.iss_position().getLatitude()));
        longitude.setText(String.valueOf(space.iss_position().getLongitude()));

    }


}
