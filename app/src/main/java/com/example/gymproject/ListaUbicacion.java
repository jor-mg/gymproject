package com.example.gymproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaUbicacion extends AppCompatActivity {

    Toolbar toolbarUbicacion;
    ActionBar mActionBar;
    ImageView btneditar, btnver;
    TextView txtdireccionActual, txtreferencia1Actual, txtreferencia2Actual, txtreferencia3Actual;
    String direccionActual, referencia1Actual, referencia2Actual , referencia3Actual, refLatitud, reflongitude;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ubicacion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbarUbicacion = findViewById(R.id.toolbarUbicacion);
        setSupportActionBar(toolbarUbicacion);
        mActionBar = getSupportActionBar();

        txtdireccionActual = findViewById(R.id.txtdireccionActual);
        txtreferencia1Actual = findViewById(R.id.txtreferencia1Actual);
        txtreferencia2Actual = findViewById(R.id.txtreferencia2Actual);
        txtreferencia3Actual = findViewById(R.id.txtreferencia3Actual);

        btneditar = findViewById(R.id.btneditar);
        btnver = findViewById(R.id.btnver);


        preferences = getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
        direccionActual = preferences.getString("direccion", "");
        referencia1Actual = preferences.getString("referencia1", "");
        referencia2Actual = preferences.getString("referencia2", "");
        referencia3Actual = preferences.getString("referencia3", "");
        refLatitud = preferences.getString("latitud", "");
        reflongitude = preferences.getString("longitud", "");


        txtdireccionActual.setText(direccionActual);
        txtreferencia1Actual.setText(referencia1Actual);
        txtreferencia2Actual.setText(referencia2Actual);
        txtreferencia3Actual.setText(referencia3Actual);




        btneditar.setOnClickListener(v -> {
            Intent intent = new Intent(ListaUbicacion.this, UbicacionPedido.class);
            startActivity(intent);
        });

        btnver.setOnClickListener(v ->{
            String urlGoogleMaps = "https://maps.google.com/?q="+refLatitud+","+reflongitude;
            Uri _link = Uri.parse(urlGoogleMaps);
            Intent i = new Intent(Intent.ACTION_VIEW, _link);
            startActivity(i);
        });

    }




}