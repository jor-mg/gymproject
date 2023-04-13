package com.example.gymproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymproject.MenuPrincipal;
import com.example.gymproject.R;


import com.example.gymproject.membresia.ListaMembresia;
import com.example.gymproject.membresia.MembresiaModelo;
import com.example.gymproject.membresia.RegistrarMembresia;
import com.example.gymproject.pago.RegistrarPago;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MembresiaAdapter extends RecyclerView.Adapter<MembresiaAdapter.MembresiasHolder> implements Filterable {
    Context context;
    public List<MembresiaModelo> listaMembresia2;
    public List<MembresiaModelo> listaMembresiaFull;
    public MembresiaAdapter(Context context,List<MembresiaModelo> listaMembresia2){
        this.context = context;
        this.listaMembresia2=listaMembresia2;
        this.listaMembresiaFull = listaMembresia2;

    }
    @NonNull
    @Override
    public MembresiasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_membresia,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new MembresiaAdapter.MembresiasHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MembresiasHolder holder, final int position) {
        MembresiaModelo item = listaMembresiaFull.get(position);

        holder.id2.setText(listaMembresiaFull.get(position).getId2().toString());
        holder.cliente2.setText(listaMembresiaFull.get(position).getCliente2().toString());
        holder.clase2.setText(listaMembresiaFull.get(position).getClase2().toString());
        holder.tipo2.setText(listaMembresiaFull.get(position).getTipo2().toString());
        holder.meses2.setText(listaMembresiaFull.get(position).getMeses2().toString());
        holder.inicio2.setText(listaMembresiaFull.get(position).getInicio2().toString());
        holder.fin2.setText(listaMembresiaFull.get(position).getFin2().toString());


    }


    @Override
    public int getItemCount() {
        return listaMembresiaFull.size();
    }


    @Override


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSquense = constraint.toString();
                if (charSquense.isEmpty()){
                    listaMembresiaFull = listaMembresia2 ;
                }else {
                    List<MembresiaModelo> filterList = new ArrayList<>();
                    for (MembresiaModelo row: listaMembresia2){
                        if (row.getId2().toLowerCase().contains(charSquense.toLowerCase()) || row.getCliente2().toLowerCase().contains(charSquense.toLowerCase())){
                            filterList.add(row);
                        }
                    }

                    listaMembresiaFull = filterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaMembresiaFull;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaMembresiaFull = (ArrayList<MembresiaModelo>) results.values;
                notifyDataSetChanged();
            }
        };
    }




    public class MembresiasHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id2, cliente2, clase2, tipo2, meses2, inicio2, fin2;
        public MembresiasHolder(View itemView){
            super(itemView);
            id2 = (TextView) itemView.findViewById(R.id.txt07);
            cliente2 = (TextView) itemView.findViewById(R.id.txt08);
            clase2 = (TextView) itemView.findViewById(R.id.txt09);
            tipo2 = (TextView) itemView.findViewById(R.id.txt10);
            meses2 = (TextView) itemView.findViewById(R.id.txt11);
            inicio2 = (TextView) itemView.findViewById(R.id.txt12);
            fin2 = (TextView) itemView.findViewById(R.id.txt13);

            itemView.setOnClickListener(this);

        }
       public void onClick(View v){
            Intent intent = new Intent(context, RegistrarPago.class);
            intent.putExtra("id2", listaMembresiaFull.get(getAdapterPosition()).getId2());
            context.startActivity(intent);
        }
    }

}
