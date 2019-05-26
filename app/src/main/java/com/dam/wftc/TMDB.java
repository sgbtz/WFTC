package com.dam.wftc;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TMDB {

    // URL para buscar peliculas
    private static final String searchURL = "https://api.themoviedb.org/3/search/movie";
    // URL para obtener detalles de una película en concreto
    private static final String filmDetailsURL = "https://api.themoviedb.org/3/movie/";
    // URL para obtener la configuración de TMDB
    private static final String tmdbConfURL = "https://api.themoviedb.org/3/configuration";
    // URL para descubrir peliculas
    private static final String discoverURL = "https://api.themoviedb.org/3/discover/movie";
    // API Key del servicio REST a utilizar
    private static final String API_KEY = "cd18a95d7192914edb30756f544a6f6c";
    // path para las imagenes
    private String secureBasePath = "https://image.tmdb.org/t/p/";

    public TMDB() {
        String url = tmdbConfURL + "?api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Miramos si la respuesta es correcta
                        try{
                            secureBasePath = response.getJSONObject("images").getString("secure_base_url");
                        } catch (JSONException e){
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

    public static String getSearchURL() { return searchURL; }

    public static String getDiscoverURL() { return discoverURL; }

    public static String getApiKey() { return API_KEY; }

    public String getSecureBasePath() { return this.secureBasePath; }
}
