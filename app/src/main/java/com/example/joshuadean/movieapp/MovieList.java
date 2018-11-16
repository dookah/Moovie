package com.example.joshuadean.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reactiveandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class MovieList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


        ListView lv = (ListView) findViewById(R.id.lv);

        List<movieDatabase> movies = Select.from(movieDatabase.class).fetch();

        List<String> movieTitles = new ArrayList<String>();

        for(int i = 0; i < movies.size(); i++){
            movieTitles.add(movies.get(i).getMovieTitle());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                movieTitles);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the text of the clicked element
                String item = ((TextView)view).getText().toString();
                //TODO On click open a page where it displays info about the movie and we can delete it from the database!
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
