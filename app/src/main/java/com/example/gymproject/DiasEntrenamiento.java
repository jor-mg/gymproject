package com.example.gymproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.adapter.DiaAdapter;
import com.example.gymproject.adapter.PaqueteAdapter;
import com.example.gymproject.adapter.PedidoFinal.PedidoHistorialModelo;
import com.example.gymproject.paquete.PaqueteModelo;
import com.example.gymproject.paquete.Paquetes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiasEntrenamiento extends AppCompatActivity {

    Toolbar toolbarDiasEntrenamiento;
    RecyclerView reDiasEntrenamiento;
    List<DiaModelo> listaDias;
    RequestQueue request;
    ProgressBar progressbar_dias;

    TextView pruebamem,txt_nombre_entrenador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias_entrenamiento);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbarDiasEntrenamiento = findViewById(R.id.toolbarDiasEntrenamiento);
        setSupportActionBar(toolbarDiasEntrenamiento);

        listaDias = new ArrayList<>();

        reDiasEntrenamiento = (RecyclerView) findViewById(R.id.reDiasEntrenamiento);
        reDiasEntrenamiento.setHasFixedSize(true);
        reDiasEntrenamiento.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        request = Volley.newRequestQueue(DiasEntrenamiento.this);

        progressbar_dias = findViewById(R.id.progressbar_dias);


        pruebamem = findViewById(R.id.pruebamem);
        txt_nombre_entrenador = findViewById(R.id.txt_nombre_entrenador);

        Intent intent = getIntent();
        String membresia_id = intent.getStringExtra("id_mem");
        String nombreEntrenador = intent.getStringExtra("nombreEntrenador");
        if (intent !=null){
            pruebamem.setText(membresia_id);
            txt_nombre_entrenador.setText(nombreEntrenador);
        }


        SharedPreferences.Editor editor = getSharedPreferences("guardarIdMembresia", MODE_PRIVATE).edit();
        editor.putString("id_membresia", membresia_id);
        editor.apply();

        mostrarDiasEntrenamiento("https://rgym.online/gymsystem/vmovil/listarDiasEntrenamiento.php?id_mem="+membresia_id);

    }

    private void mostrarDiasEntrenamiento(String urlDias){
        progressbar_dias.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlDias,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressbar_dias.setVisibility(View.GONE);
                        DiaModelo diaModelo = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                diaModelo = new DiaModelo();
                                JSONObject jsonObject = null;
                                jsonObject = array.getJSONObject(i);

                                diaModelo.setDia(jsonObject.optString("dia_det_rut"));
                                diaModelo.setMusculo(jsonObject.optString("nom_mus"));

                                listaDias.add(diaModelo);

                            }

                            DiaAdapter diaAdapter = new DiaAdapter(DiasEntrenamiento.this, listaDias);
                            reDiasEntrenamiento.setAdapter(diaAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        request.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void enviarIdMembresia(){

    }
}