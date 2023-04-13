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
import android.widget.ProgressBar;
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


public class PedidoProgreso extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    RecyclerView rePedidoProgreso;
    PedidoHistorialAdapter adapterPP;
    ArrayList<PedidoHistorialModelo> listaPedidosProgreso;
    StringRequest stringRequest;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    LinearLayout contenedor_pedidoprogreso_vacio;

    TextView txtUsuarioProgreso;

    SwipeRefreshLayout swipeRefreshLayoutProgresoPedido;


    private ProgressBar progressbarPro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pedido_progreso, container, false);

        swipeRefreshLayoutProgresoPedido = view.findViewById(R.id.swipeRefreshLayoutProgresoPedido);
        swipeRefreshLayoutProgresoPedido.setOnRefreshListener(this);

        contenedor_pedidoprogreso_vacio = view.findViewById(R.id.contenedor_pedidoprogreso_vacio);

        txtUsuarioProgreso = view.findViewById(R.id.txtUsuarioProgreso);

        listaPedidosProgreso = new ArrayList<>();

        rePedidoProgreso = (RecyclerView) view.findViewById(R.id.rePedidoProgreso);
        rePedidoProgreso.setLayoutManager(new LinearLayoutManager(getContext()));
        rePedidoProgreso.setHasFixedSize(true);

        request = Volley.newRequestQueue(getContext());

        SharedPreferences preferences = getActivity().getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String idCliente = preferences.getString("idcliente", "");
        txtUsuarioProgreso.setText(idCliente);

        listarHistorialPedidos("https://www.rgym.online/gymsystem/vmovil/listarProgresoPedido.php?id_cli="+idCliente);

        return view;


    }

    private void listarHistorialPedidos(String urlPp){
        //progressbarHP.setVisibility(View.VISIBLE);

        swipeRefreshLayoutProgresoPedido.setRefreshing(true);
        listaPedidosProgreso.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPp,
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
                                listaPedidosProgreso.add(pedidoHistorialModelo);

                            }
                            adapterPP = new PedidoHistorialAdapter(getContext(),listaPedidosProgreso);
                            rePedidoProgreso.setAdapter(adapterPP);

                            int cantidadProgreso = Integer.valueOf(listaPedidosProgreso.size());
                            if (cantidadProgreso == 0){
                                contenedor_pedidoprogreso_vacio.setVisibility(View.VISIBLE);
                            }

                            swipeRefreshLayoutProgresoPedido.setRefreshing(false);
                        }catch (Exception e){

                        }


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
        listarHistorialPedidos("https://www.rgym.online/gymsystem/vmovil/listarProgresoPedido.php?id_cli="+idCliente);
    }
}