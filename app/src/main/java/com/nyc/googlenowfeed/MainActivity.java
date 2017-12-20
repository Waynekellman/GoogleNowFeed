package com.nyc.googlenowfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nyc.googlenowfeed.controller.HackerAdapter;
import com.nyc.googlenowfeed.models.HackerModel;
import com.nyc.googlenowfeed.models.HackerTopStoriesModel;
import com.nyc.googlenowfeed.models2.SpaceStationModels;
import com.nyc.googlenowfeed.network.HackerApi;
import com.nyc.googlenowfeed.network2.SpaceStationApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public Retrofit mRetrofit;
    private String hackerBaseUrl = "https://hacker-news.firebaseio.com/v0/";
    private static final String TAG = "MainActivity";
    private HackerTopStoriesModel hackerTopStoriesModel;
    private ArrayList<HackerModel> hackerNewsArticles;
    private RecyclerView recyclerView;
    private SpaceStationModels spaceStationModels;

    private TextView message,timeStamp,latitude,longitude;
    private CardView hackerCardViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setModels();

        createViews();
        setViewInvisible();
        showProgress();
        hackerAPI();
        spaceStationApi();


        recyclerView = findViewById(R.id.recyclerview);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecView();
                setViewsVisible();
                setViews();
            }
        }, 5000);




    }

    private void setViewsVisible() {
        message.setVisibility(View.VISIBLE);
        timeStamp.setVisibility(View.VISIBLE);
        latitude.setVisibility(View.VISIBLE);
        longitude.setVisibility(View.VISIBLE);
        hackerCardViews.setVisibility(View.VISIBLE);
    }

    private void setViewInvisible() {
        message.setVisibility(View.INVISIBLE);
        timeStamp.setVisibility(View.INVISIBLE);
        latitude.setVisibility(View.INVISIBLE);
        longitude.setVisibility(View.INVISIBLE);
        hackerCardViews.setVisibility(View.INVISIBLE);
    }

    private void setModels() {
        spaceStationModels = new SpaceStationModels();
        hackerNewsArticles = new ArrayList<>();
    }

    private void setViews() {
        message.setText(spaceStationModels.getMessage());
        timeStamp.setText(spaceStationModels.getTimestamp());
        latitude.setText(String.valueOf(spaceStationModels.iss_position().getLatitude()));
        longitude.setText(String.valueOf(spaceStationModels.iss_position().getLongitude()));
    }

    private void spaceStationApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.open-notify.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpaceStationApi service = retrofit.create(SpaceStationApi.class);
        Call<SpaceStationModels> getModel = service.getSpaceStation();
        getModel.enqueue(new Callback<SpaceStationModels>() {
            @Override
            public void onResponse(Call<SpaceStationModels> call, Response<SpaceStationModels> response) {
                spaceStationModels = response.body();

            }

            @Override
            public void onFailure(Call<SpaceStationModels> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void createViews(){
        message = findViewById(R.id.message);
        timeStamp = findViewById(R.id.time_stamp);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        spaceStationModels = new SpaceStationModels();
        hackerCardViews = findViewById(R.id.hackerCardView);

    }
    public void showProgress() {
        final int time = 5000;
        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Loading data...");
        dlg.setCancelable(false);
        dlg.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dlg.dismiss();
            }
        }, time);
    }

    private void initRecView() {
        HackerAdapter hackerAdapter = new HackerAdapter(hackerNewsArticles);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(hackerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void hackerAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(hackerBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final HackerApi hackService = retrofit.create(HackerApi.class);
        final Call<Integer[]> getHackerNews = hackService.getModel();
        getHackerNews.enqueue(new Callback<Integer[]>() {
            @Override
            public void onResponse(Call<Integer[]> call, Response<Integer[]> response) {
                hackerTopStoriesModel = new HackerTopStoriesModel();
                hackerTopStoriesModel.setTopstorie(response.body());
                int i = 0;
                for (int n : hackerTopStoriesModel.getTopstorie()) {

                    Log.d(TAG, String.valueOf(n));
                    if(i <10) {
                        Call<HackerModel> getHackerNews2 = hackService.getHackerNews(n);
                        getHackerNews2.enqueue(new Callback<HackerModel>() {
                            @Override
                            public void onResponse(Call<HackerModel> call, Response<HackerModel> response) {
                                hackerNewsArticles.add(response.body());
                                Log.d(TAG, hackerNewsArticles.get(hackerNewsArticles.size() - 1).getTitle());
                            }

                            @Override
                            public void onFailure(Call<HackerModel> call, Throwable t) {
                                Log.d(TAG, "Second failed " + t.getLocalizedMessage());

                                t.printStackTrace();
                            }
                        });
                    }
                    i++;
                }
            }



            @Override
            public void onFailure(Call<Integer[]> call, Throwable t) {
                Log.d(TAG,"first failed " + t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }
}
