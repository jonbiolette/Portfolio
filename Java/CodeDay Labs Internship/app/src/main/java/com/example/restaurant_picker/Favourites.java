package com.example.restaurant_picker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class Favourites extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.settings:
                Intent act2 = new Intent(this, SettingsActivity.class);
                startActivity(act2);
                return (true);

            case R.id.exit:
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
}
