package com.example.restaurant_picker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UserInput extends AppCompatActivity {
    Button enter;
    Button priceMin;
    Button priceMid;
    Button priceMax;
    int price;
    int distance;
    Fragment fragment;
    String selectedCuisine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_input);
        FragmentManager fm = getSupportFragmentManager();

        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new Cuisine();
            ft.add(android.R.id.content,fragment);
            ft.commit();
        }
        SeekBar distancePref = (SeekBar)findViewById(R.id.distanceSeekBar);
        distancePref.setMax(9);
        distancePref.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView progressText = findViewById(R.id.progressText);
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                progressText.setText("" + progress);
                progressText.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                progressText.setY(750);
                distance = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance = distancePref.getProgress()+1;
            }
        });

        priceMin = (Button)findViewById(R.id.minPrice);
        priceMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = 2;
                priceMin.setBackgroundColor(Color.GRAY);
                priceMid.setBackgroundColor(Color.rgb(138,43,226));
                priceMax.setBackgroundColor(Color.rgb(138,43,226));
            }
        });
        priceMid = (Button)findViewById(R.id.midPrice);
        priceMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = 3;
                priceMin.setBackgroundColor(Color.rgb(138,43,226));
                priceMid.setBackgroundColor(Color.GRAY);
                priceMax.setBackgroundColor(Color.rgb(138,43,226));
            }
        });
        priceMax = (Button)findViewById(R.id.maxPrice);
        priceMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = 4;
                priceMin.setBackgroundColor(Color.rgb(138,43,226));
                priceMid.setBackgroundColor(Color.rgb(138,43,226));
                priceMax.setBackgroundColor(Color.GRAY);
            }
        });


        enter = (Button) findViewById(R.id.button_enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cuisine = selectedCuisine;
                Intent intent = new Intent(UserInput.this, MapsActivity.class);
                if (cuisine.equals("type my own restaurant")) {
                    cuisine = selectedCuisine;
                    if (cuisine.matches("")) {
                        Toast.makeText(getApplicationContext(), "You did not enter a cuisine", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                intent.putExtra("food_preference", cuisine);
                intent.putExtra("distance_preference", distance);
                intent.putExtra("price_preference", price);
                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                startActivity(intent);
            }

        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        selectedCuisine = getIntent().getStringExtra("selectedCuisine");
        if(selectedCuisine!=null){
            if(fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}