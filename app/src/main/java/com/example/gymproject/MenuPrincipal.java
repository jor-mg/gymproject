package com.example.gymproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.adapter.PedidoFinal.ListaPedido;
import com.example.gymproject.perfil.PerfilCliente;
import com.example.gymproject.producto.MenuPrincipalProductos;
import com.example.gymproject.producto.Productos;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout lyentrenamiento,lyproductos,lypago,lysalir,lytipo;
    TextView lbluser,txthora,lblusuarioprueba2,lblusuarioprueba,lblbienvenido3;
    String nombretemporal,usuario,contraseña,img64,urlImg,prueba20;
    DrawerLayout drawerLayout;
    NavigationView navegador;
    Toolbar toolbarr;
    ImageView imagenId,imgActualizar;
    ProgressDialog progressDialog;
    FloatingActionButton fabgg, fabwsp, fabmsg, fabig;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    RequestQueue requestQueue;
    SharedPreferences.Editor editor;
    ProgressBar actualizarContenido;
    boolean isOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_principal);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        lyentrenamiento = (LinearLayout) findViewById(R.id.lyentrenamiento);
        lyproductos = (LinearLayout) findViewById(R.id.lyproductos);
        lypago = (LinearLayout) findViewById(R.id.lypago);
        lysalir = (LinearLayout) findViewById(R.id.lysalir);
        lytipo = (LinearLayout) findViewById(R.id.lytipo);

        imagenId = (ImageView) findViewById(R.id.imagenId);
        imgActualizar = findViewById(R.id.imgActualizar);
        actualizarContenido = findViewById(R.id.actualizarContenido);

        fabgg = findViewById(R.id.fabgg);
        fabwsp = findViewById(R.id.fabwsp);
        fabmsg = findViewById(R.id.fabmsg);
        fabig = findViewById(R.id.fabig);

        ///animacion botones flotantes
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backforward);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        drawerLayout = findViewById(R.id.drawe_layout);
        navegador = findViewById(R.id.navegador);
        toolbarr =  findViewById(R.id.toolbarr);
       toolbarr.setTitle("");

        setSupportActionBar(toolbarr);

        Menu menu = navegador.getMenu();
       // menu.findItem(R.id.nav_salir).setVisible(false);*/

        navegador.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbarr,R.string.nave_open,R.string.nave_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navegador.setNavigationItemSelectedListener(this);

        navegador.setCheckedItem(R.id.nav_cliente);

        lblusuarioprueba2 = (TextView) findViewById(R.id.lblusuarioprueba2);
        lblusuarioprueba = (TextView) findViewById(R.id.lblusuarioprueba);
        lblbienvenido3 = findViewById(R.id.lblbienvenido3);
        lbluser = (TextView) findViewById(R.id.lbluser);
        lbluser.setGravity(Gravity.CENTER);
        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        nombretemporal = preferences.getString("nombrecompleto", "");
        usuario = preferences.getString("usuario", "");
        //contraseña = preferences.getString("contraseña", "");
        img64 = preferences.getString("imagen", "");
        prueba20 = preferences.getString("idcliente", "");


        lbluser.setText(nombretemporal);
        lblusuarioprueba2.setText(usuario);
        lblusuarioprueba.setText(img64);


        buscarImagenCliente("https://www.rgym.online/gymsystem/vmovil/buscarImagen.php?usu_usucli="+usuario);

        //urlImg = "\"" + img64 + "\"";
        urlImg = "" + img64 + "";
        //lblbienvenido3.setText(urlImg);
        //Toast.makeText(getApplicationContext(), urlImg, Toast.LENGTH_SHORT).show();

        //MOSTRAR IMAGEN USUARIO//
        /*Picasso.get()
                .load(urlImg)
                .error(R.mipmap.ic_launcher_round)
                .into(imagenId);*/

        /// HORA Y FECHA DEL SISTEMA///
        txthora = (TextView) findViewById(R.id.txthora);



        //DECODFICAR BASE64//
        /*byte[] bytes = Base64.decode(img64, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);

        imagenId.setImageBitmap(bitmap);*/

        imgActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarContenido.setVisibility(View.VISIBLE);
                buscarImagenCliente("https://www.rgym.online/gymsystem/vmovil/buscarImagen.php?usu_usucli="+usuario);
            }
        });



        lysalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(MenuPrincipal.this, Login.class);
                startActivity(intent );
                finish();
            }
        });

        lyentrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(MenuPrincipal.this, Programas.class);
                startActivity(picture_intent );
            }
        });

        lyproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(MenuPrincipal.this, MenuPrincipalProductos.class);
                startActivity(picture_intent );
            }
        });

        lypago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(MenuPrincipal.this, Retos.class);
                startActivity(picture_intent );
            }
        });

        lytipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(MenuPrincipal.this, Productos.class);
                startActivity(picture_intent );
            }
        });

        fabgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedFab();
            }
        });

        fabig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedFab();
                Toast.makeText(MenuPrincipal.this, "Instagram", Toast.LENGTH_SHORT).show();
            }
        });
        fabmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedFab();
                Toast.makeText(MenuPrincipal.this, "Messenger", Toast.LENGTH_SHORT).show();
            }
        });
        fabwsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedFab();
                Toast.makeText(MenuPrincipal.this, "WhatsApp", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void animatedFab(){
        if (isOpen){
            //fabgg.startAnimation(rotateForward);
            fabig.startAnimation(fabClose);
            fabmsg.startAnimation(fabClose);
            fabwsp.startAnimation(fabClose);

            fabig.setClickable(false);
            fabmsg.setClickable(false);
            fabwsp.setClickable(false);
            isOpen = false;
        }
        else{
            //fabgg.startAnimation(rotateBackward);
            fabig.startAnimation(fabOpen);
            fabmsg.startAnimation(fabOpen);
            fabwsp.startAnimation(fabOpen);

            fabig.setClickable(true);
            fabmsg.setClickable(true);
            fabwsp.setClickable(true);
            isOpen = true;
        }
    }


    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            super.onBackPressed();

        }
        else{
            super.onBackPressed();

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_cliente:
                Intent intent1 = new Intent(MenuPrincipal.this, PerfilCliente.class);
                startActivity(intent1);
                break;
            case R.id.nav_pedido:
                Intent intent2 = new Intent(MenuPrincipal.this, ListaPedido.class);
                startActivity(intent2);
                break;
            case R.id.nav_membresia:
                //Intent intent3 = new Intent(MenuPrincipal.this, MenuPrincipal.class);
                //startActivity(intent3);
                break;
            case R.id.nav_pago:
                //Intent intent4 = new Intent(MenuPrincipal.this, ListaTipo.class);
                //startActivity(intent4);
                break;
            case R.id.nav_pass:
                View cambiarContraseña = LayoutInflater.from(MenuPrincipal.this).
                        inflate(R.layout.activity_nueva_contra, null);
                EditText txtoldpass = cambiarContraseña.findViewById(R.id.txtoldpass);
                EditText txtnewpass = cambiarContraseña.findViewById(R.id.txtnewpass);
                EditText txtconfirmpass = cambiarContraseña.findViewById(R.id.txtconfirmpass);
                EditText txtidusuario = cambiarContraseña.findViewById(R.id.txtidusuario);


                AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
                builder.setTitle("Cambiar contraseña");
                builder.setView(cambiarContraseña);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idusu = txtidusuario.getText().toString().trim();
                        String oldpass = txtoldpass.getText().toString().trim();
                        String newpass = txtnewpass.getText().toString().trim();
                        String confirpass = txtconfirmpass.getText().toString().trim();

                        if (oldpass.isEmpty() || newpass.isEmpty() || idusu.isEmpty() || confirpass.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Completo todos los campos correctamente", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.show();
                            //String url = "https://xprojectgymx.000webhostapp.com/login/recuperarlogin2.php";
                            String url = "https://www.rgym.online/gymsystem/vmovil/recuperarlogin2.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
                                    preferences.edit().clear().commit();

                                    SharedPreferences preferences2 = getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
                                    preferences2.edit().clear().commit();

                                    Intent intent = new Intent(MenuPrincipal.this, Login.class);
                                    startActivity(intent );
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("usu_usucli",idusu);
                                    params.put("oldpassword",oldpass);
                                    params.put("newpassword",newpass);
                                    params.put("confirmpass",confirpass);
                                    return params;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(MenuPrincipal.this);
                            queue.add(stringRequest);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.nav_salir:
                SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                SharedPreferences preferences2 = getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
                preferences2.edit().clear().commit();

                Intent intent = new Intent(MenuPrincipal.this, Login.class);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
    }

    private void buscarImagenCliente(String urlImgCliente){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlImgCliente, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int f= 0; f < response.length(); f++){
                            try {
                                JSONObject objeto = new JSONObject(response.get(f).toString());
                                String url1 = objeto.getString("img_usucli");
                                String url2 = "" + url1 + "";
                                Picasso.get()
                                        .load(url2)
                                        .error(R.mipmap.ic_launcher_round)
                                        .into(imagenId);
                                actualizarContenido.setVisibility(View.GONE);
                            } catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }



}