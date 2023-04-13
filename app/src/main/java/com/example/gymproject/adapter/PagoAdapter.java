package com.example.gymproject.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymproject.R;


import com.example.gymproject.pago.PagoModelo;

import java.util.ArrayList;
import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.PagoHolder> implements Filterable {
    public List<PagoModelo> listaPago2;
    public List<PagoModelo> listaPagoFull;

    public PagoAdapter(List<PagoModelo> listaPago2){
        this.listaPago2=listaPago2;
        this.listaPagoFull = listaPago2;
    }



    @NonNull
    @Override
    public PagoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);




        return new PagoAdapter.PagoHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoHolder holder, int position) {
        PagoModelo pagoModelo = listaPagoFull.get(position);

        holder.idpa.setText(listaPagoFull.get(position).getIdpa().toString());
        holder.idmem.setText(listaPagoFull.get(position).getIdmem().toString());
        holder.nombrecli.setText(listaPagoFull.get(position).getNombrecli().toString());
        holder.monto.setText(listaPagoFull.get(position).getMonto().toString());
        holder.dnip.setText(listaPagoFull.get(position).getDnip().toString());
        holder.tipop.setText(listaPagoFull.get(position).getTipop().toString());
        holder.clasep.setText(listaPagoFull.get(position).getClasep().toString());
        holder.ciclosp.setText(listaPagoFull.get(position).getCiclosp().toString());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());

                View dialogView=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_dialog_datos,null);
                ImageView img= (ImageView) dialogView.findViewById(R.id.usuario);
                img.setImageResource(R.drawable.perfil);
                TextView user9,dni9,mem9,tip9,cla9,cic9,mon9,vacio;


                user9 = dialogView.findViewById(R.id.user9);
                dni9 = dialogView.findViewById(R.id.dni9);
                mem9 = dialogView.findViewById(R.id.mem9);
                tip9 = dialogView.findViewById(R.id.tip9);
                cla9 = dialogView.findViewById(R.id.cla9);
                cic9 = dialogView.findViewById(R.id.cic9);
                mon9 = dialogView.findViewById(R.id.mon9);
                vacio = dialogView.findViewById(R.id.vacio);

                user9.setText(pagoModelo.getNombrecli());
                mem9.setText(pagoModelo.getIdmem());
                dni9.setText(pagoModelo.getDnip());
                tip9.setText(pagoModelo.getTipop());
                cla9.setText(pagoModelo.getClasep());
                cic9.setText(pagoModelo.getCiclosp());
                mon9.setText(pagoModelo.getMonto());
                vacio.setText(pagoModelo.getIdpa());

                AlertDialog alertDialog = builder.create();

                builder.setView(dialogView);
                builder.setNegativeButton(Html.fromHtml("<font color='#2196f3'>Cerrar</font>"), null);

                builder.show();




            }
        });

    }

    @Override
    public int getItemCount() {
        return listaPagoFull.size();
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSquense = constraint.toString();
                if (charSquense.isEmpty()){
                    listaPagoFull = listaPago2 ;
                }else {
                    List<PagoModelo> filterList = new ArrayList<>();
                    for (PagoModelo row: listaPago2){
                        if (row.getDnip().toLowerCase().contains(charSquense.toLowerCase()) || row.getNombrecli().toLowerCase().contains(charSquense.toLowerCase())){
                            filterList.add(row);
                        }
                    }

                    listaPagoFull = filterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaPagoFull;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaPagoFull = (ArrayList<PagoModelo>) results.values;
                notifyDataSetChanged();
            }
        };
    }




    public static class PagoHolder extends RecyclerView.ViewHolder {
        TextView idpa, idmem, nombrecli, monto, dnip,tipop,clasep,ciclosp;
        public PagoHolder(View itemView){
            super(itemView);
            idpa = (TextView) itemView.findViewById(R.id.txt14);
            idmem = (TextView) itemView.findViewById(R.id.txt15);
            nombrecli = (TextView) itemView.findViewById(R.id.txt16);
            monto = (TextView) itemView.findViewById(R.id.txt17);
            dnip = (TextView) itemView.findViewById(R.id.dniI);
            tipop = (TextView) itemView.findViewById(R.id.tipoI);
            clasep = (TextView) itemView.findViewById(R.id.claseI);
            ciclosp = (TextView) itemView.findViewById(R.id.cicloI);
        }
    }
}
