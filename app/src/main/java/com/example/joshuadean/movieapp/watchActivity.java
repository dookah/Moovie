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

        SwipeMenuListView lv = findViewById(R.id.lv);

        Toast.makeText(watchActivity.this, "Tip : swipe left to mark as seen.", Toast.LENGTH_SHORT).show();

        List<watchDatabase> movies = Select.from(watchDatabase.class).fetch();

        final List<String> movieTitles = new ArrayList<String>();

        for(int i = 0; i < movies.size(); i++){
            movieTitles.add(movies.get(i).getMovie().getMovieTitle());
        }
        //Get the ID of the textview box that will hold the score
        TextView numToWatch = findViewById(R.id.numWatch);
        //set the text to the current length of movies in the watchDatabse
        //Since it's calcuted from the size of SQLITE it doesnt need to be saved persistently.
        numToWatch.setText("Movies to Watch : " + movieTitles.size());

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
                        //Make a movie object of the movie that was clicked on
                        movieDatabase movieListing = Select.from(movieDatabase.class).where("title = ?", item).fetchSingle();
                        //fetch all the watched database listings into a list
                        List<watchDatabase> watchedMovies = Select.from(watchDatabase.class).fetch();
                        //Linear Search looking to match the movie object to match the watched database object
                        for(int i = 0; i < watchedMovies.size(); i++) {
                            //make an object of the current item of the
                            movieDatabase movie = watchedMovies.get(i).getMovie();
                            //use that objects id to find the originally found movie id.
                            if (movie.getId() == movieListing.getId()) {
                                //Object of the found movie that matched the clicked item
                                watchDatabase watched = watchedMovies.get(i);
                                //Make a new item in the seen database with the found object
                                seenDatabase insertMovie = new seenDatabase(watched.getMovie(),watched.getName(),watched.getLat(),watched.getLongi());
                                //save it in the database
                                insertMovie.save();//delete it from the to-watch database
                                Delete.from(watchDatabase.class).where("name = ?", item).execute();


                            }
                        }
                        //deleting record with the movie item
                        //TODO Return home
                        break;
                }
                // ------------- Used to reload the page on delete button click ---------------
                finish();
                //Override the animation to stop flashing
                overridePendingTransition(0, 0);
                //Start the intent again
                startActivity(getIntent());
                //Override the animation of fading back in.
                overridePendingTransition(0, 0);
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
                //new intent for movie info
                Intent intent = new Intent(view.getContext(), movieInfo.class);
                //Create a new bundle to hold the movie information
                Bundle movieInfo = new Bundle();
                //put the clicked title in the bundle
                movieInfo.putString("title", item);
                movieInfo.putString("source", "watch");
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
