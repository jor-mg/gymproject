package com.example.gymproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.adapter.ProductoAdapter;
import com.example.gymproject.producto.MenuPrincipalProductos;
import com.example.gymproject.producto.ProductoModelo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RutinaxDia extends AppCompatActivity {

    ProgressBar progress_ejercios;
    TextView txtDiaRutina, txtMusculoDia;
    RecyclerView reRutina;
    RutinaxDiaAdapter rutinaAdapter;
    ArrayList<RutinaModelo> rutinaModelos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinax_dia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //<--Datos para web services
        txtDiaRutina = findViewById(R.id.txtDiaRutina);
        txtMusculoDia = findViewById(R.id.txtMusculoDia);

        SharedPreferences preferences = getSharedPreferences("guardarIdMembresia", Context.MODE_PRIVATE);
        String id_membresia = preferences.getString("id_membresia", "");

        Intent intent = getIntent();
        String dia = intent.getStringExtra("dia");
        String musculo = intent.getStringExtra("musculo");

        if (intent !=null){
            txtDiaRutina.setText(dia);
            txtMusculoDia.setText(musculo);
        }
        //Fin-->


        progress_ejercios = findViewById(R.id.progress_ejercios);

        reRutina = findViewById(R.id.reRutina);
        reRutina.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rutinaModelos = new ArrayList<>();




        EjerciciosRutina("https://rgym.online/gymsystem/vmovil/listarRutinaxDia.php?id_mem="+id_membresia+"&dia_det_rut="+dia);
    }

    private void EjerciciosRutina(String urlEjer){
        progress_ejercios.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEjer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_ejercios.setVisibility(View.GONE);
                        RutinaModelo rutinaModelo = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                rutinaModelo = new RutinaModelo();
                                JSONObject jsonObject = null;
                                jsonObject = array.getJSONObject(i);

                                rutinaModelo.setRepeticiones(jsonObject.optString("rep_det_rut"));
                                rutinaModelo.setSeries(jsonObject.optString("ser_det_rut"));
                                rutinaModelo.setNombre(jsonObject.optString("nom_ejer"));
                                rutinaModelo.setImagen(jsonObject.optString("img_ejer"));

                                rutinaModelos.add(rutinaModelo);

                            }
                            rutinaAdapter = new RutinaxDiaAdapter(RutinaxDia.this,rutinaModelos);
                            reRutina.setAdapter(rutinaAdapter);


                        }catch (Exception e){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(RutinaxDia.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(RutinaxDia.this).add(stringRequest);

    }
}