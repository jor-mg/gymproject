package com.example.gymproject.adapter.CarritoDeCompra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.LoadingDialogCliente;
import com.example.gymproject.R;
import com.example.gymproject.UbicacionPedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PagoCarrito extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    Button selectMethod,applycode,btndelivery, btnhome,btnConfirmEnd,btnChangeDirec;
    TextView txtcostoDelivery,txtsubtotal,txtsubtotalCap,txttotal,txtusuarioCompra,txtTipoEntrega,txtdir1,txtdir2;
    ImageView imgInfo;
    LinearLayout laypre;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private double totalparcial = 0.00;
    private Integer preciodelivery = 5;

    boolean prueba = false;
    boolean isDelivery = false;
    boolean adicional = false;

    String dire1, dire2,dire3,dire4, lat, lon;
    String direccionCompleta, coordenadas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_carrito);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        request = Volley.newRequestQueue(getApplicationContext());

        txtcostoDelivery = findViewById(R.id.txtcostoDelivery);
        txtsubtotal = findViewById(R.id.txtsubtotal);
        txtsubtotalCap = findViewById(R.id.txtsubtotalCap);
        txttotal = findViewById(R.id.txttotal);
        txtusuarioCompra = findViewById(R.id.txtusuarioCompra);
        txtTipoEntrega = findViewById(R.id.txtTipoEntrega);
        txtdir1 = findViewById(R.id.txtdir1);
        txtdir2 = findViewById(R.id.txtdir2);

        imgInfo = findViewById(R.id.imgInfo);

        selectMethod = findViewById(R.id.selectMethod);
        applycode = findViewById(R.id.applycode);
        btndelivery = findViewById(R.id.btndelivery);
        btnhome = findViewById(R.id.btnhome);
        btnChangeDirec= findViewById(R.id.btnChangeDirec);
        btnConfirmEnd = findViewById(R.id.btnConfirmEnd);


        laypre = findViewById(R.id.laypre);
        laypre.setVisibility(View.GONE);


        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "");

        mostrarMontoT("https://www.rgym.online/gymsystem/vmovil/mostrarMontoTemporal.php?usu_usucli=" + usuario);


        txtusuarioCompra.setText(usuario);


        SharedPreferences preferences2 = getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
        dire1 = preferences2.getString("direccion", "");
        dire2 = preferences2.getString("referencia1", "");
        dire3 = preferences2.getString("referencia2", "");
        dire4 = preferences2.getString("referencia3", "");

        lat = preferences2.getString("latitud","");
        lon = preferences2.getString("longitud","");
        /*txtdir1.setText(dire1);
        txtdir2.setText(dire2);
        direccionCompleta = dire1 + ", " + dire2;*/


        final LoadingDialogCliente loadingDialogCliente = new LoadingDialogCliente(PagoCarrito.this);

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(PagoCarrito.this, "Información adicional", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirmEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (prueba == true){
                    if (isDelivery == true){
                        //VALIDAR UBICACION VACIO O COMPLETA
                        if (dire1.isEmpty() || dire2.isEmpty() ){
                            Toast.makeText(PagoCarrito.this, "Por favor, complete todos los datos de su ubicación", Toast.LENGTH_SHORT).show();
                        }else{
                            //EJECUTAR PROCEDIMIENTO ALMACENADO
                            btnConfirmEnd.setEnabled(true);
                            finalizarPedido();
                            Toast.makeText(PagoCarrito.this, "Ud. a confirmado un pedido", Toast.LENGTH_SHORT).show();
                            loadingDialogCliente.startLoadingDialog();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent= new Intent (PagoCarrito.this, ConfirmPagoDesign.class);
                                    startActivity(intent);
                                    loadingDialogCliente.dismissDialog();
                                }
                            }, 4000);

                        }
                    }else{
                        //EJECUTAR PROCEDIMIENTO ALMACENADO
                        btnConfirmEnd.setEnabled(true);
                        finalizarPedido();
                        Toast.makeText(PagoCarrito.this, "Ud. a confirmado un pedido", Toast.LENGTH_SHORT).show();
                        loadingDialogCliente.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent= new Intent (PagoCarrito.this, ConfirmPagoDesign.class);
                                startActivity(intent);
                                loadingDialogCliente.dismissDialog();
                            }
                        }, 4000);
                    }
                }else{
                    Toast.makeText(PagoCarrito.this, "Por favor, llene correctamente todos los campos", Toast.LENGTH_SHORT).show();
                }



                //////
            }
        });

        btndelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                laypre.setVisibility(View.VISIBLE);
                btndelivery.setBackgroundColor(getResources().getColor(R.color.principal));
                btndelivery.setTextColor(Color.BLACK);

                btnhome.setBackgroundColor(getResources().getColor(R.color.tercero));
                btnhome.setTextColor(Color.WHITE);

                txtdir1.setText(dire1);
                txtdir2.setText(dire2);
                direccionCompleta = dire1 + ", " + dire2 + ", " + dire3 + ", " + dire4;

                coordenadas = lat + "," + lon;


                txtTipoEntrega.setText("Delivery");
                Toast.makeText(PagoCarrito.this, "Seleccionó DELIVERY :" +txtTipoEntrega.getText().toString(), Toast.LENGTH_SHORT).show();

                prueba = true;
                isDelivery = true;
                txtcostoDelivery.setText("");
                txtcostoDelivery.setText("s/5.00");

                double total = totalparcial + preciodelivery;
                txttotal.setText("s/"+total+"");
            }
        });

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                laypre.setVisibility(View.GONE);
                btnhome.setBackgroundColor(getResources().getColor(R.color.principal));
                btnhome.setTextColor(Color.BLACK);

                btndelivery.setBackgroundColor(getResources().getColor(R.color.tercero));
                btndelivery.setTextColor(Color.WHITE);

                direccionCompleta = "Tienda";
                coordenadas = "-9.0653072,-78.5833476";

                txtTipoEntrega.setText("Programada");
                Toast.makeText(PagoCarrito.this, "Seleccionó RECOJO EN TIENDA :" +txtTipoEntrega.getText().toString(), Toast.LENGTH_SHORT).show();
                prueba = true;
                isDelivery = false;
                txtcostoDelivery.setText("");
                txtcostoDelivery.setText("s/0.00");

                txttotal.setText("s/"+totalparcial);
            }
        });

        selectMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PagoCarrito.this, "Método de pago", Toast.LENGTH_SHORT).show();
            }
        });

        applycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PagoCarrito.this, "Aplicar código", Toast.LENGTH_SHORT).show();

                /*if (!prueba){
                    laypre.setVisibility(View.VISIBLE);
                    prueba = true;
                }else{
                    laypre.setVisibility(View.GONE);
                    prueba = false;
                }*/
            }
        });

        btnChangeDirec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagoCarrito.this, UbicacionPedido.class);
                startActivity(intent);
            }
        });
    }

    ///SUBTOTAL MOSTRAR///
    private void mostrarMontoT(String url1){
        txtsubtotal.setText("");
        txttotal.setText("");
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int f= 0; f < response.length(); f++){
                            try {
                                JSONObject objeto = new JSONObject(response.get(f).toString());
                                txtsubtotal.setText("s/"+objeto.getString("montototal")+"");
                                txtsubtotalCap.append(objeto.getString("montototal"));
                                String subtotal = String.valueOf(txtsubtotal.getText());
                                txttotal.append("s/"+objeto.getString("montototal")+"");
                                double totaldf = Double.valueOf(txtsubtotalCap.getText().toString());
                                totalparcial = totaldf + totalparcial;
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
        request.add(requerimiento);
    }


    /// REGISTRAR PEDIDO FINAL ///
    public void finalizarPedido() {
        //SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        //String usuario = preferences.getString("usuario", "");
        String est = "pro";

        String url3 = "https://www.rgym.online/gymsystem/vmovil/aaa.php?nom_usu="+txtusuarioCompra.getText().toString()+
                "&tip_ped="+txtTipoEntrega.getText().toString()+
                "&dir_ped="+direccionCompleta+
                "&coor_ped="+coordenadas+
                "&est_ped="+est;

        url3 = url3.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url3, null, this, this);
        request.add(jsonObjectRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getApplicationContext(), "¡¡!!", Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());


    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Procedimiento go", Toast.LENGTH_SHORT).show();


    }

}