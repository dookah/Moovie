package com.example.joshuadean.movieapp;


import com.reactiveandroid.Model;
import com.reactiveandroid.annotation.Column;
import com.reactiveandroid.annotation.PrimaryKey;
import com.reactiveandroid.annotation.Table;
import com.reactiveandroid.annotation.Unique;

@Table(name = "movieDatabase", database = AppDatabase.class )
public class movieDatabase extends Model {
    @PrimaryKey
    private Long id;
    @Unique
    @Column(name = "title")
    private String title;
    @Column(name = "year")
    private String year;
    @Column(name = "rated")
    private String rated;
    @Column(name = "metascore")
    private String metascore;
    @Column(name = "imdbrating")
    private String imdbrating;
    @Column(name="plot")
    private String plot;
    @Column(name = "posterURL")
    private String posterURL;

    //default constructor
    public movieDatabase(){

    }

    //Parameterised Constructor
    public movieDatabase(String title, String year, String rated, String metascore, String imdbrating, String posterURL, String plot){
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.metascore = metascore;
        this.imdbrating = imdbrating;
        this.posterURL = posterURL;
        this.plot = plot;
    }
    public Long getId() {
        return id;
    }
    public String getMovieTitle(){
        return title;
    }
    public String getYear(){
        return year;
    }
    public String getRated(){
        return rated;
    }
    public String getMetaScore(){
        return metascore;
    }
    public String getImdbRating(){
        return imdbrating;
    }
    public String getPosterURL() { return posterURL; }
    public String getPlot() { return plot; }

}
