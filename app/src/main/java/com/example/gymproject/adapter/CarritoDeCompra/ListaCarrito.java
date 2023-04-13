package com.example.gymproject.adapter.CarritoDeCompra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.R;
import com.example.gymproject.adapter.CarritoAdapter;
import com.example.gymproject.producto.MenuPrincipalProductos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListaCarrito extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static int totalCant;
    private static double totalMont;
    private RecyclerView recyclerCarrito3;
    ArrayList<CarritoModelo> listaCarrito3;
    ArrayList<CarritoModelo> listaMembresia5;
    StringRequest stringRequest;
    String cantidadFinal;
    RequestQueue request;
    SwipeRefreshLayout swipeRefreshLayoutCarritoF;
    RequestQueue rq;
    TextView tvitemsc, txtcontador, textaased, abcd, txtMontoTotalItems;
    TextView aoa;
    Button btnactualizarMonto, btnprocesopagar, btnreturnmenu;
    CarritoModelo carritoModelo;
    int nro10;
    TextView cartBadgeTextView;
    Toolbar toolbarListaCarrito;
    LinearLayout contenedor_carrito, contenedor_carrito_vacio;
    RelativeLayout  contenedor_carrito_bottom;
    ProgressBar progressCarritoCompras;
    CarritoAdapter carritoAdapter;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_carrito);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        toolbarListaCarrito = findViewById(R.id.toolbarListaCarrito);
        setSupportActionBar(toolbarListaCarrito);

        contenedor_carrito = findViewById(R.id.contenedor_carrito);
        contenedor_carrito_vacio = findViewById(R.id.contenedor_carrito_vacio);
        contenedor_carrito_bottom = findViewById(R.id.contenedor_carrito_bottom);
        progressCarritoCompras = findViewById(R.id.progressCarritoCompras);

        request = Volley.newRequestQueue(getApplicationContext());
        abcd = findViewById(R.id.abcd);
        aoa = findViewById(R.id.aoa);
        recyclerCarrito3 = findViewById(R.id.reCarrito);
        recyclerCarrito3.setHasFixedSize(true);
        recyclerCarrito3.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        //recyclerPaquete.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listaCarrito3 = new ArrayList<>();
        listaMembresia5 = new ArrayList<>();
        swipeRefreshLayoutCarritoF = findViewById(R.id.swipeRefreshLayoutCarritoF);
        swipeRefreshLayoutCarritoF.setOnRefreshListener(this);



        btnactualizarMonto = findViewById(R.id.btnactualizarMonto);
        btnprocesopagar = findViewById(R.id.btnprocesopagar);
        btnreturnmenu = findViewById(R.id.btnreturnmenu);

        txtMontoTotalItems = findViewById(R.id.txtMontoTotalItems);
        textaased = findViewById(R.id.textaased);
        tvitemsc = findViewById(R.id.tvitemsc);
        txtcontador = findViewById(R.id.txtcontador);
        tvitemsc.setText("Total: S/. 00.00");

        TextView pruebitaa = findViewById(R.id.pruebitaa);

        /////////////////////////////
        mActivity = ListaCarrito.this;

        Intent intent = getIntent();
        String identif = intent.getStringExtra("eliminado");

        if (intent == null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            restartActivity(mActivity);
            overridePendingTransition(0, 0);
        }


        //////////////////////////////
        progressCarritoCompras.setVisibility(View.VISIBLE);
        contenedor_carrito.setVisibility(View.GONE);
        contenedor_carrito_bottom.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "");
        pruebitaa.setText(usuario);

        //metodoListarCarritoTemporal
        //mostrarLista2("https://raygymproject.000webhostapp.com/listarCarrito.php?usu_usuario=" + usuario);
        mostrarLista2("https://www.rgym.online/gymsystem/vmovil/listarCarrito.php?usu_usucli=" + usuario);
        //metodoMontoTotalCarritoTemporal
        //mostrarMontoT("https://raygymproject.000webhostapp.com/mostrarMontoTemporal.php?usu_usuario=" + usuario);
        mostrarMontoT("https://www.rgym.online/gymsystem/vmovil/mostrarMontoTemporal.php?usu_usucli=" + usuario);
        Toast.makeText(getApplicationContext(), "hola", Toast.LENGTH_SHORT).show();


        //init();


        btnprocesopagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaCarrito.this, PagoCarrito.class);
                startActivity(intent);
            }
        });


        btnactualizarMonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tvitemsc.setText("Total: S/. 00.00");
                //mostrarMontoT("https://raygymproject.000webhostapp.com/mostrarMontoTemporal.php?usu_usuario=" + usuario);
                mostrarMontoT("https://www.rgym.online/gymsystem/vmovil/mostrarMontoTemporal.php?usu_usuario=" + usuario);

            }
        });

        btnreturnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ListaCarrito.this, MenuPrincipalProductos.class);
                startActivity(intent1);
            }
        });
    }


    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }


    private void mostrarLista2(String url) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject CarritoModelo = array.getJSONObject(i);

                                listaCarrito3.add(new CarritoModelo(
                                        CarritoModelo.getString("idcar"),
                                        CarritoModelo.getString("nom_prod"),
                                        CarritoModelo.getDouble("precio_uni_car"),
                                        CarritoModelo.getInt("cantidad_car"),
                                        CarritoModelo.getString("img_prod")
                                ));
                            }
                            progressCarritoCompras.setVisibility(View.GONE);
                            cartBadgeTextView.setVisibility(View.VISIBLE);
                            contenedor_carrito.setVisibility(View.VISIBLE);
                            contenedor_carrito_bottom.setVisibility(View.VISIBLE);
                            CarritoAdapter carritoAdapter = new CarritoAdapter(ListaCarrito.this, listaCarrito3);
                            recyclerCarrito3.setAdapter(carritoAdapter);
                            Toast.makeText(getApplicationContext(), "hola2", Toast.LENGTH_SHORT).show();



                            txtcontador.setText("" + listaCarrito3.size());

                            nro10 = Integer.valueOf(txtcontador.getText().toString());
                            calcularCantidadItems();
                            calcularMontoTotal();
                            carritoAdapter.notifyDataSetChanged();
                            if (nro10 == 0) {
                                btnprocesopagar.setEnabled(false);
                                cartBadgeTextView.setVisibility(View.GONE);
                                contenedor_carrito_bottom.setVisibility(View.GONE);
                                Toast.makeText(ListaCarrito.this, "El carrito está vacio", Toast.LENGTH_LONG).show();
                            } else {
                                btnprocesopagar.setEnabled(true);
                            }

                            swipeRefreshLayoutCarritoF.setRefreshing(false);


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


    // ELIMINAR ITEM DE CARRITO ///
    private void eliminarItemCarrito(String url) {
        ///String url = "https://xprojectgymx.000webhostapp.com/crud/eliminarPago.php?id_pago=" + txtidpago3.getText().toString();
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("elimina")) {

                    Toast.makeText(getApplicationContext(), "Se ha eliminado con exito", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.trim().equalsIgnoreCase("noExiste")) {
                        Toast.makeText(getApplicationContext(), "No se encuentra el registro", Toast.LENGTH_SHORT).show();
                        Log.i("Respuesta", "" + response);
                    } else {
                        Toast.makeText(getApplicationContext(), "No se ha eliminado", Toast.LENGTH_SHORT).show();
                        Log.i("Respuesta", "" + response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se pudo conectar", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(stringRequest);
    }


    ///MOSTRAR TOTAL CARRITO EA
    private void mostrarMontoT(String url1) {

        tvitemsc.setText("");
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int f = 0; f < response.length(); f++) {
                            try {
                                JSONObject objeto = new JSONObject(response.get(f).toString());
                                tvitemsc.append(objeto.getString("montototal") + ".00");
                                String comparar = "null.00";
                                if (tvitemsc.getText().toString().equals(comparar)) {
                                    contenedor_carrito_vacio.setVisibility(View.VISIBLE);
                                    tvitemsc.setText("00.00");
                                }


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


    public void obtenerActualizacion() {
        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "");
        swipeRefreshLayoutCarritoF.setRefreshing(true);
        listaCarrito3.clear();
        //mostrarLista2("https://raygymproject.000webhostapp.com/listarCarrito.php?usu_usuario=" + usuario);
        //mostrarMontoT("https://raygymproject.000webhostapp.com/mostrarMontoTemporal.php?usu_usuario=" + usuario);

        mostrarLista2("https://www.rgym.online/gymsystem/vmovil/listarCarrito.php?usu_usucli=" + usuario);
        mostrarMontoT("https://www.rgym.online/gymsystem/vmovil/mostrarMontoTemporal.php?usu_usucli=" + usuario);
    }

    public void onRefresh() {
        obtenerActualizacion();

    }

    public void onBackPressed() {
        //this.finish();

        Intent refresh = new Intent(this, MenuPrincipalProductos.class);
        startActivity(refresh);//Start the same Activity
        overridePendingTransition(R.anim.down_in, R.anim.down_out);


    }

    public void calcularCantidadItems(){
        int i = 0;
        totalCant = 0;
        while(i < nro10){
            totalCant = totalCant + (Integer.valueOf(listaCarrito3.get(i).getCantidadProducto()));
            i++;
        }
        cartBadgeTextView.setText(""+totalCant);
    }

    public void calcularMontoTotal(){
        int i = 0;
        totalMont = 0;
        while(i < nro10){
            totalMont = totalMont + (Double.valueOf(listaCarrito3.get(i).getTotal()));

            i++;
        }
        BigDecimal bd = new BigDecimal(totalMont).setScale(2, RoundingMode.HALF_UP);
        double val2 = bd.doubleValue();
        txtMontoTotalItems.setText("s/"+val2);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_tres, menu);

        final MenuItem menuItem = menu.findItem(R.id.cartFragment2);
        View actionView = menuItem.getActionView();

        cartBadgeTextView = actionView.findViewById(R.id.cart_badge_text_view);
        cartBadgeTextView.setVisibility(View.GONE);
        if (nro10 == 0){
            cartBadgeTextView.setVisibility(View.GONE);
        }
        return true;
        }


    }