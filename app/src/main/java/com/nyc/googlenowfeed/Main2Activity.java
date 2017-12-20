package com.nyc.googlenowfeed;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.nyc.googlenowfeed.controller2.SpaceStationAdapter;
import com.nyc.googlenowfeed.models2.SpaceStationModels;
import com.nyc.googlenowfeed.network2.SpaceStationApi;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {
    SpaceStationModels spaceStationModels;
    private static String TAG ="Json Test";

    private TextView message;
    private TextView timeStamp;
    private TextView latitude;
    private TextView longitude;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spaceitemview);
        spaceStationModels = new SpaceStationModels();
        //Creates Views!
        createViews();

        message = findViewById(R.id.message);
        timeStamp = findViewById(R.id.time_stamp);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.open-notify.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.d("MainActivity", spaceStationModels.getMessage());
                message.setText(spaceStationModels.getMessage());
                timeStamp.setText(spaceStationModels.getTimestamp());
                latitude.setText(String.valueOf(spaceStationModels.iss_position().getLatitude()));
                longitude.setText(String.valueOf(spaceStationModels.iss_position().getLongitude()));
            }
        },10000);


        SpaceStationApi service = retrofit.create(SpaceStationApi.class);
        Call<SpaceStationModels> getmodel = service.getSpaceStation();
        getmodel.enqueue(new Callback<SpaceStationModels>() {
            @Override
            public void onResponse(Call<SpaceStationModels> call, Response<SpaceStationModels> response) {
//               String modelJSON = String.valueOf(response.body());
               spaceStationModels = response.body();
//               Log.d(TAG, "modelJson: "+ modelJSON.toString());
//                try {
//                    JSONObject initialObject= new JSONObject(modelJSON);
//
//                    Log.d(TAG,"Initial Object : " + initialObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onFailure(Call<SpaceStationModels> call, Throwable t) {
                    t.printStackTrace();
            }
        });
    }


//    public void progress(){
//        final int time = 3000;
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("tracking...");
//        dialog.setCancelable(false);
//        dialog.show();
//        new Handler().postDelayed(new Runnable(){
//            public void run (){
//                dialog.dismiss();
//            }
//        }, time);
//
//    }



    public void createViews(){
         message = (TextView) findViewById(R.id.message);
         timeStamp = (TextView)findViewById(R.id.time_stamp);
         latitude = (TextView)findViewById(R.id.latitude);
         longitude = (TextView)findViewById(R.id.longitude);
         recyclerView = findViewById(R.id.recyclerview2);
    }


}
