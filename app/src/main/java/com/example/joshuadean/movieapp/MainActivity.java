package com.example.joshuadean.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void searchIntent(View view){
        Intent intent = new Intent(this,searchIntent.class);
        startActivity(intent);
    }
    public void savedIntent(View view){
        Intent intent = new Intent(this,savedIntent.class);
        startActivity(intent);
    }
}
