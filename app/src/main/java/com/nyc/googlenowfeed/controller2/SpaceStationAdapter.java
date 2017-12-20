package com.nyc.googlenowfeed.controller2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nyc.googlenowfeed.R;
import com.nyc.googlenowfeed.controller.SpaceViewHolder;
import com.nyc.googlenowfeed.models2.SpaceStationModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D on 12/16/17.
 */

public class SpaceStationAdapter extends RecyclerView.Adapter<SpaceViewHolder> {

   private ArrayList<SpaceStationModels> spaceStationModelsList;

    public SpaceStationAdapter(ArrayList<SpaceStationModels> spaceStationModelsList ){
        this.spaceStationModelsList = spaceStationModelsList;
        notifyDataSetChanged();
    }

    @Override
    public SpaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View spaceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spaceitemview,parent,false);
        return new SpaceViewHolder(spaceView);
    }

    @Override
    public void onBindViewHolder(SpaceViewHolder holder, int position) {
            SpaceStationModels models =spaceStationModelsList.get(position);
            holder.onBind(models);
    }

    @Override
    public int getItemCount() {
        return spaceStationModelsList.size();
    }
}
