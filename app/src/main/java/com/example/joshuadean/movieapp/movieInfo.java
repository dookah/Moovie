package com.example.joshuadean.movieapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reactiveandroid.query.Delete;
import com.reactiveandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.Calendar;
import java.util.List;

public class movieInfo extends AppCompatActivity {
    String movieTitle;
    String moviePlot;
    double lat = 0;
    double longi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        //Request android Permission
        ActivityCompat.requestPermissions(movieInfo.this,
                new String[]{Manifest.permission.WRITE_CALENDAR},
                1);

        //get the passed bundle
        Bundle movie = getIntent().getExtras();
        //get the string out of the bundle with the key
        movieTitle = movie.getString("title");
        String source = movie.getString("source");
        //Query the database with the passed in string
        List<movieDatabase> movieListing = Select.from(movieDatabase.class).where("title = ?", movieTitle).fetch();
        //grab the poster URL
        String posterUrl = movieListing.get(0).getPosterURL();
        moviePlot = movieListing.get(0).getPlot();

        //Get the image and title view
        ImageView imageView= findViewById(R.id.imageView3);
        TextView titleArea = findViewById(R.id.title);
        //set the title to the passed in title
        titleArea.setText(movieTitle);
        //Underline the title
        titleArea.setPaintFlags(titleArea.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        //Use Picasso to load the poster Url from the movieListing database and place it into the imageView defined above
        Picasso.get().load(posterUrl).into(imageView);
        Button locationButton = findViewById(R.id.location);
        //if the source is watch we dont need to show where you saw the film so we hide that button
        if(source.equals("watch")) {
            //Get the button by ID
            Button calanderButton = findViewById(R.id.calanderButton);
            //Set visibility to 0
            locationButton.setVisibility(View.GONE);

            calanderButton.setVisibility(View.VISIBLE);
        } //If it doesnt come from the watchList, itll run this next conditional loop (From seen)
        else if(source.equals("seen")){
            List<seenDatabase> seenListing = Select.from(seenDatabase.class).where("name = ?", movieTitle).fetch();
             lat = seenListing.get(0).getLat();
             longi = seenListing.get(0).getLongi();
        }
        else{
            locationButton.setVisibility(View.GONE);
        }
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
        frag.show(this.getSupportFragmentManager(),"plot");
    }

    public void showLocation(View view){
        Intent intent = new Intent(this, filmLocation.class);
        //Bundle in the coordinates to pass them to the new intent
        Bundle Coord = new Bundle();
        Coord.putDouble("lat", lat);
        Coord.putDouble("longi", longi);
        intent.putExtras(Coord);
        startActivity(intent);
    }
    public void addEvent(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No calender Permission!", Toast.LENGTH_SHORT).show();
        }else {
            //Learnt from https://developer.android.com/guide/topics/providers/calendar-provider
            ContentResolver contentResolver = this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CalendarContract.Events.TITLE, "Watch Movie : " + movieTitle);
            contentValues.put(CalendarContract.Events.DESCRIPTION, "You used Moo-vie to Book this Movie Time-Slot!");
            contentValues.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
            contentValues.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis() + 60 + 60 * 1000);
            contentValues.put(CalendarContract.Events.CALENDAR_ID, 3);
            contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
            //Insert the calander in, requires permissions
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);

            Toast.makeText(this, "Thanks! Added to your Google Calander.", Toast.LENGTH_SHORT).show();
        }
    }
}
