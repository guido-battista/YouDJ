package com.example.guido.youdj.ListaVotar;

import android.content.Context;
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

    public static class CancionVotarViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, View.OnLongClickListener{
        CardView cv;
        public TextView titulo;
        public TextView votos;
        public ItemClickListener mListener;

        CancionVotarViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            titulo = (TextView)itemView.findViewById(R.id.cancionVotarTitulo);
            votos = (TextView)itemView.findViewById(R.id.cancionVotarVotos);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener (ItemClickListener itemClickListener)
        {
            this.mListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v,getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onClick(v,getAdapterPosition(), true);
            return true;
        }
    }

    List<Cancion> canciones;
    Context context;
    private ItemClickListener itemClickListener;

    CancionVotarAdapter(List<Cancion> canciones, ItemClickListener listener){
        this.canciones = canciones;
        this.itemClickListener = listener;
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
        cancionVotarViewHolder.setItemClickListener(itemClickListener);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




}