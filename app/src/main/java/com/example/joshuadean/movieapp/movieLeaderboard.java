package com.example.joshuadean.movieapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class movieLeaderboard extends AppCompatActivity {
    //Initilise the Database
    private DatabaseReference mDatabase;
    //Initilise the Listview
    private ListView listView;
    //List for holding the scores
    private ArrayList<String> mScores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_leaderboard);

        //Make an instance of the firebase database that was initilised up.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Get the listview by the ID
        listView = findViewById(R.id.listView);
        //Use an array adapter on the list of the scores to get it in a listview
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mScores);
        listView.setAdapter(arrayAdapter);


        //Learnt from the docs found here : https://firebase.google.com/docs/database/android/lists-of-data
        //Listens to the Firebase update for updates and runs
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Runs when item added to database
                //Get the current values
                String value = dataSnapshot.getValue(String.class);
                //add them to an array
                mScores.add(value);
                Collections.sort(mScores);
                Collections.reverse(mScores);
                //Update the array adapater
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Runs when item changed in the database
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            //Runs when moved
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

