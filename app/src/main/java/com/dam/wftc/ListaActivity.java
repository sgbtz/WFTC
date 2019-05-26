package com.dam.wftc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.util.ArrayList;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link to-do} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ListaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TMDB tmdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libreria_list);

        tmdb = new TMDB();

        final LibreriaBaseDatos libreria = new LibreriaBaseDatos(getApplicationContext());

        ArrayList<Pelicula> lista_peliculas = libreria.recuperarPELICULAS();

        recyclerView = findViewById(R.id.RecyclerView_lista_peliculas);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ListaAdaptador listaAdaptador = new ListaAdaptador(lista_peliculas, tmdb, libreria);
        recyclerView.setAdapter(listaAdaptador);

    }

    public void irBuscarPeliculas(View view) {
        Intent intent = new Intent(this, BuscarPeliculasActivity.class);
        startActivity(intent);
    }
}
