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

    RequestQueue queue;

    double lat = 0;
    double longi = 0;
    EncryptedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_intent);
        textInputMovie = findViewById(R.id.textInputLayout);
        //Make a volley request queue
        queue = Volley.newRequestQueue(this);

        //Request android Permission
        ActivityCompat.requestPermissions(searchIntent.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        //Make a new instance of Secured shared preferences with the key being tbe android ID of the current phone
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If no internet connection, cached response will be given.
                        Movies movie = gson.fromJson(response, Movies.class); //Use GSON to convert JSON to a java object
                        //Make a variable to hold if theres a response
                        String responding = movie.getResponse();

                        String title = movie.getTitle();
                        String year = movie.getYear();
                        String rated = movie.getRated();
                        String metascore = movie.getMetascore();
                        String imdbrating = movie.getImdbRating();
                        String posterURL = movie.getPoster();
                        String plot = movie.getPlot();
                        //Have to use compare to since object strings are different to normal strings apparently :@
                        if (responding.compareTo("False") == 0) {

                            //query the database for the searched title
                            List<movieDatabase> returnedMovies = Select.from(movieDatabase.class).where("title = ?", movieRawInput).fetch();
                            // Check if
                            if (returnedMovies.isEmpty() == true) {
                                // -------------------- This will trigger if the API cant find the movie --------------------
                                Toast.makeText(searchIntent.this, "No movie found, please add it!", Toast.LENGTH_SHORT).show();
                            } else {
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
                        } else {
                            //adds the searched movie to my SQLite database with the returned data, can search this offline!
                            movieDatabase note = new movieDatabase(title, year, rated, metascore, imdbrating, posterURL, plot);
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
                if (note.isEmpty() != true) {
                    //Make a new movie object so i can pass it to render page
                    Movies movie = new Movies();
                    //Assign the movie object variables from the database List returned from the query
                    movie.setTitle(note.get(0).getMovieTitle());
                    movie.setYear(note.get(0).getYear());
                    movie.setRated(note.get(0).getRated());
                    movie.setMetascore(note.get(0).getMetaScore());
                    movie.setImdbRating(note.get(0).getImdbRating());
                    //Pass the movie object to the render page function
                    renderPage(movie);
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
                                    //Save them in secure shared preferences
                                    pref.edit().putString("address", concat).apply();
                                    //Retrieve and output them
                                    String address = pref.getString("address","");
                                    nearCin.setText(address);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String address = pref.getString("address","");
                            if (address != null){
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
    public String generateSalt(){
        //Get the phones androidID
        //Using a salt to avoid rainbow table attacks
        String ID = System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //return it, this'll act as the salt
        return ID;
    }
    public String encrypt(String toEncrypt){
        //Generate salt with method used above
       String salt = generateSalt();
       //Before hashing concatinate the string and the salt
       String toHash = toEncrypt + salt;
       //Use Base64 to encode the concatinated string
       String hash = Base64.encodeToString(toHash.getBytes(), Base64.DEFAULT);
       //Return the encoded value
       return hash;
    }
    public String dehash(String toDehash){
        //De-encode the string using  base64
        return new String(Base64.decode(toDehash, Base64.DEFAULT));
    }
    public String decrypt(String toDecrypt){
        //Call the dehash function to get rid of the hash
        String Dehashed = dehash(toDecrypt);
        //Decrypt it by removing the known salt based on the android ID device, if different ID it wont get rid of the salt
        String Decrypt = Dehashed.replace(System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID),"");
        //Return the decrypted value
        return Decrypt;
    }
}
