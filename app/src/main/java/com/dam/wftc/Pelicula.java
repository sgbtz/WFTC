package com.dam.wftc;

public class Pelicula {

    private int movie_id;
    private String title;
    private String original_title;
    private String synopsis;
    private String poster_image;
    private double rating;



    public Pelicula(int id, String tit, String ori_tit, String syn, String img, double rat) {
        this.movie_id = id;
        this.title = tit;
        this.original_title = ori_tit;
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


    public String getORIGINAL_TITLE() {
        return original_title;
    }
    public void setORIGINAL_TITLE(String original_title) {
        this.original_title = original_title;
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
