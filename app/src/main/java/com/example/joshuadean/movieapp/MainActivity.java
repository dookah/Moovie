package com.example.joshuadean.movieapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reactiveandroid.ReActiveAndroid;
import com.reactiveandroid.ReActiveConfig;
import com.reactiveandroid.internal.database.DatabaseConfig;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseConfig appDatabase = new DatabaseConfig.Builder(AppDatabase.class)
                .build();

        ReActiveAndroid.init(new ReActiveConfig.Builder(this)
                .addDatabaseConfigs(appDatabase)
                .build());

        TextView textView = findViewById(R.id.bigTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/happycow.ttf");
        textView.setTypeface(typeface);
    }
    public void searchIntent(View view){
        Intent intent = new Intent(this,searchIntent.class);
        startActivity(intent);
    }
    public void seenIntent(View view){
        Intent intent = new Intent(this, seenActivity.class);
        startActivity(intent);
    }
    public void watchIntent(View view){
        Intent intent = new Intent(this, watchActivity.class);
        startActivity(intent);
    }



    //TODO - add watchlist intent
    //TODO - add seen intent


    public void moviesIntent(View view){
        Intent intent = new Intent(this, MovieList.class);
        startActivity(intent);
    }


}
