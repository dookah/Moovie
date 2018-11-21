package com.example.joshuadean.movieapp;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reactiveandroid.query.Delete;
import com.reactiveandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.util.List;

public class movieInfo extends AppCompatActivity {
    String movieTitle;
    String moviePlot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        //get the passed bundle
        Bundle movie = getIntent().getExtras();
        //get the string out of the bundle with the key
        movieTitle = movie.getString("title");
        //Query the database with the passed in string
        List<movieDatabase> movieListing = Select.from(movieDatabase.class).where("title = ?", movieTitle).fetch();
        //grab the poster URL
        String posterUrl = movieListing.get(0).getPosterURL();
        moviePlot = movieListing.get(0).getPlot();

        //Get the image and title view
        ImageView imageView=(ImageView) findViewById(R.id.imageView3);
        TextView titleArea = findViewById(R.id.title);
        //set the title to the passed in title
        titleArea.setText(movieTitle);
        //Underline the title
        titleArea.setPaintFlags(titleArea.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        Picasso.get().load(posterUrl).into(imageView);

    }
    public void deleteCurrent(View view){
        //deleting record
        Delete.from(movieDatabase.class).where("title = ?", movieTitle).execute();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Movie Deleted from database!", Toast.LENGTH_SHORT).show();

    }
    public void showPlot (View view){
        Bundle plotBundle = new Bundle();
        plotBundle.putString("plot", moviePlot);

        dialogFragment frag = new dialogFragment();
        frag.setArguments(plotBundle);
        frag.show(this.getSupportFragmentManager(),"Time Picker");

    }
}
