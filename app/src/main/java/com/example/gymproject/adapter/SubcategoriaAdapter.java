package com.example.gymproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymproject.R;
import com.example.gymproject.adapter.CarritoDeCompra.ListaCarrito;
import com.example.gymproject.producto.ProductoCategoria;
import com.example.gymproject.producto.SubcategoriaModelo;

import java.util.List;

public class SubcategoriaAdapter extends RecyclerView.Adapter<SubcategoriaAdapter.MyViewHolder> {

    private Context mContext;
    private List<SubcategoriaModelo> subcategoria;
    int posicionMarcada = -1;
    View view;
    public SubcategoriaAdapter(Context mContext, List<SubcategoriaModelo> subcategoria) {
        this.mContext = mContext;
        this.subcategoria = subcategoria;
    }

    @NonNull
    @Override
    public SubcategoriaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_subcategoria,parent,false);
        return new SubcategoriaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoriaAdapter.MyViewHolder holder, int position) {
        SubcategoriaModelo subcategoriaModelo = subcategoria.get(position);
        holder.txtidsub.setText(subcategoriaModelo.getIdSub());
        holder.txtnombresub.setText(subcategoriaModelo.getNomSub());
        holder.txtidcat.setText(subcategoriaModelo.getIdCat());

        final int pos = position;

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProductoCategoria) mContext).obtenerActualizacion();
                subcategoriaModelo.getIdSub();
                ProductoCategoria.seleccionSub = subcategoriaModelo.getIdSub();
                ((ProductoCategoria) mContext).listarProductoSubcategoria("https://rgym.online/gymsystem/vmovil/listarProductoSubcategoria.php?id_sub="+subcategoriaModelo.getIdSub());
                //((ProductoCategoria) mContext).obtenerActualizacion();

                posicionMarcada = pos;
                notifyDataSetChanged();
                //Toast.makeText(mContext, "Ud. seleccionó la categoria " +holder.txtnombresub.getText() + "id" +subcategoriaModelo.getIdSub(), Toast.LENGTH_SHORT).show();
            }
        });

        if (posicionMarcada == position){
            //holder.mContainer.setBackgroundResource(R.drawable.shape_seleccion_subcategoria);
            holder.txtnombresub.setTypeface(null, Typeface.BOLD);
            holder.barradeselecion.setBackgroundColor(view.getResources().getColor(R.color.principal));
        }else{
            holder.txtnombresub.setTypeface(null, Typeface.NORMAL);
            holder.barradeselecion.setBackgroundColor(view.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return subcategoria.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtnombresub, txtidsub, txtidcat, barradeselecion;
        private RelativeLayout mContainer;

        public MyViewHolder (View view){
            super(view);

            txtnombresub = view.findViewById(R.id.txtnombresub);
            txtidsub = view.findViewById(R.id.txtidsub);
            txtidcat = view.findViewById(R.id.txtidcat);
            mContainer = view.findViewById(R.id.subcategoria_container);
            barradeselecion = view.findViewById(R.id.barraseleccion);
        }
    }

}
