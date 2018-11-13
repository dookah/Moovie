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
        String name = Coord.getString("movieName");
        String context = Coord.getString("context");

        TextView latiView = findViewById(R.id.latText);
        TextView longiView = findViewById(R.id.LongiText);
        TextView ps = findViewById(R.id.ps);




        latiView.setText(Double.toString(lat));
        longiView.setText(Double.toString(longi));

        ps.setText(name + " is now in your " + context);


}}
