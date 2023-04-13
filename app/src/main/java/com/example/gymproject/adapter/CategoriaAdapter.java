package com.example.gymproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymproject.DetalleProduco;
import com.example.gymproject.R;
import com.example.gymproject.producto.CategoriaModelo;
import com.example.gymproject.producto.ProductoCategoria;
import com.example.gymproject.producto.ProductoModelo;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoriaModelo> categoria;

    public CategoriaAdapter(Context mContext, List<CategoriaModelo> categoria) {
        this.mContext = mContext;
        this.categoria = categoria;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_categorias,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.MyViewHolder holder, int position) {
        CategoriaModelo categoriaModelo = categoria.get(position);
        holder.txtIdCategoria.setText(categoriaModelo.getIdCategoria());
        holder.txtNomCategoria.setText(categoriaModelo.getNombreCategoria());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductoCategoria.class);
                intent.putExtra("id",categoriaModelo.getIdCategoria());
                intent.putExtra("nombre",categoriaModelo.getNombreCategoria());

                //Toast.makeText(mContext, "Ud. seleccionó la categoria " +holder.txtNomCategoria.getText(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoria.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtIdCategoria, txtNomCategoria;
        private RelativeLayout mContainer;

        public MyViewHolder (View view){
            super(view);

            txtIdCategoria = view.findViewById(R.id.txtIdCategoria);
            txtNomCategoria = view.findViewById(R.id.txtNomCategoria);
            mContainer = view.findViewById(R.id.categoria_container);
        }
    }
}
