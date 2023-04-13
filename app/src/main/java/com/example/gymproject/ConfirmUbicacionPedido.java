package com.example.gymproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymproject.producto.MenuPrincipalProductos;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ConfirmUbicacionPedido extends AppCompatActivity {

    LinearLayout lytcasa, lyttrabajo, lytapart, lytotro;
    Button btnguardar;
    TextView txttxtotradireccioneti,txtotradireccion,txtreferencia;
    String otraref;
    SharedPreferences preferences;
    String direc, ref1, ref2, ref3, lati, logi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ubicacion_pedido);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txttxtotradireccioneti = findViewById(R.id.txttxtotradireccioneti);
        txtotradireccion = findViewById(R.id.txtotradireccion);
        txtreferencia = findViewById(R.id.txtreferencia);

        btnguardar = findViewById(R.id.btnguardar);

        lytcasa = findViewById(R.id.lytcasa);
        lyttrabajo = findViewById(R.id.lyttrabajo);
        lytapart = findViewById(R.id.lytapart);
        lytotro = findViewById(R.id.lytotro);

        //<--Obtener datos del SharedPreferencesUbicacion
        preferences = getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
        direc = preferences.getString("direccion", "");
        ref1 = preferences.getString("referencia1", "");
        /*String ref2A = preferences.getString("referencia2", "");
        String ref3A = preferences.getString("referencia3", "");
        txtreferencia.setText(ref2A);
        txtotradireccion.setText(ref3A);*/
        lati = preferences.getString("latitud", "");
        logi = preferences.getString("longitud", "");
        Toast.makeText(ConfirmUbicacionPedido.this, "Direccion: "+direc + "/a:" +ref1 + "Latitud:" + lati + "\nLongitud:" +logi , Toast.LENGTH_SHORT).show();
        //Fin->>

        btnguardar.setOnClickListener(v -> {
            ref2 = txtreferencia.getText().toString();
            ref3 = txtotradireccion.getText().toString();
            guardarDatos();
            Toast.makeText(ConfirmUbicacionPedido.this, "Dir: " + direc + "\nRef1: " + ref1 + "\nRef2: " + ref2 + "\nRef3: " +ref3+ lati + "\nLongitud:" +logi, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ConfirmUbicacionPedido.this, MenuPrincipalProductos.class);
            startActivity(intent);
        });

        lytcasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytcasa.setBackground(getDrawable(R.drawable.background_input4));
                lyttrabajo.setBackground(getDrawable(R.drawable.background_input3));
                lytapart.setBackground(getDrawable(R.drawable.background_input3));
                lytotro.setBackground(getDrawable(R.drawable.background_input3));
                ///////////////////////////////////////////////////////////////////
                txtotradireccion.setVisibility(View.GONE);
                txtotradireccion.setText("Casa");
            }
        });

        lyttrabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyttrabajo.setBackground(getDrawable(R.drawable.background_input4));
                lytcasa.setBackground(getDrawable(R.drawable.background_input3));
                lytapart.setBackground(getDrawable(R.drawable.background_input3));
                lytotro.setBackground(getDrawable(R.drawable.background_input3));
                ///////////////////////////////////////////////////////////////////
                txtotradireccion.setVisibility(View.GONE);
                txtotradireccion.setText("Trabajo");
            }
        });

        lytapart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytapart.setBackground(getDrawable(R.drawable.background_input4));
                lytcasa.setBackground(getDrawable(R.drawable.background_input3));
                lyttrabajo.setBackground(getDrawable(R.drawable.background_input3));
                lytotro.setBackground(getDrawable(R.drawable.background_input3));
                ///////////////////////////////////////////////////////////////////
                txtotradireccion.setVisibility(View.GONE);
                txtotradireccion.setText("Apartamento");
            }
        });

        lytotro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytotro.setBackground(getDrawable(R.drawable.background_input4));
                lytapart.setBackground(getDrawable(R.drawable.background_input3));
                lyttrabajo.setBackground(getDrawable(R.drawable.background_input3));
                lytcasa.setBackground(getDrawable(R.drawable.background_input3));
                ///////////////////////////////////////////////////////////////////
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ConfirmUbicacionPedido.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                EditText txtotro = bottomSheetDialog.findViewById(R.id.txtotro);
                Button btnconfirmarotro = bottomSheetDialog.findViewById(R.id.btnconfirmarotro);
                bottomSheetDialog.show();

                btnconfirmarotro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        otraref = txtotro.getText().toString();
                        if (otraref.length() > 0) {
                            txttxtotradireccioneti.setVisibility(View.VISIBLE);
                            txtotradireccion.setVisibility(View.VISIBLE);
                            txtotradireccion.setText("" + otraref);
                        }else{
                            txttxtotradireccioneti.setVisibility(View.GONE);
                            txtotradireccion.setVisibility(View.GONE);
                            Toast.makeText(ConfirmUbicacionPedido.this,"¡UPS!, no ingresó datos", Toast.LENGTH_SHORT).show();
                        }
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });
    }


    private void guardarDatos(){

        preferences = this.getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("direccion", direc);
        editor.putString("referencia1", ref1);
        editor.putString("referencia2", ref2);
        editor.putString("referencia3", ref3);
        editor.putString("latitud", lati);
        editor.putString("longitud", logi);
        editor.apply();
    }


}