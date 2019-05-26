package com.dam.wftc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListaAdaptador extends RecyclerView.Adapter<ListaAdaptador.ListaViewHolder> {

    private ArrayList<Pelicula> peliculas;
    public TMDB tmdb;
    public LibreriaBaseDatos libreria;

    public static class ListaViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagenPoster;
        public TextView textoTitulo;
        public TextView textoAño;
        public TextView textoID;
        public ToggleButton toggleAñadida;

        public ListaViewHolder(View itemView) {
            super(itemView);

            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            imagenPoster = itemView.findViewById(R.id.imageView_poster);
            textoTitulo = itemView.findViewById(R.id.textView_titulo);
            textoAño = itemView.findViewById(R.id.textView_año);
            textoID = itemView.findViewById(R.id.textView_ID);
            toggleAñadida = itemView.findViewById(R.id.toggleButton_añadir_pelicula);

        }
    }

    public ListaAdaptador(ArrayList<Pelicula> peliculas, TMDB tmdb, LibreriaBaseDatos libreria) {
        this.peliculas = peliculas;
        this.tmdb = tmdb;
        this.libreria = libreria;
    }

    @Override
    public ListaAdaptador.ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);

        // Inflate the custom layout
        View vistaPelicula = inflater.inflate(R.layout.pelicula, parent, false);

        ListaViewHolder viewHolder = new ListaViewHolder(vistaPelicula);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int position) {
        // - get element from your dataset at this position
        final Pelicula pelicula = peliculas.get(position);

        // - replace the contents of the view with that element
        String imgURL = tmdb.getSecureBasePath() + "original" + (pelicula.getIMAGE());
        new DownloadImage(holder.imagenPoster).execute(imgURL);

        holder.textoTitulo.setText(pelicula.getTITLE());
        holder.textoAño.setText(pelicula.getYEAR());
        holder.textoID.setText(Integer.toString(pelicula.getID()));

        if (pelicula.getAñadida())
            holder.toggleAñadida.setChecked(true);
        else
            holder.toggleAñadida.setChecked(false);

        holder.toggleAñadida.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return peliculas.size();
    }

}