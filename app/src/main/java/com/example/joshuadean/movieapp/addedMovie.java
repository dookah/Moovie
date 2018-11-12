package com.example.joshuadean.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class addedMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_movie);

        TextView latiView = findViewById(R.id.latText);
        TextView longiView = findViewById(R.id.longiText);


}}
