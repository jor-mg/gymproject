package com.example.gymproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.producto.Productos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Programas extends AppCompatActivity {

    LinearLayout layoutmp, layoutop;
    TextView txt_id_cliente;
    RequestQueue request;
    String nomEntrenador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        request = Volley.newRequestQueue(getApplicationContext());
        layoutmp = findViewById(R.id.layoutmp);
        layoutop = findViewById(R.id.layoutop);

        txt_id_cliente = findViewById(R.id.txt_id_cliente);

        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String idcliente = preferences.getString("idcliente", "");

        Toast.makeText(Programas.this, ""+idcliente, Toast.LENGTH_SHORT).show();

        mostrarDatosRut("https://rgym.online/gymsystem/vmovil/buscarMembresia.php?id_cli="+idcliente);

        Toast.makeText(Programas.this, "aaa"+txt_id_cliente.getText().toString(), Toast.LENGTH_SHORT).show();

        layoutmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Programas.this, DiasEntrenamiento.class);
                //startActivity(intent);
                Toast.makeText(Programas.this, "Mi programa", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Programas.this, DiasEntrenamiento.class);
                intent.putExtra("id_mem",txt_id_cliente.getText().toString());
                intent.putExtra("nombreEntrenador",nomEntrenador);
                startActivity(intent);

            }
        });
        layoutop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Programas.this, "Otros programas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbarPro);
        toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        toolbar.setNavigationOnClickListener(v -> {

            this.onBackPressed();
            Programas.this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
        });
    }

    private void mostrarDatosRut(String urlMem) {

        txt_id_cliente.setText("");
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, urlMem, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int f = 0; f < response.length(); f++) {
                            try {
                                JSONObject objeto = new JSONObject(response.get(f).toString());
                                txt_id_cliente.setText(objeto.getString("id_mem"));
                                nomEntrenador = objeto.getString("nombrec");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(requerimiento);
    }
}