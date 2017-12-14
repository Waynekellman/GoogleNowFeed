package com.nyc.googlenowfeed;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nyc.googlenowfeed.controller.HackerAdapter;
import com.nyc.googlenowfeed.models.HackerModel;
import com.nyc.googlenowfeed.models.HackerTopStoriesModel;
import com.nyc.googlenowfeed.network.HackerApi;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProgress();
        hackerNewsArticles = new ArrayList<>();
        hackerAPI();

        recyclerView = findViewById(R.id.recyclerview);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecView();
            }
        }, 5000);




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
        Call<Integer[]> getHackerNews = hackService.getmodel();
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



        /**
         * NYTimesService igService = retrofit.create(NYTimesService.class);
         Call<BestSeller> getRecentMedia = igService.getBestSellers();
         getRecentMedia.enqueue(new Callback<BestSeller>() {
        @Override
        public void onResponse(Call<BestSeller> call, Response<BestSeller> response) {
        if (response.isSuccessful()) {
        BestSeller NYTBestSellers = response.body();
        mCardsData.add(NYTBestSellers);
        }
        }

        @Override
        public void onFailure(Call<BestSeller> call, Throwable t) {
        }
        });
         */
    }
}
