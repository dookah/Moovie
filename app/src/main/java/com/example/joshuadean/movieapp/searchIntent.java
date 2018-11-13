package com.example.joshuadean.movieapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

//Location Imports
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class searchIntent extends AppCompatActivity {
    //Declare GSON object so i can use it to parse data
    //make variable for input
    private TextInputLayout textInputMovie;
    Gson gson = new Gson();

    double lat = 0;
    double longi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_intent);
        textInputMovie = findViewById(R.id.textInputLayout);

        //Request android Permission
        ActivityCompat.requestPermissions(searchIntent.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }

    public void movieSearch(View view){
        //get string inputted in the text box;
        String movieRawInput = textInputMovie.getEditText().getText().toString();
        //convert it to movie api standard
        String movieInput = movieRawInput.replace(" ", "+");
        //Convert the movie input into a URL that works with the movie api
        String URL = "http://www.omdbapi.com/?t=" + movieInput + "&apikey=567b015";

        //Make a volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Movies movie = gson.fromJson(response, Movies.class); //Use GSON to convert JSON to a java object
                        if(movie.Title == null){
                            Toast.makeText(searchIntent.this, "Movie not found, please try again!", Toast.LENGTH_SHORT).show();
                        }else {
                            renderPage(movie);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(searchIntent.this,
                        "No Network, Using Cache!", Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
    public void renderPage(Movies movie){
        //Update the movie Title on the page
        TextView titleArea = findViewById(R.id.titleBox);
        titleArea.setText(movie.Title);
        //Update the movie Title on the page
        TextView yearArea = findViewById(R.id.yearBox);
        yearArea.setText(movie.Year);
        //Update the movie Title on the page
        TextView ratedArea = findViewById(R.id.ratedBox);
        ratedArea.setText(movie.Rated);
        //Update the movie Title on the page
        TextView metaArea = findViewById(R.id.metaBox);
        metaArea.setText(movie.Metascore);
        //Update the movie Title on the page
        TextView imdbArea = findViewById(R.id.imdbBox);
        imdbArea.setText(movie.imdbRating);
    }

    //Fires on seen Movie button press
    public void seenMovie(View view) {
        //Grab the current films title
        TextView nameBox = findViewById(R.id.titleBox);
        String name = nameBox.getText().toString();
        // Check if a valid movie as been searched before loading the complete intent
        if (name != "") {
            //Check if permission has been granted to look for locations, if not skip it and use 0,0
            if (ContextCompat.checkSelfPermission(searchIntent.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Make an instance of the Location Android Inbuilt API.
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                //Use GPS to get location from provider
                String locationProvider = LocationManager.GPS_PROVIDER;
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                //New vairables to hold Lat and Long of phone
                lat = lastKnownLocation.getLatitude();
                longi = lastKnownLocation.getLongitude();
            }
            //make a new intent to show a film has been saved
            Intent intent = new Intent(this, addedMovie.class);

            //Bundle in the coordinates to pass them to the new intent
            Bundle coord = new Bundle();
            coord.putDouble("lat", lat);
            coord.putDouble("longi", longi);
            coord.putString("movieName", name);
            coord.putString("context", "Seen Movies!");
            intent.putExtras(coord);

            //Start the seen film page
            startActivity(intent);
        } else{
            Toast.makeText(searchIntent.this, "Please seach a movie!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(searchIntent.this, "Permission Granted!.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                    Toast.makeText(searchIntent.this, "Permission Denied. Locations will not be saved.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
