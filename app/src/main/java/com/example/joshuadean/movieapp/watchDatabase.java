package com.example.joshuadean.movieapp;


import com.reactiveandroid.Model;
import com.reactiveandroid.annotation.Column;
import com.reactiveandroid.annotation.ForeignKeyAction;
import com.reactiveandroid.annotation.PrimaryKey;
import com.reactiveandroid.annotation.Table;
import com.reactiveandroid.annotation.Unique;

@Table(name = "watchDatabase", database = AppDatabase.class)
public class watchDatabase extends Model {

    @PrimaryKey
    private Long id;
    //One to one relationship, seen movies have a movie, latitude and longitude
    @Unique
    @Column(name = "movie")
    private movieDatabase movie;
    @Column(name = "lat")
    private double lat;
    @Column(name = "longi")
    private double longi;

    //default constructor needed
    public watchDatabase(){
    }
    //parameterised constructor
    public watchDatabase(movieDatabase movie, double lat, double longi){
        this.movie = movie;
        this.lat = lat;
        this.longi = longi;
    }
    public Long getId() {
        return id;
    }
    public movieDatabase getMovie(){
        return movie;
    }
    public double getLat(){
        return lat;
    }
    public double getLongi(){
        return longi;
    }

}
