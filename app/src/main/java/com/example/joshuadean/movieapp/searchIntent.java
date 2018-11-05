package com.example.joshuadean.movieapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    }
}
