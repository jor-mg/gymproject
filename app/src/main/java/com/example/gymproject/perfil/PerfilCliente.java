package com.example.gymproject.perfil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerfilCliente extends AppCompatActivity {


    TextView misdatos, mismembresias;
    String nombreCliente, usuarioCliente, imgCliente, idCliente;
    TextView txtnombrecliente, txtusuarioCliente, txtnombrecliente2, txtdnicliente2, txtnumerocliente2, txtcorreocliente2, txtTipoMembresia, txtFechaInicioM, txtFechaFinalM;
    ImageView imagenIdCLiente;

    CardView contenedor_datos, contenedor_membresias, contenedor_compras;
    ShimmerFrameLayout shimmer_misdatos,shimmer_datos_cliente;
    LinearLayout contenedor_shimmer_datos, contenedor_datos_perfil;
    EditText cajaprueba;

    ImageButton btnabrirDatos, btncerrarDatos;
    ImageButton btnabrirMembresia, btncerrarMembresia;
    CardView crdMisDatos, crdMisMembresias;
    Button verMasMem;
    RequestQueue request;
    private Bitmap bitmap;
    private static final String urlImagenCliente = "https://rgym.online/gymsystem/img/img_usuario/actualizarImagenCliente.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbarPerfil = findViewById(R.id.toolbarPerfil);
        request = Volley.newRequestQueue(getApplicationContext());

        //imagenActuCLiente = findViewById(R.id.imagenActuCLiente);
        ///VARIABLES CONTENEDORES Y SHIMMER
        contenedor_datos = findViewById(R.id.contenedor_datos);
        contenedor_membresias = findViewById(R.id.contenedor_membresias);
        contenedor_compras = findViewById(R.id.contenedor_compras);
        contenedor_shimmer_datos = findViewById(R.id.contenedor_shimmer_datos);
        contenedor_datos_perfil = findViewById(R.id.contenedor_datos_perfil);

        shimmer_misdatos = findViewById(R.id.shimmer_misdatos);
        shimmer_datos_cliente = findViewById(R.id.shimmer_datos_cliente);
        shimmer_misdatos.startShimmer();
        shimmer_datos_cliente.startShimmer();

        cajaprueba = findViewById(R.id.cajaprueba);

        //VARIABLES DATOS DEL CLIENTE
        txtnombrecliente = findViewById(R.id.txtnombrecliente);
        txtusuarioCliente = findViewById(R.id.txtusuarioCliente);
        imagenIdCLiente = findViewById(R.id.imagenIdCLiente);
        txtnombrecliente2 = findViewById(R.id.txtnombrecliente2);
        txtdnicliente2 = findViewById(R.id.txtdnicliente2);
        txtnumerocliente2 = findViewById(R.id.txtnumerocliente2);
        txtcorreocliente2 = findViewById(R.id.txtcorreocliente2);

        txtTipoMembresia = findViewById(R.id.txtTipoMembresia);
        txtFechaInicioM = findViewById(R.id.txtFechaInicioM);
        txtFechaFinalM = findViewById(R.id.txtFechaFinalM);

        //ALMACENAR VARIABLES DEL CLIENTE
        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        nombreCliente = preferences.getString("nombrecompleto", "");
        usuarioCliente = preferences.getString("usuario", "");
        imgCliente = preferences.getString("imagen", "");
        idCliente = preferences.getString("idcliente", "");
        //SETEAR VARIABLES
        txtnombrecliente.setText(nombreCliente);
        txtusuarioCliente.setText("@" + usuarioCliente);
        txtnombrecliente2.setText(nombreCliente);
        txtdnicliente2.setText(usuarioCliente);
        //SETEAR IMG DEL CLIENTE
        /*String urlImg = "" + imgCliente + "";
        Picasso.get()
                .load(urlImg)
                .error(R.mipmap.ic_launcher_round)
                .into(imagenIdCLiente);*/

        buscarImagenCliente("https://www.rgym.online/gymsystem/vmovil/buscarImagen.php?usu_usucli="+usuarioCliente);

        //TEXTVIEW COLOR ANTERIOR
        misdatos = findViewById(R.id.misdatos);
        ColorStateList oldColorDatos = misdatos.getTextColors();

        mismembresias = findViewById(R.id.mismembresias);
        ColorStateList oldColorMembresias = mismembresias.getTextColors();


        //BUTTON ABRIR CERRAR
        btnabrirDatos = findViewById(R.id.btnabrirDatos);
        btncerrarDatos = findViewById(R.id.btncerrarDatos);

        btnabrirMembresia = findViewById(R.id.btnabrirMembresia);
        btncerrarMembresia = findViewById(R.id.btncerrarMembresia);

        ///CARDVIEW OCULTOS
        crdMisDatos = findViewById(R.id.crdMisDatos);
        crdMisMembresias = findViewById(R.id.crdMisMembresias);


        verMasMem = findViewById(R.id.verMasMem);
        verMasMem.setPaintFlags(verMasMem.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //TOOLBAR PERFIL CLIENTE
        setSupportActionBar(toolbarPerfil);
        ActionBar mActionBarPerfil = getSupportActionBar();
        mActionBarPerfil.setDisplayHomeAsUpEnabled(true);
        mActionBarPerfil.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_volver_atras));

        mActionBarPerfil.setTitle("Mi perfil");

        //////

        txtnombrecliente.setOnClickListener(v -> chooseFile());

        //BUTTON ABRIR CERRAR DATOS
        btnabrirDatos.setOnClickListener(v -> abrirDatos());

        btncerrarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarDatos();
                animarDatos(false);
                misdatos.setTextColor(oldColorDatos);
                misdatos.setTypeface(null, Typeface.NORMAL);
            }
        });

        //BUTTON ABRIR CERRAR MEMBRESIAS
        btnabrirMembresia.setOnClickListener(v -> abrirMembresia());

        btncerrarMembresia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarMembresia();
                animarMembresia(false);
                mismembresias.setTextColor(oldColorMembresias);
                mismembresias.setTypeface(null, Typeface.NORMAL);
            }
        });

        ///RECUPERAR DATOS DE LA BD
        recuperarDatosCliente("https://rgym.online/gymsystem/vmovil/listarDatosCliente.php?dni_per=" + usuarioCliente);
        recuperarDatosMembresiaCliente("https://rgym.online/gymsystem/vmovil/listarDatosMembresia.php?id_cli="+ idCliente);

    }

    //abrirDatos cerrarDatos
    private void abrirDatos() {
        btnabrirDatos.setVisibility(View.GONE);
        btncerrarDatos.setVisibility(View.VISIBLE);
        animarDatos(true);
        crdMisDatos.setVisibility(View.VISIBLE);
        misdatos.setTextColor(Color.BLACK);
        misdatos.setTypeface(null, Typeface.BOLD);
    }

    private void cerrarDatos() {
        btnabrirDatos.setVisibility(View.VISIBLE);
        btncerrarDatos.setVisibility(View.GONE);
        crdMisDatos.setVisibility(View.GONE);
    }


    //abrirMembresia cerrarMembresia
    private void abrirMembresia() {
        btnabrirMembresia.setVisibility(View.GONE);
        btncerrarMembresia.setVisibility(View.VISIBLE);
        animarMembresia(true);
        crdMisMembresias.setVisibility(View.VISIBLE);
        mismembresias.setTextColor(Color.BLACK);
        mismembresias.setTypeface(null, Typeface.BOLD);
    }

    private void cerrarMembresia() {
        btnabrirMembresia.setVisibility(View.VISIBLE);
        btncerrarMembresia.setVisibility(View.GONE);
        crdMisMembresias.setVisibility(View.GONE);
    }

    //animacion mostrar datos
    private void animarDatos(boolean mostrar) {
        AnimationSet set = new AnimationSet(true);
        Animation animation;
        if (mostrar) {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        } else {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(600);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        crdMisDatos.setLayoutAnimation(controller);
        crdMisDatos.startAnimation(animation);
    }

    //animacion mostrar membresia
    private void animarMembresia(boolean mostrar) {
        AnimationSet set = new AnimationSet(true);
        Animation animation;
        if (mostrar) {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        } else {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(600);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        crdMisMembresias.setLayoutAnimation(controller);
        crdMisMembresias.startAnimation(animation);
    }

    private void recuperarDatosCliente(String urlDatosCliente) {
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, urlDatosCliente, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int f = 0; f < response.length(); f++) {
                            try {
                                JSONObject objeto = new JSONObject(response.get(f).toString());
                                txtnumerocliente2.setText(objeto.getString("tel_pe"));
                                txtcorreocliente2.setText(objeto.getString("cor_per"));

                                //<--EMPIEZA SHIMMER DATOS CLIENTE
                                shimmer_misdatos.stopShimmer();
                                shimmer_misdatos.setVisibility(View.GONE);
                                contenedor_datos.setVisibility(View.VISIBLE);
                                contenedor_membresias.setVisibility(View.VISIBLE);
                                contenedor_compras.setVisibility(View.VISIBLE);
                                //TERMINA SHIMMER DATOS CLIENTE-->

                                //<--EMPIEZA SHIMMER HEAD
                                shimmer_datos_cliente.stopShimmer();
                                contenedor_shimmer_datos.setVisibility(View.GONE);
                                contenedor_datos_perfil.setVisibility(View.VISIBLE);
                                //TERMINA SHIMMER HEAD-->
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

    private void recuperarDatosMembresiaCliente(String urlDatosCliente) {
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, urlDatosCliente, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int f = 0; f < response.length(); f++) {
                            try {
                                JSONObject objeto = new JSONObject(response.get(f).toString());
                                txtTipoMembresia.setText(objeto.getString("nom_tip"));
                                txtFechaInicioM.setText(objeto.getString("fec_ini_mem"));
                                txtFechaFinalM.setText(objeto.getString("fecha_fin_mem"));
                                //<--EMPIEZA SHIMMER DATOS CLIENTE
                                shimmer_misdatos.stopShimmer();
                                shimmer_misdatos.setVisibility(View.GONE);
                                contenedor_datos.setVisibility(View.VISIBLE);
                                contenedor_membresias.setVisibility(View.VISIBLE);
                                contenedor_compras.setVisibility(View.VISIBLE);
                                //TERMINA SHIMMER DATOS CLIENTE-->

                                //<--EMPIEZA SHIMMER HEAD
                                shimmer_datos_cliente.stopShimmer();
                                contenedor_shimmer_datos.setVisibility(View.GONE);
                                contenedor_datos_perfil.setVisibility(View.VISIBLE);
                                //TERMINA SHIMMER HEAD-->
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

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagenIdCLiente.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            actualizarImagenCliente(usuarioCliente, getStringImage(bitmap));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodeImage;
    }

    private void actualizarImagenCliente(String usuarioClientee, String img_usuclii ) {

        img_usuclii.replaceAll(" ","");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlImagenCliente,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(getApplicationContext(), "EXITOSOR1", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "ERRORORROROJSONN"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERRORORRORO" +error.toString(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usu_usucli", usuarioClientee);
                params.put("img_usucli", img_usuclii);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                                        .into(imagenIdCLiente);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


}