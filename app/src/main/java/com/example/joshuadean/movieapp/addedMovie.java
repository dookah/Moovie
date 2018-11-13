package com.example.joshuadean.movieapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class addedMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_movie);
        //grab the extras passed before
        Bundle Coord = getIntent().getExtras();
        //assign them to variables
        double lat = Coord.getDouble("lat");
        double longi = Coord.getDouble("longi");

        TextView latiView = findViewById(R.id.latText);
        TextView longiView = findViewById(R.id.longiText);

        latiView.setText(Double.toString(lat));
        longiView.setText(Double.toString(longi));


}}
