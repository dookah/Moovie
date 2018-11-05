package com.example.joshuadean.movieapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class searchIntent extends AppCompatActivity {
    //make variable for input
    private TextInputLayout textInputMovie;

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
                        String responsed =  response.substring(0,5000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
}
