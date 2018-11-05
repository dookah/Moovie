package com.example.joshuadean.movieapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.bigTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/happycow.ttf");
        textView.setTypeface(typeface);
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
