package com.example.joshuadean.movieapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


public class searchIntent extends AppCompatActivity {
    //Declare GSON object so i can use it to parse data
    //make variable for input
    private TextInputLayout textInputMovie;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_intent);
        textInputMovie = findViewById(R.id.textInputLayout);

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
                        // Display the first 500 characters of the response string.
                        String movieResponse =  response;
                        Movies movie = gson.fromJson(movieResponse, Movies.class); //Use GSON to convert JSON to a java object
                        //pass the java object with the current movie object
                        renderPage(movie);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    }
}
