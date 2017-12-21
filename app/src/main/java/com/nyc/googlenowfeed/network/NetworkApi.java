package com.nyc.googlenowfeed.network;

import com.nyc.googlenowfeed.models.HackerModel;
import com.nyc.googlenowfeed.models2.SpaceStationModels;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Wayne Kellman on 12/12/17.
 */

public interface NetworkApi {

    @GET("topstories.json")
    Call<Integer[]> getModel();

    @GET("item/{item-id}.json?print=pretty")
    Call<HackerModel> getHackerNews(@Path("item-id") int id);


    @GET("iss-now.json")
    Call<SpaceStationModels> getSpaceStation();
}
