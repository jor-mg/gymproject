package com.example.gymproject.adapter.PedidoFinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.R;
import com.example.gymproject.adapter.PedidoHistorialAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;




public class PedidoHistorial extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    View view;
    RecyclerView rePedidoHistorial;
    PedidoHistorialAdapter adapterHP;
    ArrayList<PedidoHistorialModelo> listaPedidosHistorial;
    StringRequest stringRequest;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    LinearLayout contenedor_pedidohistorial_vacio;

    TextView txtUsuarioHistorial;

    SwipeRefreshLayout swipeRefreshLayoutHistorialPedidos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pedido_historial, container, false);

        swipeRefreshLayoutHistorialPedidos = view.findViewById(R.id.swipeRefreshLayoutHistorialPedidos);
        swipeRefreshLayoutHistorialPedidos.setOnRefreshListener(this);

        contenedor_pedidohistorial_vacio = view.findViewById(R.id.contenedor_pedidohistorial_vacio);

        txtUsuarioHistorial = view.findViewById(R.id.txtUsuarioHistorial);

        listaPedidosHistorial = new ArrayList<>();

        rePedidoHistorial = (RecyclerView) view.findViewById(R.id.rePedidoHistorial);
        rePedidoHistorial.setLayoutManager(new LinearLayoutManager(getContext()));
        rePedidoHistorial.setHasFixedSize(true);

        request = Volley.newRequestQueue(getContext());

        SharedPreferences preferences = getActivity().getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String idCliente = preferences.getString("idcliente", "");
        txtUsuarioHistorial.setText(idCliente);

        listarHistorialPedidos("https://www.rgym.online/gymsystem/vmovil/listarHistorialPedido.php?id_cli="+idCliente);




        return view;
    }



    private void listarHistorialPedidos(String urlP){
        //progressbarHP.setVisibility(View.VISIBLE);

        swipeRefreshLayoutHistorialPedidos.setRefreshing(true);
        listaPedidosHistorial.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //progressbarHP.setVisibility(View.GONE);

                        PedidoHistorialModelo pedidoHistorialModelo = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                pedidoHistorialModelo = new PedidoHistorialModelo();
                                JSONObject jsonObject = null;
                                jsonObject = array.getJSONObject(i);

                                pedidoHistorialModelo.setEstado(jsonObject.optString("est_ped"));
                                pedidoHistorialModelo.setNropedido(jsonObject.optString("id_ped"));
                                pedidoHistorialModelo.setTotal(jsonObject.optString("tot_ped"));
                                pedidoHistorialModelo.setFecha(jsonObject.optString("fec_ped"));
                                pedidoHistorialModelo.setTipo(jsonObject.optString("tip_ped"));
                                pedidoHistorialModelo.setDir(jsonObject.optString("dir_ped"));

                                listaPedidosHistorial.add(pedidoHistorialModelo);

                            }
                            adapterHP = new PedidoHistorialAdapter(getContext(),listaPedidosHistorial);
                            rePedidoHistorial.setAdapter(adapterHP);

                            int cantidadProgresoo = Integer.valueOf(listaPedidosHistorial.size());
                            if (cantidadProgresoo == 0){
                                contenedor_pedidohistorial_vacio.setVisibility(View.VISIBLE);
                                //rePedidoHistorial.setVisibility(View.GONE);
                            }

                            swipeRefreshLayoutHistorialPedidos.setRefreshing(false);
                        }catch (Exception e){

                        }

                        //mAdapter = new ProductoAdapter(Productos.this,products);
                        // adapterP = new ProductoAdapter(Productos.this,products);
                        //recyclerView.setAdapter(adapterP);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getContext(), error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(getContext()).add(stringRequest);

    }


    @Override
    public void onRefresh() {
        SharedPreferences preferences = getActivity().getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String idCliente = preferences.getString("idcliente", "");
        listarHistorialPedidos("https://www.rgym.online/gymsystem/vmovil/listarHistorialPedido.php?id_cli="+idCliente);
    }


}