package com.example.joshuadean.movieapp;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactiveandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class seenActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen);

        ListView lv = findViewById(R.id.lv);

        List<seenDatabase> movies = Select.from(seenDatabase.class).fetch();

        List<String> movieTitles = new ArrayList<String>();

        for(int i = 0; i < movies.size(); i++){
            movieTitles.add(movies.get(i).getMovie().getMovieTitle());
        }


        // Write a message to the database
        String id = Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String score = Integer.toString(movieTitles.size());
        mDatabase.child(id).setValue(score);



        //Get the ID of the textview box that will hold the score
        final TextView highScore = findViewById(R.id.score);
        //set the text to the current length of movies in the watchDatabse
        //Since it's calcuted from the size of SQLITE it doesnt need to be saved persistently.
        highScore.setText("High Score : " + movieTitles.size());

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
                movieInfo.putString("source", "seen");
                //put the title in the intent
                intent.putExtras(movieInfo);

                //Start the new intent
                startActivity(intent);
                //Go back to the home page if the movie info intent finishes
                finish();

            }
        });


    }
    public void viewLeaderboard(View view){
        Intent intent = new Intent(this, movieLeaderboard.class);
        startActivity(intent);
    }

}
