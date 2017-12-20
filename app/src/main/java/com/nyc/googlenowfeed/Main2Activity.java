package com.nyc.googlenowfeed;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        spaceStationModels = new SpaceStationModels();
        //Creates Views!
        createViews();




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.open-notify.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", spaceStationModels.getMessage());
            }
        },5000);

        SpaceStationApi service = retrofit.create(SpaceStationApi.class);
        Call<SpaceStationModels> getmodel = service.getSpaceStation();
        getmodel.enqueue(new Callback<SpaceStationModels>() {
            @Override
            public void onResponse(Call<SpaceStationModels> call, Response<SpaceStationModels> response) {
               String modelJSON = String.valueOf(response.body());
               Log.d(TAG, "modelJson: "+ modelJSON.toString());
                try {
                    JSONObject initialObject= new JSONObject(modelJSON);

                    Log.d(TAG,"Initial Object : " + initialObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
    }


}
