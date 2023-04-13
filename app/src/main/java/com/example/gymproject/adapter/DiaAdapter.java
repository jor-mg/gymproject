package com.example.gymproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymproject.DiaModelo;
import com.example.gymproject.R;
import com.example.gymproject.RutinaxDia;

import java.util.List;

public class DiaAdapter extends RecyclerView.Adapter<DiaAdapter.DiaHolder>{

    private Context miCtext;
    private List<DiaModelo> diaList;

    public DiaAdapter(Context miCtext, List<DiaModelo>diaList){
        this.miCtext = miCtext;
        this.diaList=diaList;
    }

    @NonNull
    @Override
    public DiaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtext);
        View view = inflater.inflate(R.layout.item_dia_entrenamiento, null);
        return new DiaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaAdapter.DiaHolder holder, int position) {
        DiaModelo diaModelo = diaList.get(position);

        holder.dia.setText(diaModelo.getDia());
        holder.musculo.setText(diaModelo.getMusculo());

        holder.dia_container.setOnClickListener(v ->{
            Intent intent = new Intent(miCtext, RutinaxDia.class);
            intent.putExtra("dia",diaModelo.getDia());
            intent.putExtra("musculo",diaModelo.getMusculo());
            miCtext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return diaList.size();
    }

    class  DiaHolder extends  RecyclerView.ViewHolder {
        TextView dia, musculo;
        LinearLayout dia_container;
        public DiaHolder(@NonNull View itemView) {
            super(itemView);

            dia = itemView.findViewById(R.id.txtdia);
            musculo = itemView.findViewById(R.id.txtmusculo);
            dia_container = itemView.findViewById(R.id.dia_container);
        }


    }
}
