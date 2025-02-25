package com.example.joshuadean.movieapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;
import android.provider.Settings.System;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;
import com.reactiveandroid.query.Select;


//Location Imports
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;


public class searchIntent extends AppCompatActivity {
    //Declare datbase helper
    //make variable for input
    private TextInputLayout textInputMovie;
    //Declare GSON object so i can use it to parse data
    Gson gson = new Gson();
    //Create a new request queue
    RequestQueue queue;
    //Set default latitude and longitudianl values
    double lat = 0;
    double longi = 0;
    //Make an object for encrypted preferences
    EncryptedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_intent);
        //Get the input box
        textInputMovie = findViewById(R.id.textInputLayout);
        //Make a volley request queue
        queue = Volley.newRequestQueue(this);
        //Request android Permission
        ActivityCompat.requestPermissions(searchIntent.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        //Assign instance of Secure shared preferences with the key being tbe android ID of the current phone
        pref = new EncryptedPreferences.Builder(this).withEncryptionPassword(System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)).build();
    }

    public void movieSearch(View view) {
        //get string inputted in the text box;
        final String movieRawInput = textInputMovie.getEditText().getText().toString();
        //convert it to movie api standard
        final String movieInput = movieRawInput.replace(" ", "+");
        //Convert the movie input into a URL that works with the movie api
        String URL = "http://www.omdbapi.com/?t=" + movieInput + "&apikey=567b015";

        // Request a string response from the provided URL using volley
        //Code learnt from https://developer.android.com/training/volley/simple
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Make a new movie object and let GSON parse the JSON object into that java object
                        Movies movie = gson.fromJson(response, Movies.class);
                        //Make a variable to hold what the reponse is from the API call
                        String responding = movie.getResponse();
                        //If the API doesnt find anything on their database go into this statment
                        if (responding.compareTo("False") == 0) {
                            //Check if the database has any movie in it that was searched
                            List<movieDatabase> returnedMovies = Select.from(movieDatabase.class).where("title = ?", movieRawInput).fetch();
                            //if the database doesnt have anything in it tell the user that no movies were found
                            if (returnedMovies.isEmpty() == true) {
                                Toast.makeText(searchIntent.this, "No movie found", Toast.LENGTH_SHORT).show();
                            } else { //Else take that database object and call the function that will render it on the screen
                                renderFromDB(returnedMovies);
                            }
                        } else { //If api api does return the results we can use that movie object made earlier with GSON
                            //fill in variables from the movie object, this will be added to the database
                            String title = movie.getTitle();
                            String year = movie.getYear();
                            String rated = movie.getRated();
                            String metascore = movie.getMetascore();
                            String imdbrating = movie.getImdbRating();
                            String posterURL = movie.getPoster();
                            String plot = movie.getPlot();
                            String runtime = movie.getRuntime();
                            //Have a default value for rotten tomatoes incase there isn't any score returned
                            //This is an example of why i dont parse the score to an int with GSON, Strings just work better for this app
                            String rottenTomatoesScore = "Score not found!";
                            //Check if there is a rotten tomatoes score.
                            if (movie.getRatings().size() > 1) {
                                rottenTomatoesScore = movie.getRatings().get(1).Value;
                            }
                            //Add the data returned from the API to the database
                            movieDatabase note = new movieDatabase(title, year, rated, metascore, imdbrating, posterURL, plot, runtime);
                            note.save();
                            renderPage(movie);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // -------------------- This will only enter if no Internet + no Cached response --------------------
                //Query the database with the searched string and assign it to a list
                List<movieDatabase> movieList = Select.from(movieDatabase.class).where("title = ?", movieRawInput).fetch();
                //Check if the database found anything
                if (movieList.isEmpty() != true) {
                    //render the database result to the screen
                    renderFromDB(movieList);
                    //Warn the user the movie database is being used instead of the API
                    Toast.makeText(searchIntent.this, "No Network Connection or Cached Results, Using Database!", Toast.LENGTH_SHORT).show();
                } else {
                    //Run if theres nothing in the database
                    Toast.makeText(searchIntent.this, "No Results in Cache or Database!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    //Function to add data to the screen, use a Movie Object
    public void renderPage(Movies movie) {
        //Update the movie Title on the page
        TextView titleArea = findViewById(R.id.titleBox);
        titleArea.setText(movie.getTitle());
        //Update the movie Title on the page
        TextView yearArea = findViewById(R.id.yearBox);
        yearArea.setText(movie.getYear());
        //Update the movie Title on the page
        TextView ratedArea = findViewById(R.id.ratedBox);
        ratedArea.setText(movie.getRated());
        //Update the movie Title on the page
        TextView metaArea = findViewById(R.id.metaBox);
        metaArea.setText(movie.getMetascore());
        //Update the movie Title on the page
        TextView imdbArea = findViewById(R.id.imdbBox);
        imdbArea.setText(movie.getImdbRating());
    }

    //Fires on seen Movie button press
    public void seenMovie(View view) {
        //Grab the current films title
        TextView nameBox = findViewById(R.id.titleBox);
        String name = nameBox.getText().toString();
        // Check if a valid movie as been searched before loading the addedMovie intent, better UX
        if (name != "") {


            //make a new intent to show a film has been saved
            Intent intent = new Intent(this, addedMovie.class);
            //TODO Add movie to seen SQLite table
            //getting a specific record of the currently searched movie
            movieDatabase currentMovie = Select.from(movieDatabase.class).where("title = ?", name).fetchSingle();

            //adds the movie to the seenDatabase if a movie is searched, using the parameterised constructor in the table for seen movies.
            seenDatabase seen = new seenDatabase(currentMovie,currentMovie.getMovieTitle() ,lat, longi);
            seen.save();

            //Bundle in the coordinates to pass them to the new intent
            Bundle coord = new Bundle();
            coord.putDouble("lat", lat);
            coord.putDouble("longi", longi);
            coord.putString("movieName", name);
            coord.putBoolean("context", false);
            intent.putExtras(coord);

            //Start the seen film page
            startActivity(intent);
        } else {
            //warn the user that they've not searched a valid movie so theres nothing to add.
            Toast.makeText(searchIntent.this, "Please search a movie!", Toast.LENGTH_SHORT).show();
        }
    }

    public void watchList(View view) {
        //Grab the current films title
        TextView nameBox = findViewById(R.id.titleBox);
        String name = nameBox.getText().toString();
        // Check if a valid movie as been searched before loading the addedMovie intent, better UX
        if (name != "") {
            //getting a specific record of the currently searched movie
            movieDatabase currentMovie = Select.from(movieDatabase.class).where("title = ?", name).fetchSingle();
            //adds the movie to the seenDatabase if a movie is searched, using the parameterised constructor in the table for seen movies.
            watchDatabase watch = new watchDatabase(currentMovie, name, lat, longi);
            watch.save();

            //make a new intent to show a film has been saved
            Intent intent = new Intent(this, addedMovie.class);

            //Bundle in the coordinates to pass them to the new intent
            Bundle coord = new Bundle();
            coord.putDouble("lat", lat);
            coord.putDouble("longi", longi);
            coord.putString("movieName", name);
            coord.putBoolean("context", true);
            intent.putExtras(coord);

            //Start the seen film page
            startActivity(intent);
        } else {
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
                    // ------------------- This all runs if permission is granted on the result --------------
                    // Make an instance of the Location Android Inbuilt API.
                    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                    //Use GPS to get location from provider
                    String locationProvider = LocationManager.GPS_PROVIDER;
                    //Get the last known location of the phone into an object, red squiggily line incorrect
                    //This line will never trigger if no permission due to being on correct result
                    Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                    //Check if the last known location is null, typically for emulators because they dont have a last known location
                    if (lastKnownLocation != null) {
                        //Set variables with lat and longi if not null
                        lat = lastKnownLocation.getLatitude();
                        longi = lastKnownLocation.getLongitude();
                    } else {
                        //Tell the user to open the maps to get a last known location.
                        Toast.makeText(searchIntent.this, "Please launch maps to get a last known location!", Toast.LENGTH_SHORT).show();
                    }
                    //Get the id for the text at the bottom of the app, this will be updated after the API Call
                    final TextView nearCin = findViewById(R.id.nearCinema);
                    //Make a URL with the URI Being the latitude and Longitude
                    String url = "https://api.cinelist.co.uk/search/cinemas/coordinates/" + lat + "/" + longi;
                    //Set up a string request using Volley with the url constructed above
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    cinemaLocation cinema = gson.fromJson(response, cinemaLocation.class);
                                    //Get the name and postcode of the nearest cinema
                                    String name = cinema.cinemas.get(0).name;
                                    String postcode = cinema.postcode;
                                    //Concatinate them to be saved
                                    String concat = name + " " + postcode;
                                    //Save the address into secure shared preferences encrypted, preventing them to be seen from the file explorer
                                    pref.edit().putString("address", concat).apply();
                                    //Retrieve and output them onto the screen
                                    String address = pref.getString("address","");
                                    nearCin.setText(address);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //If the api call doesnt work just retrieve the last known location from the shared preferences as a fallback
                            String address = pref.getString("address","");
                            if (address != null){
                                //Set the test to the last known location
                                nearCin.setText("Last Known : " + address);
                            }else {
                                //Set the text saying bad coordinates from the phone, typically due to being 0,0 from no maps launched
                                nearCin.setText("Invalid Co-Ordinates, please launch maps!");
                            }
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                } else {
                    //Warn the user they have denied the location permission
                    Toast.makeText(searchIntent.this, "Permission Denied. Locations will not be saved.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    public void renderFromDB(List<movieDatabase> returnedMovies){
        //make a new instance of my movie object
        Movies localMovie = new Movies();
        //Assign the movie object variables from the database List returned from the query
        localMovie.setTitle(returnedMovies.get(0).getMovieTitle());
        localMovie.setYear(returnedMovies.get(0).getYear());
        localMovie.setRated(returnedMovies.get(0).getRated());
        localMovie.setMetascore(returnedMovies.get(0).getMetaScore());
        localMovie.setImdbRating(returnedMovies.get(0).getImdbRating());
        //Pass the movie object to the render page function
        renderPage(localMovie);
    }
}
