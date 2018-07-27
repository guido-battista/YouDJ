package com.example.guido.youdj.ListaVotar;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guido.youdj.Modelos.Cancion;
import com.example.guido.youdj.R;

import java.util.List;

public class CancionYaEscuchadaAdapter extends RecyclerView.Adapter<CancionYaEscuchadaAdapter.CancionYaEscuchadaViewHolder>{


    public static class CancionYaEscuchadaViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        public TextView titulo;
        public TextView votos;
        public ItemClickListener mListener;

        CancionYaEscuchadaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvYaEscuchada);
            titulo = (TextView)itemView.findViewById(R.id.cancionYaEscuchadaTitutlo);
            votos = (TextView)itemView.findViewById(R.id.cancionYaEscuchadaVotos);
        }

        public void setItemClickListener (ItemClickListener itemClickListener)
        {
            this.mListener = itemClickListener;
        }

    }

    List<Cancion> canciones;

    CancionYaEscuchadaAdapter(List<Cancion> canciones){
        this.canciones = canciones;
    }

    @Override
    public int getItemCount()
    {
        if (canciones != null) {
            return canciones.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public CancionYaEscuchadaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancion_ya_escuchada_cardview, parent, false);
        CancionYaEscuchadaViewHolder pvh = new CancionYaEscuchadaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CancionYaEscuchadaViewHolder holder, int i) {
        //personViewHolder.personAge.setText(persons.get(i).age);
        holder.titulo.setText(canciones.get(i).titulo);
        holder.votos.setText(Integer.toString(canciones.get(i).votos));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
