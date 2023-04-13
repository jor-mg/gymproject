package com.example.gymproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymproject.DetalleProduco;
import com.example.gymproject.R;
import com.example.gymproject.adapter.PedidoFinal.DetallePedidoFinal;
import com.example.gymproject.adapter.PedidoFinal.PedidoHistorialModelo;
import com.example.gymproject.producto.ProductoModelo;

import java.util.ArrayList;
import java.util.List;

public class PedidoHistorialAdapter extends RecyclerView.Adapter<PedidoHistorialAdapter.PedidoHolder> implements Filterable  {
    private Context mContextD;
    public List<PedidoHistorialModelo> listaPedido;
    public List<PedidoHistorialModelo> listaPedidoFull;


    public PedidoHistorialAdapter(Context context,List<PedidoHistorialModelo> listaPedido){
        this.mContextD = context;
        this.listaPedido=listaPedido;
        this.listaPedidoFull = listaPedido;

    }

    @NonNull
    @Override
    public PedidoHistorialAdapter.PedidoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new PedidoHistorialAdapter.PedidoHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoHistorialAdapter.PedidoHolder holder, int position) {

        String est = listaPedidoFull.get(position).getEstado();
        final PedidoHistorialModelo pedidoHistorialModelo = listaPedidoFull.get(position);


        holder.estado.setText(listaPedidoFull.get(position).getEstado().toString());
        holder.nropedido.setText(listaPedidoFull.get(position).getNropedido().toString());
        holder.total.setText(listaPedidoFull.get(position).getTotal().toString());
        holder.fecha.setText(listaPedidoFull.get(position).getFecha().toString());
        holder.tipo.setText(listaPedidoFull.get(position).getTipo().toString());
        holder.dire.setText(listaPedidoFull.get(position).getDir().toString());

        switch (est){
            case "pro":
                holder.estado.setText("En progreso...");
                break;
            case "prs":
                holder.estado.setText("Procesado");
                break;
            case "env":
                holder.estado.setText("En camino...");
                break;
            case "pgd":
                holder.estado.setText("Pagado");
                break;
            case "anu":
                holder.estado.setText("Anulado");
                holder.estado.setTextColor(Color.RED);
                break;

        }

        holder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContextD, DetallePedidoFinal.class);
                intent.putExtra("estado",pedidoHistorialModelo.getEstado());
                intent.putExtra("fecha",pedidoHistorialModelo.getFecha());
                intent.putExtra("montoT",pedidoHistorialModelo.getTotal());
                intent.putExtra("nro",pedidoHistorialModelo.getNropedido());
                intent.putExtra("tipo",pedidoHistorialModelo.getTipo());
                intent.putExtra("direc",pedidoHistorialModelo.getDir());

                mContextD.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return listaPedidoFull.size();
    }

    public long getItemId(int pos) {
        return pos;
    }


    public void removeItemAtPosition(int pos) {
        listaPedidoFull.remove(pos);
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSquense = constraint.toString();
                if (charSquense.isEmpty()){
                    listaPedidoFull = listaPedido ;
                }else {
                    List<PedidoHistorialModelo> filterList = new ArrayList<>();
                    for (PedidoHistorialModelo row: listaPedido){
                        if (row.getNropedido().toLowerCase().contains(charSquense.toLowerCase())){
                            filterList.add(row);
                        }
                    }

                    listaPedidoFull = filterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaPedidoFull;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaPedidoFull = (ArrayList<PedidoHistorialModelo>) results.values;
                notifyDataSetChanged();
            }
        };
    }




public class PedidoHolder extends RecyclerView.ViewHolder {
    TextView estado, nropedido, total, fecha, tipo, dire;
    LinearLayout contenedor;
    public PedidoHolder(View itemView){
        super(itemView);
        estado = (TextView) itemView.findViewById(R.id.txtEstadoPedido);
        nropedido = (TextView) itemView.findViewById(R.id.txtNroPedido);
        total = (TextView) itemView.findViewById(R.id.txtPrecioPedido);
        fecha = (TextView) itemView.findViewById(R.id.txtFechaPedido);
        tipo = (TextView) itemView.findViewById(R.id.txtTipoPedido);
        dire = (TextView) itemView.findViewById(R.id.txtDireccionPedido);
        contenedor = (LinearLayout) itemView.findViewById(R.id.detallePedidoFinal_container);

    }



}}
