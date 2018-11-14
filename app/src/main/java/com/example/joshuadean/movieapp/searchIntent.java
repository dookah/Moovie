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
import com.reactiveandroid.query.Select;


//Location Imports
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.List;


public class searchIntent extends AppCompatActivity {
    //Declare datbase helper




    //make variable for input
    private TextInputLayout textInputMovie;

    //Declare GSON object so i can use it to parse data
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
        final String movieRawInput = textInputMovie.getEditText().getText().toString();
        //convert it to movie api standard
        final String movieInput = movieRawInput.replace(" ", "+");
        //Convert the movie input into a URL that works with the movie api
        String URL = "http://www.omdbapi.com/?t=" + movieInput + "&apikey=567b015";

        //Make a volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL using volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If no internet connection, cached response will be given.
                        Movies movie = gson.fromJson(response, Movies.class); //Use GSON to convert JSON to a java object
                        //Make a variable to hold if theres a response
                        String responding = movie.Response;

                        String title = movie.Title;
                        String year = movie.Year;
                        String rated = movie.Rated;
                        String metascore = movie.Metascore;
                        String imdbrating = movie.imdbRating;
                        //Have to use compare to since object strings are different to normal strings apparently :@
                        if(responding.compareTo("False") == 0){
                            //TODO : Check if there's a movie with this title in the database
                            //query the database for the searched title
                            List<movieDatabase> returnedMovies = Select.from(movieDatabase.class).where("title = ?", movieRawInput).fetch();
                            // Check if
                            if(returnedMovies.isEmpty() == true){
                                // -------------------- This will trigger if the API cant find the movie --------------------
                                Toast.makeText(searchIntent.this, "No movie found, please add it!", Toast.LENGTH_SHORT).show();
                            }else{
                                Movies localMovie = new Movies();
                                //Assign the movie object variables from the database List returned from the query
                                localMovie.Title = returnedMovies.get(0).getMovieTitle();
                                localMovie.Year = returnedMovies.get(0).getYear();
                                localMovie.Rated = returnedMovies.get(0).getRated();
                                localMovie.Metascore = returnedMovies.get(0).getMetaScore();
                                localMovie.imdbRating = returnedMovies.get(0).getImdbRating();
                                //Pass the movie object to the render page function
                                renderPage(localMovie);
                            }
                        }else {
                            //adds the searched movie to my SQLite database with the returned data, can search this offline!
                            movieDatabase note = new movieDatabase(title, year, rated, metascore, imdbrating);
                            note.save();

                            renderPage(movie);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // -------------------- This will only enter if no Internet + no Cached response --------------------
                //Query the database with the searched string and assign it to a list
                List<movieDatabase> note = Select.from(movieDatabase.class).where("title = ?", movieRawInput).fetch();

                //Check if the database found anything
                if(note.isEmpty() != true) {
                    //Make a new movie object so i can pass it to render page
                    Movies movie = new Movies();
                    //Assign the movie object variables from the database List returned from the query
                    movie.Title = note.get(0).getMovieTitle();
                    movie.Year = note.get(0).getYear();
                    movie.Rated = note.get(0).getRated();
                    movie.Metascore = note.get(0).getMetaScore();
                    movie.imdbRating = note.get(0).getImdbRating();
                    //Pass the movie object to the render page function
                    renderPage(movie);
                    //Warn the user the movie database is being used instead of the API
                    Toast.makeText(searchIntent.this, "No Network Connection or Cached Results, Using Database!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(searchIntent.this, "No Results in Cache or Database!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        stringRequest.setShouldCache(false);
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
        // Check if a valid movie as been searched before loading the addedMovie intent, better UX
        if (name != "") {

            //Check if permission has been granted to look for locations, if not skip it and use 0,0
            if (ContextCompat.checkSelfPermission(searchIntent.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                // Make an instance of the Location Android Inbuilt API.
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                //Use GPS to get location from provider
                String locationProvider = LocationManager.GPS_PROVIDER;
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                if(lastKnownLocation != null) {
                    //New vairables to hold Lat and Long of phone
                    lat = lastKnownLocation.getLatitude();
                    longi = lastKnownLocation.getLongitude();
                }
                else{
                    Toast.makeText(searchIntent.this, "Please launch maps to get a last known location!", Toast.LENGTH_SHORT).show();
                }
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
            //warn the user that they've not searched a valid movie so theres nothing to add.
            Toast.makeText(searchIntent.this, "Please search a movie!", Toast.LENGTH_SHORT).show();
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
