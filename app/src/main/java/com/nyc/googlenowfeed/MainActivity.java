package com.nyc.googlenowfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.nyc.googlenowfeed.network.NetworkApi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String spaceStationBaseUrl = "http://api.open-notify.org/";
    public Retrofit mRetrofit;
    private String hackerBaseUrl = "https://hacker-news.firebaseio.com/v0/";
    private static final String TAG = "MainActivity";
    private HackerTopStoriesModel hackerTopStoriesModel;
    private List<HackerModel> hackerNewsArticles;
    private RecyclerView recyclerView;
    private SpaceStationModels spaceStationModels;

    private TextView message,timeStamp,latitude,longitude;
    private CardView hackerCardViews;
    private NetworkApi service;
    private String hackerArticleBaseUrl = "https://hacker-news.firebaseio.com/v0/";
    private HackerModel[] hackerModels;
    private HackerModel model;
    private Integer integer;
    private Button map;

    private HackerAdapter hackerAdapter;
    private Handler h = new Handler();
    private int delay = 5000;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setModels();

        createViews();
        showProgress();
        hackerAPI();
        spaceStationApi();

        recyclerView = findViewById(R.id.recyclerview);
        initRecView();


        NoteFragment fragment = new NoteFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_layout, fragment);
        transaction.commit();


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("latitude", spaceStationModels.iss_position().getLatitude());
                intent.putExtra("longitude", spaceStationModels.iss_position().getLongitude());
                startActivity(intent);

            }
        });


    }
    @Override
    protected void onResume() { //start handler as activity become visible

        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                spaceStationApi();
                runnable=this;

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }


    private void setModels() {
        spaceStationModels = new SpaceStationModels();
        hackerNewsArticles = new LinkedList<>();
    }

    private void setViews() {
        message.setText(spaceStationModels.getMessage());
        timeStamp.setText(spaceStationModels.getTimestamp());
        latitude.setText(String.valueOf(spaceStationModels.iss_position().getLatitude()));
        longitude.setText(String.valueOf(spaceStationModels.iss_position().getLongitude()));
    }

    private void spaceStationApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(spaceStationBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(NetworkApi.class);
        Call<SpaceStationModels> getModel = service.getSpaceStation();
        getModel.enqueue(new Callback<SpaceStationModels>() {
            @Override
            public void onResponse(Call<SpaceStationModels> call, Response<SpaceStationModels> response) {
                spaceStationModels = response.body();
                setViews();
                Log.d(TAG, "Latitude: " + spaceStationModels.iss_position().getLatitude());
                Log.d(TAG, "Longitude: " + spaceStationModels.iss_position().getLongitude());

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
        map = findViewById(R.id.map_frag);

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        hackerAdapter = new HackerAdapter(hackerNewsArticles);
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
        service = retrofit.create(NetworkApi.class);
        Call<Integer[]> getHackerNews = service.getModel();
        getHackerNews.enqueue(new Callback<Integer[]>() {
            @Override
            public void onResponse(Call<Integer[]> call, Response<Integer[]> response) {
                hackerTopStoriesModel = new HackerTopStoriesModel();
                hackerTopStoriesModel.setTopstorie(response.body());
                getArticleList();

            }



            @Override
            public void onFailure(Call<Integer[]> call, Throwable t) {
                Log.d(TAG,"first failed " + t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

    private void getArticleList() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(hackerArticleBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(NetworkApi.class);
        Integer[] integers = hackerTopStoriesModel.getTopstorie();
        hackerModels = new HackerModel[10];

        for (int i = 0; i < integers.length; i++) {
            if (i < 10) {
                Log.d(TAG, String.valueOf(integers[i]));
                /**
                 * The list is added out of order because the call is being made in a loop
                 * so the responses for each model is added out of sync.
                 * I still only recieve the articles I want though
                 * and there isn't much order to the top 10 or so articles.
                 *
                 * The id is in the model as well as the list of top stories. If I wanted I could
                 * make a hashmap with the id as the key and iterate through the topstories array to
                 * create a new list of proper size but that seems more than necessary and probably
                 * not best practice.
                 */
                Call<HackerModel> getHackerNews2 = service.getHackerNews(integers[i]);
                getHackerNews2.enqueue(new Callback<HackerModel>() {
                    @Override
                    public void onResponse(Call<HackerModel> call, Response<HackerModel> response) {
                        hackerNewsArticles.add(response.body());
                        hackerAdapter.notifyDataSetChanged();
                        Log.d(TAG, hackerNewsArticles.get(hackerNewsArticles.size() - 1).getTitle());
                    }

                    @Override
                    public void onFailure(Call<HackerModel> call, Throwable t) {
                        Log.d(TAG, "Second failed " + t.getLocalizedMessage());

                        t.printStackTrace();
                    }
                });
            }

        }

    }
}
