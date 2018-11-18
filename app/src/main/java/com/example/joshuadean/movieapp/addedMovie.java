package com.example.joshuadean.movieapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class addedMovie extends AppCompatActivity {
    //hold the variables in the class scope for lat and long
    double lat;
    double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_movie);

        //grab the extras passed before
        Bundle Coord = getIntent().getExtras();
        //assign them to the location variables
        lat = Coord.getDouble("lat");
        longi = Coord.getDouble("longi");
        String name = Coord.getString("movieName");
        //Holds weather added to wishlist or watchlist
        boolean context = Coord.getBoolean("context");

        TextView ps = findViewById(R.id.ps);
        ps.setText(name + " is now in your " + context);
}
    public void startMaps(View view){
        Intent intent = new Intent(this, filmLocation.class);

        //Bundle in the coordinates to pass them to the new intent
        Bundle Coord = new Bundle();
        Coord.putDouble("lat", lat);
        Coord.putDouble("longi", longi);
        intent.putExtras(Coord);
        startActivity(intent);
    }
}
