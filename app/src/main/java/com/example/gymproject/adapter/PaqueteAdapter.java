package com.example.gymproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymproject.ListaUbicacion;
import com.example.gymproject.R;
import com.example.gymproject.paquete.PaqueteModelo;

import java.util.List;

public class PaqueteAdapter extends RecyclerView.Adapter<PaqueteAdapter.PaqueteHolder> {


    private Context miCtext;
    private List<PaqueteModelo>paqueteList;

    public PaqueteAdapter(Context miCtext, List<PaqueteModelo>paqueteList){
        this.miCtext = miCtext;
        this.paqueteList=paqueteList;
    }

    @NonNull
    @Override
    public PaqueteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(miCtext);
        View view = inflater.inflate(R.layout.item_paquetes, null);
        return new PaqueteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaqueteHolder holder, int position) {

        PaqueteModelo paqueteModelo = paqueteList.get(position);

        Glide.with(miCtext)
                .load(paqueteModelo.getImagen())
                .into(holder.imgPago);
        holder.txt_nombre_paquete.setText(paqueteModelo.getTitulo());
        holder.txt_precio_paquete.setText(paqueteModelo.getPrecio());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(miCtext.getApplicationContext(), ListaUbicacion.class);
                detail.putExtra("txt_nombre_paquete", paqueteModelo.getTitulo());
                miCtext.startActivity(detail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return paqueteList.size();
    }

    class PaqueteHolder extends RecyclerView.ViewHolder {

        TextView txt_nombre_paquete, txt_precio_paquete;
        ImageView imgPago;
        public PaqueteHolder(@NonNull View itemView) {
            super(itemView);

            txt_nombre_paquete = itemView.findViewById(R.id.txt_nombre_paquete);
            txt_precio_paquete = itemView.findViewById(R.id.txt_precio_paquete);
            imgPago = itemView.findViewById(R.id.imgPago);

        }
    }
}
