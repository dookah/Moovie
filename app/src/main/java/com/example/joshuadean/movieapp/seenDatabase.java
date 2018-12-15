package com.example.joshuadean.movieapp;


import com.reactiveandroid.Model;
import com.reactiveandroid.annotation.Column;
import com.reactiveandroid.annotation.ForeignKeyAction;
import com.reactiveandroid.annotation.PrimaryKey;
import com.reactiveandroid.annotation.Table;
import com.reactiveandroid.annotation.Unique;

@Table(name = "seenDatabase", database = AppDatabase.class)
public class seenDatabase extends Model {

    @PrimaryKey
    private Long id;
    //One to one relationship, seen movies have a movie, latitude and longitude
    @Unique
    @Column(name = "movie", onDelete = ForeignKeyAction.CASCADE)
    private movieDatabase movie;
    @Column(name = "name")
    private String name;
    @Column(name = "lat")
    private double lat;
    @Column(name = "longi")
    private double longi;

    //default constructor needed
    public seenDatabase(){
    }
    //parameterised constructor
    public seenDatabase(movieDatabase movie,String name, double lat, double longi){
        this.movie = movie;
        this.lat = lat;
        this.longi = longi;
        this.name = name;
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
    public String getName(){
        return name;
    }

}
