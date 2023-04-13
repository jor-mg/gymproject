package com.example.gymproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.gymproject.R;
import com.example.gymproject.adapter.CarritoDeCompra.CarritoModelo;
import com.example.gymproject.adapter.CarritoDeCompra.ListaCarrito;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoHolder>  {


    private Context miCtext;
    private List<CarritoModelo> carritoList;


    public CarritoAdapter(Context miCtext, List<CarritoModelo> carritoList) {
        this.miCtext = miCtext;
        this.carritoList = carritoList;

    }


    @NonNull
    @Override
    public CarritoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(miCtext);
        View view = inflater.inflate(R.layout.item_carrito, null);
        return new CarritoHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CarritoHolder holder, @SuppressLint("RecyclerView") int position) {

        CarritoModelo carritoModelo = carritoList.get(position);

        Glide.with(miCtext)
                .load(carritoModelo.getImagenProducto())
                .into(holder.imgPlatilloDC);
        holder.tvIdCarrito.setText(carritoModelo.getIdCarritoTemp());
        holder.tvNombreProductoCarrito.setText(carritoModelo.getnProducto());
        holder.tvPrecioProductoCarrito.setText(""+carritoModelo.getPrecioProducto());
        holder.edtCantidadCarrito.setText(""+carritoModelo.getCantidadProducto());


        String idcar1 = holder.tvIdCarrito.getText().toString();
        String usu_usuario1 = holder.tvUsuario.getText().toString();
        String cantidad_car1 = holder.edtCantidadCarrito.getText().toString();
        String precioxP = holder.tvPrecioProductoCarrito.getText().toString();



        holder.btnEliminarPCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(miCtext, holder.tvIdCarrito.getText().toString(), Toast.LENGTH_SHORT).show();
                //String url = "https://raygymproject.000webhostapp.com/eliminarItemCarrito.php?idcar=" + holder.tvIdCarrito.getText().toString();
                String url = "https://www.rgym.online/gymsystem/vmovil/eliminarItemCarrito.php?idcar=" + holder.tvIdCarrito.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String check = object.getString("state");
                            if (check.equals("delete")) {
                                Delete(position);
                                String e = "eliminado";
                                Toast.makeText(miCtext, "Se ha eliminado el ItemCarrito NRO: " + holder.tvIdCarrito.getText().toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(miCtext, ListaCarrito.class);
                                intent.putExtra("key", "eliminado");
                                miCtext.startActivity(intent);
                            }else{
                                Toast.makeText(miCtext, response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(miCtext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> deleteParams = new HashMap<>();
                        deleteParams.put("idcar", carritoModelo.getIdCarritoTemp());
                        return deleteParams;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(miCtext);
                requestQueue.add(stringRequest);
                notifyDataSetChanged();

            }
        });
        double CantiA = 0;
        int Canti = 1;

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double montoporproducto = carritoModelo.getTotal();

                int CantidadActual = Integer.parseInt(cantidad_car1);
                double PrecioxP = Double.parseDouble(precioxP);
                if (carritoModelo.getCantidadProducto() != 10) {
                    CantidadActual++;
                    carritoModelo.agregarCantidad();

                    //holder.edtCantidadCarrito.setText(Integer.toString(CantidadActual));

                    /*String url2 = "https://raygymproject.000webhostapp.com/actualizarCantidadCarritoTemp.php?idcar=" + holder.tvIdCarrito.getText().toString() +
                            "&cantidad_car=" +CantidadActual;*/
                    String url2 = "https://www.rgym.online/gymsystem/vmovil/actualizarCantidadCarritoTemp.php?idcar=" + holder.tvIdCarrito.getText().toString() +
                            "&cantidad_car=" +CantidadActual;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(miCtext, response, Toast.LENGTH_SHORT).show();
                            ;

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(miCtext, error.toString(), Toast.LENGTH_SHORT).show();
                            ;
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("idcar", idcar1);
                            params.put("cantidad_car", cantidad_car1);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(miCtext);
                    queue.add(stringRequest);
                    notifyDataSetChanged();
                    ((ListaCarrito) miCtext).calcularCantidadItems();
                    ((ListaCarrito) miCtext).calcularMontoTotal();
                }

            }
        });


        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int CantidadActual = Integer.parseInt(cantidad_car1);
                if (carritoModelo.getCantidadProducto() != 1) {
                    CantidadActual--;
                    carritoModelo.disminuirCantidad();

                    //holder.edtCantidadCarrito.setText(Integer.toString(CantidadActual));
                    /*String url2 = "https://raygymproject.000webhostapp.com/actualizarCantidadCarritoTemp.php?idcar=" + holder.tvIdCarrito.getText().toString() +
                            "&cantidad_car=" +CantidadActual;*/
                    String url2 = "https://www.rgym.online/gymsystem/vmovil/actualizarCantidadCarritoTemp.php?idcar=" + holder.tvIdCarrito.getText().toString() +
                            "&cantidad_car=" +CantidadActual;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(miCtext, response, Toast.LENGTH_SHORT).show();
                            ;

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(miCtext, error.toString(), Toast.LENGTH_SHORT).show();
                            ;
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("idcar", idcar1);
                            params.put("cantidad_car", cantidad_car1);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(miCtext);
                    queue.add(stringRequest);
                    notifyDataSetChanged();
                    ((ListaCarrito) miCtext).calcularCantidadItems();
                    ((ListaCarrito) miCtext).calcularMontoTotal();
                }

            }
        });



    }



    @Override
    public int getItemCount() {
        return carritoList.size();
    }




    class CarritoHolder extends RecyclerView.ViewHolder {

        TextView tvCantxPr;
        TextView tvNombreProductoCarrito, tvPrecioProductoCarrito, tvIdCarrito,tvUsuario;
        EditText edtCantidadCarrito;
        ImageView imgPlatilloDC, btnEliminarPCarrito,btnAdd,btnDecrease;

        public CarritoHolder(@NonNull View itemView) {
            super(itemView);

            tvCantxPr = itemView.findViewById(R.id.tvCantxPr);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvIdCarrito = itemView.findViewById(R.id.tvIdCarrito);
            tvNombreProductoCarrito = itemView.findViewById(R.id.tvNombreProductoCarrito);
            tvPrecioProductoCarrito = itemView.findViewById(R.id.tvPrecioProductoCarrito);
            edtCantidadCarrito = itemView.findViewById(R.id.edtCantidadCarrito);
            imgPlatilloDC = itemView.findViewById(R.id.imgPlatilloDC);
            btnEliminarPCarrito = itemView.findViewById(R.id.btnEliminarPCarrito);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }

    public void Delete(int item) {
        carritoList.remove(item);

        notifyItemRemoved(item);
    }


}