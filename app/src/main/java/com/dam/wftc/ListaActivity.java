package com.dam.wftc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private TMDB tmdb;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libreria_list);

        tmdb = new TMDB();

        final LibreriaBaseDatos libreria = new LibreriaBaseDatos(getApplicationContext());

        ArrayList<Pelicula> lista_peliculas = libreria.recuperarPELICULAS();
        lista = findViewById(R.id.ListView_lista_peliculas);
        lista.setAdapter(new Lista_adaptador(this, R.layout.pelicula, lista_peliculas) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_contacto = view.findViewById(R.id.textView_titulo);
                    if (texto_contacto != null)
                        texto_contacto.setText(((Pelicula) entrada).getTITLE());

                    TextView texto_telefono = view.findViewById(R.id.textView_año);
                    if (texto_telefono != null)
                        texto_telefono.setText(((Pelicula) entrada).getYEAR());

                    String imgURL = tmdb.getSecureBasePath() + "original" + ((Pelicula) entrada).getIMAGE();

                    new DownloadImage((ImageView) view.findViewById(R.id.imageView_poster))
                            .execute(imgURL);



                    final TextView texto_ID = view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Pelicula) entrada).getID()));

                    ToggleButton toggle = view.findViewById(R.id.toggleButton_añadir_pelicula);

                    if (libreria.recuperarPELICULA(((Pelicula) entrada).getID()) != null)
                        toggle.setChecked(true);

                    final Pelicula pelicula =  (Pelicula) entrada;

                    toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                // Añadir película
                                String id = texto_ID.getText().toString();
                                Log.d("sebas",id);
                                libreria.insertarPELICULA(pelicula);
                            } else {
                                // Quitar película
                                String id = texto_ID.getText().toString();
                                Log.d("sebas",id);
                                libreria.borrarPELICULA(pelicula.getID());
                            }
                        }
                    });

                }
            }
        });

    }

    public void irBuscarPeliculas(View view) {
        Intent intent = new Intent(this, BuscarPeliculasActivity.class);
        startActivity(intent);
    }
}
