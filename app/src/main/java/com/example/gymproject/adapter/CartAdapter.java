package com.example.gymproject.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.gymproject.Database.OrderContract;
import com.example.gymproject.R;

public class CartAdapter extends CursorAdapter {

    public CartAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_cartlist, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titulo, precio, cantidad;


        titulo = view.findViewById(R.id.drinkNameinOrderSummary);
        precio = view.findViewById(R.id.priceinOrderSummary);
        cantidad = view.findViewById(R.id.quantityinOrderSummary);

        int name = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME);
        int priceproducto = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE);
        int quantityproducto = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_QUANTITY);

        String nameofproduct = cursor.getString(name);
        String priceofproduct = cursor.getString(priceproducto);
        String quantityofproduct = cursor.getString(quantityproducto);

        titulo.setText(nameofproduct);
        precio.setText(priceofproduct);
        cantidad.setText(quantityofproduct);

    }
}
