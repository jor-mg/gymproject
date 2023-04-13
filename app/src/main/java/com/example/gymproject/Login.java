package com.example.gymproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.paquete.Paquetes;

import org.json.JSONArray;
import org.json.JSONException;

public class Login extends AppCompatActivity {

    EditText txtusuario, txtcontraseña;
    String usuario, contraseña, nombrec, base64t, urlImagen, identificadorCliente;
    String dato;
    Button btnredeswsp;
    TextView a, b, txtnombrecito, contacts, txtcomprarmembresia, txtidcliente;
    Button btnlogin;
    RequestQueue request;
    JSONArray jsonArray;
    ImageView imgcontra;
    SharedPreferences preferences;
    //String llave = sesion;
    String _urlwsp = "https://wa.me/51962724425?text=Hola!%20necesito%20información%20por%20favor.";
    private boolean esVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txtusuario = (EditText) findViewById(R.id.txtusuario);
        txtcontraseña = (EditText) findViewById(R.id.txtcontraseña);
        a = (TextView) findViewById(R.id.a);
        b = (TextView) findViewById(R.id.b);
        txtnombrecito = (TextView) findViewById(R.id.txtnombrecito);
        contacts = (TextView) findViewById(R.id.contacts);
        txtcomprarmembresia = findViewById(R.id.txtcomprarmembresia);
        imgcontra = (ImageView) findViewById(R.id.imgcontra);
        txtidcliente = findViewById(R.id.txtidcliente);

        btnlogin = (Button) findViewById(R.id.btnlogin);

        btnredeswsp = (Button) findViewById(R.id.btnredeswsp);
        recuperarDatos();

        /*try {
            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        txtcomprarmembresia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Paquetes.class);
                startActivity(intent);
            }
        });


        btnredeswsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri _link = Uri.parse(_urlwsp);
                Intent i = new Intent(Intent.ACTION_VIEW, _link);
                startActivity(i);
                Log.d("click", "ok");
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                iniciarSesion();
            }
        });

        imgcontra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visualizar();
            }
        });

        if (revisarSesion()) {
            startActivity(new Intent(this, MenuPrincipal.class));
            finish();
        } else {
            String mensaje = "";
            //Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show();
        }


    }


    private void visualizar() {
        if (!esVisible) {
            txtcontraseña.setTransformationMethod(PasswordTransformationMethod.getInstance());
            esVisible = true;

        } else {
            txtcontraseña.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            esVisible = false;

        }

    }


    private void iniciarSesion() {
        usuario = txtusuario.getText().toString();
        contraseña = txtcontraseña.getText().toString();

        request = Volley.newRequestQueue(this);
        //String url="https://raygymproject.000webhostapp.com/login2.php?usu_usuario="+usuario;
        //String url = "https://www.rgym.online/gymsystem/vmovil/login2.php?usu_usuario=" + usuario;
        String url = "https://rgym.online/gymsystem/vmovil/login2.php?usu_usucli=" + usuario;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String pass = jsonArray.getString(0);
                    String nombre = jsonArray.getString(1);
                    String apellido = jsonArray.getString(2);
                    String imagencita = jsonArray.getString(3);
                    String idCli = jsonArray.getString(4);
                    if (pass.equals(contraseña)) {
                        txtnombrecito.setText(nombre + " " + apellido);
                        nombrec = txtnombrecito.getText().toString();
                        contacts.setText(imagencita);
                        urlImagen = contacts.getText().toString();
                        //base64t = contacts.getText().toString();
                        txtidcliente.setText(idCli);
                        identificadorCliente = txtidcliente.getText().toString();

                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                        startActivity(intent);
                        guardarDatos();

                        finish();
                    } else {
                        a.setVisibility(View.VISIBLE);
                        b.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Verifique su usuario o contraseña", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    a.setVisibility(View.VISIBLE);
                    b.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Usuario no válido", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(stringRequest);

    }

    private void guardarDatos() {
        //SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        preferences = this.getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("contraseña", contraseña);
        editor.putString("nombrecompleto", nombrec);
        editor.putString("imagen", urlImagen);
        editor.putString("idcliente", identificadorCliente);
        //editor.putString("base64", base64t);
        editor.putBoolean("sesion", true);
        editor.apply();
        editor.commit();
    }

    private void recuperarDatos() {
        //SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        preferences = this.getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        txtusuario.setText(preferences.getString("usuario", ""));
        txtcontraseña.setText(preferences.getString("contraseña", ""));
        txtnombrecito.setText(preferences.getString("nombrecompleto", ""));
        contacts.setText(preferences.getString("imagen", ""));
        txtidcliente.setText(preferences.getString("idcliente", ""));
    }

    private boolean revisarSesion() {

        return this.preferences.getBoolean("sesion", false);

    }
}