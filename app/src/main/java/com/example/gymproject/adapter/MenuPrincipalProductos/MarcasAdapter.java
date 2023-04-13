package com.example.gymproject.adapter.MenuPrincipalProductos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymproject.R;
import com.example.gymproject.adapter.ProductoAdapter;
import com.example.gymproject.pago.RegistrarPago;
import com.example.gymproject.producto.ProductoModelo;

import java.util.ArrayList;
import java.util.List;

public class MarcasAdapter extends RecyclerView.Adapter<MarcasAdapter.MyViewHolder> implements Filterable {

    private Context maContext;
    private List<MarcaModelo> marcas;
    private List<MarcaModelo> marcaFull;


    public MarcasAdapter (Context context,List<MarcaModelo> marcas){
        this.maContext = context;
        this.marcas = marcas;
        this.marcaFull = marcas;
    }

    @NonNull
    @Override
    public MarcasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(maContext).inflate(R.layout.item_marcas,parent,false);
        return new MarcasAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarcasAdapter.MyViewHolder holder, int position) {
        final MarcaModelo marcas = marcaFull.get(position);
        holder.marcaId.setText(marcas.getIdM());
        holder.marcaTitle.setText(marcas.getTitle_m());
        Glide.with(maContext).load(marcas.getImage_m()).into(holder.marcaImageView);

        holder.marca_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(maContext, "Ud. seleccionó la marca: " +holder.marcaTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return marcaFull.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSquense = constraint.toString();
                if (charSquense.isEmpty()){
                    marcaFull = marcas ;
                }else {
                    List<MarcaModelo> filterList = new ArrayList<>();
                    for (MarcaModelo row: marcas){
                        if (row.getTitle_m().toLowerCase().contains(charSquense.toLowerCase())){
                            filterList.add(row);
                        }
                    }

                    marcaFull = filterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = marcaFull;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                marcaFull = (ArrayList<MarcaModelo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView marcaTitle, marcaId;
        private ImageView marcaImageView;
        private LinearLayout marca_container;
        public MyViewHolder(@NonNull View view) {
            super(view);
            marcaId = view.findViewById(R.id.iden_marca);
            marcaTitle = view.findViewById(R.id.marca_title);
            marcaImageView = view.findViewById(R.id.marca_image);
            marca_container = view.findViewById(R.id.marca_container);
        }

    }
}
