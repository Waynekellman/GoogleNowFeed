package com.nyc.googlenowfeed;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }


    public void progress(){
        final int time = 3000;
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("tracking...");
        dialog.setCancelable(false);
        dialog.show();
        new Handler().postDelayed(new Runnable(){
            public void run (){
                dialog.dismiss();
            }
        }, time);

    }


}
