package com.example.gymproject.paquete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.R;
import com.example.gymproject.adapter.PaqueteAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Paquetes extends AppCompatActivity {


    private RecyclerView recyclerPaquete;
    private static String url="https://raygymproject.000webhostapp.com/listarPaquetes.php";
    List<PaqueteModelo> listaPaquete2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paquetes);

        recyclerPaquete = findViewById(R.id.rePaquetes);
        recyclerPaquete.setHasFixedSize(true);
        recyclerPaquete.setLayoutManager(new LinearLayoutManager(this));
        //recyclerPaquete.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listaPaquete2 = new ArrayList<>();

        mostrarLista();
    }

    private void mostrarLista(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject PaqueteModelo = array.getJSONObject(i);

                        listaPaquete2.add(new PaqueteModelo(
                                //PaqueteModelo.getInt("id");
                                PaqueteModelo.getString("nombre_tip"),
                                PaqueteModelo.getString("precio_tip"),
                                PaqueteModelo.getString("img")

                        ));
                    }

                    PaqueteAdapter paqueteAdapter = new PaqueteAdapter(Paquetes.this, listaPaquete2);
                    recyclerPaquete.setAdapter(paqueteAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

    }
}