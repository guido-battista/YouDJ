package com.example.guido.youdj.ListaVotar;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import com.example.guido.youdj.R;
import java.util.List;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.guido.youdj.Modelos.Cancion;

public class CancionVotarAdapter extends RecyclerView.Adapter<CancionVotarAdapter.CancionVotarViewHolder>{

    public static class CancionVotarViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        public TextView titulo;
        public TextView votos;

        CancionVotarViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            titulo = (TextView)itemView.findViewById(R.id.cancionVotarTitulo);
            votos = (TextView)itemView.findViewById(R.id.cancionVotarVotos);
        }
    }

    List<Cancion> canciones;

    CancionVotarAdapter(List<Cancion> canciones){
        this.canciones = canciones;
    }

    @Override
    public int getItemCount() {
        return canciones.size();
    }

    @Override
    public CancionVotarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cancion_votar_cardview, viewGroup, false);
        CancionVotarViewHolder pvh = new CancionVotarViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CancionVotarViewHolder cancionVotarViewHolder, int i) {
        //personViewHolder.personName.setText(persons.get(i).name);
        //personViewHolder.personAge.setText(persons.get(i).age);
        cancionVotarViewHolder.titulo.setText(canciones.get(i).titulo);
        cancionVotarViewHolder.votos.setText(Integer.toString(canciones.get(i).votos));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




}