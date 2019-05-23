package com.dam.wftc;

public class Pelicula {

    private int movie_id;
    private String title;
    private String year;
    private String synopsis;
    private String poster_image;
    private double rating;



    public Pelicula(int id, String tit, String year, String syn, String img, double rat) {
        this.movie_id = id;
        this.title = tit;
        this.year = year;
        this.synopsis = syn;
        this.poster_image = img;
        this.rating = rat;
    }



    public int getID() {
        return movie_id;
    }
    public void setID(int id) {
        this.movie_id = id;
    }


    public String getTITLE() {
        return title;
    }
    public void setTITLE(String title) {
        this.title = title;
    }


    public String getYEAR() {
        return year;
    }
    public void setYEAR(String year) {
        this.year = year;
    }

    public String getSYNOPSIS() {
        return synopsis;
    }
    public void setSYNOPSIS(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getIMAGE() {
        return poster_image;
    }
    public void setIMAGE(String poster_image) {
        this.poster_image = poster_image;
    }

    public double getRATING() {
        return rating;
    }
    public void setRATING(double rating) {
        this.rating = rating;
    }
}
