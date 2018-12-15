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
//Home page
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //On the apps creation, create an object of the database config
        DatabaseConfig appDatabase = new DatabaseConfig.Builder(AppDatabase.class)
                .build();
        //Intilise the database with the Object
        ReActiveAndroid.init(new ReActiveConfig.Builder(this)
                .addDatabaseConfigs(appDatabase)
                .build());
        //Find the title by the id and set it to a variable
        TextView textView = findViewById(R.id.bigTitle);
        //Set a variable to a font inside the font folder
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/happycow.ttf");
        //set the font to the variable font assigned above
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
