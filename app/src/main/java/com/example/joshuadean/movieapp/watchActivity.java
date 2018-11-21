package com.example.joshuadean.movieapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.reactiveandroid.query.Delete;
import com.reactiveandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class watchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        SwipeMenuListView lv = (SwipeMenuListView)findViewById(R.id.lv);

        List<watchDatabase> movies = Select.from(watchDatabase.class).fetch();

        final List<String> movieTitles = new ArrayList<String>();

        for(int i = 0; i < movies.size(); i++){
            movieTitles.add(movies.get(i).getMovie().getMovieTitle());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                movieTitles);

        lv.setAdapter(arrayAdapter);
        //Make instance of swipe menu library
        //Boilerplate code from SwipeMenuListView Library Documentation - https://github.com/baoyongzhang/SwipeMenuListView
        SwipeMenuCreator creator = new SwipeMenuCreator() {
        //Create swipe menu options
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(250);
                // set item title
                openItem.setTitle("Seen");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };

        // set creator
        lv.setMenuCreator(creator);

        //handle the clicks
        //Boilerplate code from SwipeMenuListView Library Documentation - https://github.com/baoyongzhang/SwipeMenuListView
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //get the clicked title
                String item = movieTitles.get(position);
                //When you click the first button
                switch (index) {
                    case 0:

                        //Get the movie item
                        movieDatabase movieListing = Select.from(movieDatabase.class).where("title = ?", item).fetchSingle();
                        //Search the watch database with that item
                        watchDatabase watchedListing = Select.from(watchDatabase.class).where("movie = ?", movieListing).fetchSingle();
                        //Insert it into Seen movie
                        seenDatabase insertMovie = new seenDatabase(movieListing, 2, 3);
                        insertMovie.save();
                        //deleting record with the movie item

                        //TODO Return home
                        break;
                }
                return false;
            }
        });

        //called when you click on one of the list view items
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<watchDatabase> notes = Select.from(watchDatabase.class).fetch();
                //get the clicked title by getting the position in the array
                String item = movieTitles.get(position);
                Toast.makeText(watchActivity.this, item, Toast.LENGTH_SHORT).show();
                //new intent for movie info
                Intent intent = new Intent(view.getContext(), movieInfo.class);
                //Create a new bundle to hold the movie information
                Bundle movieInfo = new Bundle();
                //put the clicked title in the bundle
                movieInfo.putString("title", item);
                //put the title in the intent
                intent.putExtras(movieInfo);

                //Start the new intent
                startActivity(intent);
                //Go back to the home page if the movie info intent finishes
                finish();
            }
        });


    }
}
