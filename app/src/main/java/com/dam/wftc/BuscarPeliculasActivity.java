package com.dam.wftc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    private LibreriaBaseDatos libreria;

    private TMDB tmdb;
    private EditText titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_peliculas);

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_lista_resultados);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        libreria = new LibreriaBaseDatos(getApplicationContext());
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
                                        Pelicula pelicula = new Pelicula(peli.getInt("id"), peli.getString("title"),
                                                peli.getString("release_date"), peli.getString("overview"),
                                                peli.getString("poster_path"), peli.getDouble("vote_average"), false);
                                        if (libreria.recuperarPELICULA(pelicula.getID()) != null)
                                            pelicula.setAñadida(true);

                                        lista_peliculas.add(pelicula);
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

            ListaAdaptador listaAdaptador = new ListaAdaptador(peliculas, tmdb, libreria);
            recyclerView.setAdapter(listaAdaptador);
    }
}
