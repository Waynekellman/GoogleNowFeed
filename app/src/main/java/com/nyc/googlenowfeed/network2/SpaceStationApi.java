package com.nyc.googlenowfeed.network2;

import com.nyc.googlenowfeed.models2.SpaceStationModels;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
/**
 * Created by D on 12/16/17.
 */

public interface SpaceStationApi {
    @GET("iss-now.json")
    Call<SpaceStationModels> getSpaceStation();

}
