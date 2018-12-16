package com.example.joshuadean.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class movieLeaderboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_leaderboard);

        DatabaseReference leaderboard = FirebaseDatabase.getInstance().getReference();
        DatabaseReference chatSpaceRef = rootRef.child("ChatSpace");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String msg = ds.child("msg").getValue(String.class);
                    Log.d("TAG", msg);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        chatSpaceRef.addListenerForSingleValueEvent(eventListener);
    }
}
