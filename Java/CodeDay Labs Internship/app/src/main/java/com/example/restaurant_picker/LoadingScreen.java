package com.example.restaurant_picker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class LoadingScreen extends AppCompatActivity {
    private String log = "LOG";
    private int delay = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        Intent intent = getIntent();
        delay = intent.getIntExtra("milliSeconds",1);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("LOG","Nullpointer caught in Loading screen try/catch");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Log.d(log,"Loading finished");
            }
        }, delay);

    }
}