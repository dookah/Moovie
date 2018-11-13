package com.example.joshuadean.movieapp;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class movieDatabase extends SugarRecord{
    @Unique
    String title;
    String year;
    String rated;
    String metascore;
    String imdbrating;

    //default constructor
    public movieDatabase(){
        title = "";
        year = "";
        rated = "";
        metascore = "";
        imdbrating = "";

    }
    //Parameterised Constructor
    public movieDatabase(String title, String year, String rated, String metascore, String imdbrating){
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.metascore = metascore;
        this.imdbrating = imdbrating;
    }
}
