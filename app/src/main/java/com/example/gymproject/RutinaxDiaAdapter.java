package com.example.gymproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymproject.adapter.ProductoAdapter;
import com.example.gymproject.producto.ProductoModelo;

import java.util.ArrayList;
import java.util.List;

public class RutinaxDiaAdapter extends RecyclerView.Adapter<RutinaxDiaAdapter.MyViewHolder>{

    private Context mContext;
    private List<RutinaModelo> rutinaList;

    public RutinaxDiaAdapter (Context context,List<RutinaModelo> rutinaList){
        this.mContext = context;
        this.rutinaList = rutinaList;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView series, repes, nombre;
        private ImageView mImageView;
        private LinearLayout mContainer;

        public MyViewHolder (View view){
            super(view);

            repes = view.findViewById(R.id.txt_repes);
            series = view.findViewById(R.id.txt_series);
            nombre = view.findViewById(R.id.txt_nombre_ejercicio);
            mImageView = view.findViewById(R.id.img_ejer);
            mContainer = view.findViewById(R.id.ejercicios_container);
        }
    }


    @NonNull
    @Override
    public RutinaxDiaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rutinax_dia,parent,false);
        return new RutinaxDiaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaxDiaAdapter.MyViewHolder holder, int position) {

        final RutinaModelo rutina = rutinaList.get(position);

        holder.nombre.setText(rutina.getNombre());
        holder.series.setText(rutina.getSeries()+" series de ");
        holder.repes.setText(rutina.getRepeticiones() + " repeticiones");


        Glide.with(mContext).load(rutina.getImagen()).into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        return rutinaList.size();
    }


}
