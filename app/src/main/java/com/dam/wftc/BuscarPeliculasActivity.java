package com.dam.wftc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BuscarPeliculasActivity extends AppCompatActivity {


    private LibreriaBaseDatos libBD;

    private ListView lista;
    private TMDB tmdb;
    private EditText titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_peliculas);

        libBD = new LibreriaBaseDatos(getApplicationContext());
        lista = findViewById(R.id.ListView_lista_resultados);
        tmdb = new TMDB();

    }

    public void buscar(View view) {

        titulo = findViewById(R.id.editText_titulo);

        String url = TMDB.getSearchURL() + "?api_key=" + TMDB.getApiKey() + "&query=" + titulo.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Miramos si la respuesta es correcta
                        try{
                            int total_results =response.getInt("total_results");
                            if(total_results > 0){
                                JSONArray results = response.getJSONArray("results");
                                // Peliculas encontradas
                                ArrayList<Pelicula> lista_peliculas = new ArrayList<>();
                                if (results != null) {
                                    for (int i=0 ; i<results.length() ; i++){

                                        JSONObject peli = results.getJSONObject(i);
                                        lista_peliculas.add(new Pelicula(peli.getInt("id"), peli.getString("title"),
                                                peli.getString("release_date"), peli.getString("overview"),
                                                peli.getString("poster_path"), peli.getDouble("vote_average")));
                                    }
                                }

                                mostrarPeliculas(lista_peliculas);

                            } else {
                                // Pelicula no identificada
                                Toast.makeText(getApplicationContext(),
                                        "Película no encontrada", Toast.LENGTH_SHORT).show();
                            } } catch (JSONException e){
                            e.printStackTrace();
                        }
                        VolleyLog.v("Response:%n %s", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // add the request object to the queue to be executed
        WFTCApplication.getInstance().getRequestQueue().add(request);
    }

    public void mostrarPeliculas(ArrayList<Pelicula> peliculas) {

            lista.setAdapter(new Lista_adaptador(this, R.layout.pelicula, peliculas) {
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

                        if (libBD.recuperarPELICULA(((Pelicula) entrada).getID()) != null)
                            toggle.setChecked(true);

                        final Pelicula pelicula =  (Pelicula) entrada;

                        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    // Quitar película
                                    String id = texto_ID.getText().toString();
                                    Log.d("sebas",id);
                                    libBD.insertarPELICULA(pelicula);
                                } else {
                                    // Añadir película
                                }
                            }
                        });
                    }
                }
            });
        }
}
