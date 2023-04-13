package com.example.gymproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymproject.R;
import com.example.gymproject.adapter.PedidoFinal.DetallePedidoFinal;
import com.example.gymproject.adapter.PedidoFinal.PedidoHistorialModelo;
import com.example.gymproject.producto.SubcategoriaModelo;

import java.util.List;

public class PedidoProgressAdapterMenu extends RecyclerView.Adapter<PedidoProgressAdapterMenu.PedidoProHolder> {

    private Context mContextD;
    public List<PedidoHistorialModelo> listaPedido;

    public PedidoProgressAdapterMenu(Context mContextD, List<PedidoHistorialModelo> listaPedido) {
        this.mContextD = mContextD;
        this.listaPedido = listaPedido;
    }

    @NonNull
    @Override
    public PedidoProgressAdapterMenu.PedidoProHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pedido_progress,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new PedidoProgressAdapterMenu.PedidoProHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoProgressAdapterMenu.PedidoProHolder holder, int position) {
        String est = listaPedido.get(position).getEstado();

        PedidoHistorialModelo pedidoHistorialModelo = listaPedido.get(position);
        holder.estado.setText(pedidoHistorialModelo.getEstado());
        holder.nropedido.setText(pedidoHistorialModelo.getNropedido());
        holder.total.setText(pedidoHistorialModelo.getTotal());
        holder.fecha.setText(pedidoHistorialModelo.getFecha());
        holder.tipo.setText(pedidoHistorialModelo.getTipo());
        holder.dire.setText(pedidoHistorialModelo.getDir());

        holder.btnDetallePedido.setOnClickListener(v ->{
            Intent intent = new Intent(mContextD, DetallePedidoFinal.class);
            intent.putExtra("estado",pedidoHistorialModelo.getEstado());
            intent.putExtra("fecha",pedidoHistorialModelo.getFecha());
            intent.putExtra("montoT",pedidoHistorialModelo.getTotal());
            intent.putExtra("nro",pedidoHistorialModelo.getNropedido());
            intent.putExtra("tipo",pedidoHistorialModelo.getTipo());
            intent.putExtra("direc",pedidoHistorialModelo.getDir());

            mContextD.startActivity(intent);

            Toast.makeText(mContextD, "Este el pedido nro" +holder.nropedido.getText(), Toast.LENGTH_SHORT).show();
        });

        switch (est){
            case "pro":
                //holder.estado.setText("Espera de confirmación");
                holder.estado.setText("En proceso...");
                holder.barraprogreso1.setIndeterminate(true);
                holder.btnEstadoPedido.setText("Espera de confirmación");
                break;
            case "prs":
                holder.estado.setText("Procesado");
                holder.barraprogreso1.setIndeterminate(false);
                holder.barraprogreso1.setProgress(100);
                holder.btnEstadoPedido.setText("Espera de envío");
                holder.barraprogreso2.setIndeterminate(true);
                break;
            case "env":
                holder.barraprogreso1.setIndeterminate(false);
                holder.barraprogreso1.setProgress(100);
                holder.barraprogreso2.setIndeterminate(false);
                holder.barraprogreso2.setProgress(100);
                holder.estado.setText("En camino");
                holder.barraprogreso3.setIndeterminate(true);
                holder.btnEstadoPedido.setText("Confirmado");
        }
    }

    @Override
    public int getItemCount() {
        return listaPedido.size();
    }

    public class PedidoProHolder extends RecyclerView.ViewHolder {
        TextView estado, nropedido, total, fecha, tipo, dire;
        TextView btnDetallePedido,btnEstadoPedido;
        //LinearLayout contenedor;
        ProgressBar barraprogreso1, barraprogreso2, barraprogreso3;
        public PedidoProHolder(View itemView){
            super(itemView);
            estado = (TextView) itemView.findViewById(R.id.txtestadoActualPedido);
            nropedido = (TextView) itemView.findViewById(R.id.txtnroActualPedido);
            total = (TextView) itemView.findViewById(R.id.txttotalActualPedido);
            fecha = (TextView) itemView.findViewById(R.id.txtfechaActualPedido);
            tipo = (TextView) itemView.findViewById(R.id.txttipo);
            dire = (TextView) itemView.findViewById(R.id.txtdire);
            btnDetallePedido = (TextView) itemView.findViewById(R.id.btnDetallePedido);
            btnEstadoPedido = (TextView) itemView.findViewById(R.id.btnEstadoPedido);
            //contenedor = (LinearLayout) itemView.findViewById(R.id.contenedor_pedido_progreso);
            barraprogreso1 = (ProgressBar) itemView.findViewById(R.id.barraprogreso1);
            barraprogreso2 = (ProgressBar) itemView.findViewById(R.id.barraprogreso2);
            barraprogreso3 = (ProgressBar) itemView.findViewById(R.id.barraprogreso3);
        }
}}
