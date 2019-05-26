package com.dam.wftc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.FloatMath;
import android.util.Log;
import android.widget.CompoundButton;
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

public class DescubreActivity extends AppCompatActivity implements SensorEventListener {

    // Sensor de movimiento
    private SensorManager sensorManager;
    private Sensor sensor;
    private long ultimaActualizacion = -1;
    // coordenadas
    private float x, y, z;
    private float last_x, last_y, last_z;
    // umbral para la detección de agitamiento
    private static final int SHAKE_THRESHOLD = 350;

    private TMDB tmdb;
    LibreriaBaseDatos libreria;
    private int index;
    private ArrayList<Pelicula> lista_peliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descubre);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        tmdb = new TMDB();
        libreria = new LibreriaBaseDatos(getApplicationContext());
        index = 0;

        lista_peliculas = new ArrayList<>();

        String url = TMDB.getDiscoverURL() + "?api_key=" + TMDB.getApiKey() + "&vote_count.gte=1000&vote_average.gte=7";

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
                                ArrayList<Pelicula> peliculas = new ArrayList<>();
                                if (results != null) {
                                    for (int i=0 ; i<results.length() ; i++){

                                        JSONObject peli = results.getJSONObject(i);
                                        Pelicula pelicula = new Pelicula(peli.getInt("id"), peli.getString("title"),
                                                peli.getString("release_date"), peli.getString("overview"),
                                                peli.getString("poster_path"), peli.getDouble("vote_average"), false);
                                        if (libreria.recuperarPELICULA(pelicula.getID()) != null)
                                            pelicula.setAñadida(true);

                                        peliculas.add(pelicula);
                                    }

                                    setListaPeliculas(peliculas);
                                }

                                mostrarPelicula(peliculas.get(index));

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

    public void  setListaPeliculas(ArrayList<Pelicula> peliculas) {
        lista_peliculas.addAll(peliculas);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    public void mostrarPelicula(final Pelicula pelicula) {

        ImageView imagenPoster = findViewById(R.id.imageView_poster);
        TextView textoTitulo = findViewById(R.id.textView_titulo);
        TextView textoAño = findViewById(R.id.textView_año);
        ToggleButton toggleAñadida = findViewById(R.id.toggleButton_añadir_pelicula);

        String imgURL = tmdb.getSecureBasePath() + "original" + (pelicula.getIMAGE());
        new DownloadImage(imagenPoster).execute(imgURL);

        textoTitulo.setText(pelicula.getTITLE());
        textoAño.setText(pelicula.getYEAR());

        if (pelicula.getAñadida())
            toggleAñadida.setChecked(true);
        else
            toggleAñadida.setChecked(false);

        toggleAñadida.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Añadir pelicula
                    libreria.insertarPELICULA(pelicula);
                    pelicula.setAñadida(true);
                } else {
                    // Quitar película
                    libreria.borrarPELICULA(pelicula.getID());
                    pelicula.setAñadida(false);
                }
            }
        });
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            long tiempoActual = System.currentTimeMillis();
            // solo permite actualizar cada segundo
            if ((tiempoActual - ultimaActualizacion) > 1000) {
                ultimaActualizacion = tiempoActual;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];


                if (z - last_z < -2)
                    descartar();
                else if (z - last_z > 2)
                    descartar();
                else if (x > 1 || x < -1)
                    añadir();
            }


            last_x = x;
            last_y = y;
            last_z = z;
        }


    }

    public void onAccuracyChanged(Sensor sensor, int arg1) {
        // TODO Auto-generated method stub
    }

    public void descartar() {
        Toast toast = Toast.makeText(getApplicationContext(), "Película descartada.", Toast.LENGTH_LONG);
        toast.show();
        index++;
        mostrarPelicula(lista_peliculas.get(index));
    }

    public void añadir() {
        libreria.insertarPELICULA(lista_peliculas.get(index));
        Toast toast = Toast.makeText(getApplicationContext(), "Película añadida a librería.", Toast.LENGTH_LONG);
        toast.show();
        index++;
        mostrarPelicula(lista_peliculas.get(index));
    }
}