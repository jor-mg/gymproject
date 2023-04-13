package com.example.gymproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymproject.DetalleProduco;
import com.example.gymproject.R;
import com.example.gymproject.producto.ProductoModelo;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter2 extends RecyclerView.Adapter<ProductoAdapter2.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<ProductoModelo> products;
    private List<ProductoModelo> productsFull  ;

    public ProductoAdapter2 (Context context,List<ProductoModelo> products){
        this.mContext = context;
        this.products = products;
        this.productsFull = products;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mPrice,mId, mIdSub, mIdMar, mStk;
        private CardView contenedorStk;
        private ImageView mImageView;
        private LinearLayout mContainer;

        public MyViewHolder (View view){
            super(view);

            mId = view.findViewById(R.id.iden_product);
            mIdSub = view.findViewById(R.id.iden_subcate);
            mIdMar = view.findViewById(R.id.iden_marca);
            mTitle = view.findViewById(R.id.product_title);
            mImageView = view.findViewById(R.id.product_image);
            mPrice = view.findViewById(R.id.product_price);
            mStk = view.findViewById(R.id.product_stk);
            mContainer = view.findViewById(R.id.product_container);
            contenedorStk = view.findViewById(R.id.contenedorStk);
        }
    }


    @NonNull
    @Override
    public ProductoAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_productos2,parent,false);
        return new ProductoAdapter2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter2.MyViewHolder holder, int position) {

        final ProductoModelo product = productsFull.get(position);
        holder.mId.setText(product.getIdP());
        holder.mIdSub.setText(product.getIdSub());
        holder.mIdSub.setText(product.getIdMar());
        holder.mPrice.setText("S/"+product.getPrice());
        holder.mStk.setText("Stock: "+product.getStock());
        holder.mTitle.setText(product.getTitle());
        Glide.with(mContext).load(product.getImage()).into(holder.mImageView);


        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetalleProduco.class);
                intent.putExtra("id",product.getIdP());
                intent.putExtra("idSub",product.getIdSub());
                intent.putExtra("idMar",product.getIdMar());
                intent.putExtra("title",product.getTitle());
                intent.putExtra("image",product.getImage());
                intent.putExtra("stk",product.getStock());
                intent.putExtra("price",product.getPrice());

                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return productsFull.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSquense = constraint.toString();
                if (charSquense.isEmpty()){
                    productsFull = products ;
                }else {
                    List<ProductoModelo> filterList = new ArrayList<>();
                    for (ProductoModelo row: products){
                        if (row.getTitle().toLowerCase().contains(charSquense.toLowerCase())){
                            filterList.add(row);
                        }
                    }

                    productsFull = filterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productsFull;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productsFull = (ArrayList<ProductoModelo>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
