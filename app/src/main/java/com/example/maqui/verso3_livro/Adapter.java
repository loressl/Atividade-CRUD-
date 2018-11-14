package com.example.maqui.verso3_livro;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

/**
 * Created by jamiltondamasceno
 */

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Livro> lista;

    public Adapter(List<Livro> lista) {
        this.lista = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Livro livro = lista.get( position );

        holder.titulo.setText( livro.getTitulo() );
        holder.autor.setText( livro.getAutor() );
        holder.ano.setText( livro.getAno() );

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView ano;
        TextView autor;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo =(TextView) itemView.findViewById(R.id.titulo);
            ano =(TextView) itemView.findViewById(R.id.ano);
            autor =(TextView) itemView.findViewById(R.id.autor);

        }
    }

}
