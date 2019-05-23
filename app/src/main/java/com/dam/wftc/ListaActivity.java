package com.dam.wftc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link to-do} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ListaActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        final LibreriaBaseDatos libreria = new LibreriaBaseDatos(getApplicationContext());

        ArrayList<Pelicula> lista_peliculas = libreria.recuperarPELICULAS();
        lista = (ListView) findViewById(R.id.lista_peliculas);
        lista.setAdapter(new Lista_adaptador(this, R.layout.pelicula, lista_peliculas) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_contacto = (TextView) view.findViewById(R.id.textView_titulo);
                    if (texto_contacto != null)
                        texto_contacto.setText(((Pelicula) entrada).getTITLE());

                    TextView texto_telefono = (TextView) view.findViewById(R.id.textView_año);
                    if (texto_telefono != null)
                        texto_telefono.setText(((Pelicula) entrada).getYEAR());


                    TextView texto_ID = (TextView) view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Pelicula) entrada).getID()));

                }
            }
        });
    }
}
