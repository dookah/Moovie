package com.example.joshuadean.movieapp;

import java.lang.reflect.Array;
import java.util.List;

public class Movies {
    //I read these in as strings as the primary function will just be outputting the data
    //I could read the JSON in with volley as JSONObject but no need to.
    //Gson can interperet the string data
    public String Title;
    public String Year;
    public String Rated;
    public String Released;
    public String Runtime;
    public String Genre;
    public String Director;
    public String Writer;
    public String Actors;
    public String Plot;
    public String Language;
    public String Country;
    public String Awards;
    public String Poster;
    //Since the Ratings in the JSON is an array of javascript objects, GSON will convert this to a list of a class
    //the class is decalared below
    public List<ArrayData> Ratings;
    public String Metascore;
    public String imdbRating;
    public String imdbVotes;
    public String imdbID;
    public String Type;
    public String DVD;
    public String BoxOffice;
    public String Production;
    public String Website;
    public String Response;
    public String Error;
}
//class to hold the JSON array data
class ArrayData{
    String Source;
    String Value;
}
