package com.dam.wftc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class LibreriaBaseDatos extends SQLiteOpenHelper {


    private static final int VERSION_BASEDATOS = 1;
    private static final String title_BASEDATOS = "libreria.db";
    private static final String TABLA_PELICULAS ="CREATE TABLE IF NOT EXISTS peliculas " +
            " (movie_id INTEGER PRIMARY KEY, title TEXT, year TEXT, synopsis TEXT, poster_image TEXT, rating DOUBLE)";

    public LibreriaBaseDatos(Context context) {
        super(context, title_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_PELICULAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLA_PELICULAS);
        onCreate(db);
    }




    public boolean insertarPELICULA(Pelicula peli) {
        long salida=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            if(peli.getID()!=0)
                valores.put("movie_id", peli.getID());
            valores.put("title", peli.getTITLE());
            valores.put("year", peli.getYEAR());
            valores.put("synopsis", peli.getSYNOPSIS());
            valores.put("poster_image", peli.getIMAGE());
            valores.put("rating", peli.getRATING());
            try {
                salida = db.insert("peliculas", null, valores);
            } catch(SQLException e) {
                Log.d("error", e.toString());
            }
        }
        db.close();
        return(salida>0);
    }

    public boolean  modificarPELICULA(int id, String tit, String year, String syn, String img, int rat){
        long salida=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("movie_id", id);
            valores.put("title", tit);
            valores.put("year", year);
            valores.put("synopsis", syn);
            valores.put("poster_image", img);
            valores.put("rating", rat);
            salida=db.update("peliculas", valores, "movie_id=" + id, null);
        }
        db.close();
        return(salida>0);
    }

    public boolean  borrarPELICULA(int id) {
        SQLiteDatabase db = getWritableDatabase();
        long salida=0;
        if (db != null) {
            salida=db.delete("peliculas", "movie_id=" + id, null);
        }
        db.close();
        return(salida>0);
    }

    public void limpiarPELICULAS() {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("DELETE FROM peliculas");
        }
        db.close();
    }

    public Pelicula recuperarPELICULA(int id) {
        Pelicula pelicula = null;
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"movie_id", "title" , "year" , "synopsis" , "poster_image" , "rating" };
        Cursor c = db.query("peliculas", valores_recuperar, "movie_id=" + id, null, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0)
            pelicula = new Pelicula(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                    c.getString(4), c.getDouble(5), true);

        db.close();
        c.close();
        return pelicula;
    }


    public ArrayList<Pelicula> recuperarPELICULAS() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Pelicula> lista_peliculas = new ArrayList<Pelicula>();
        String[] valores_recuperar = {"movie_id", "title" , "year" , "synopsis" , "poster_image" , "rating" };
        Cursor c = db.query("peliculas", valores_recuperar, null, null, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                Pelicula pelicula = new Pelicula(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getString(4), c.getDouble(5), true);
                lista_peliculas.add(pelicula);
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return lista_peliculas;
    }
}












