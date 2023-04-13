package com.example.gymproject.producto;

import static com.example.gymproject.adapter.CarritoDeCompra.ListaCarrito.totalCant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.R;
import com.example.gymproject.adapter.CarritoAdapter;
import com.example.gymproject.adapter.CarritoDeCompra.CarritoModelo;
import com.example.gymproject.adapter.CarritoDeCompra.ListaCarrito;
import com.example.gymproject.adapter.ProductoAdapter2;

import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Productos extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    ArrayList<ProductoModelo> products;
    ArrayList<CarritoModelo> listaCarrito3;
    private ProgressBar progressBar;
    private RecyclerView.Adapter mAdapter;
    SearchView svSearch;
    ArrayList<ProductoModelo> listaProductos;
    ProductoAdapter2 adapterP;
    TextView cartBadgeTextView;
    int nro10;
    SearchView searchView;
    //private static final String BASE_URL = "https://raygymproject.000webhostapp.com/listarProductos.php";

    private static final String BASE_URL = "https://www.rgym.online/gymsystem/vmovil/listarProducto.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar = findViewById(R.id.dashboard_toolbar);
        progressBar = findViewById(R.id.progressbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        recyclerView = findViewById(R.id.products_recyclerView);
        manager = new GridLayoutManager(Productos.this, 2);
        recyclerView.setLayoutManager(manager);
        products = new ArrayList<>();
        listaCarrito3 = new ArrayList<>();

        svSearch = findViewById(R.id.svSearch);
        svSearch.setOnQueryTextListener(this);
        getProducts();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuPrincipalProductos.class));
                Productos.this.overridePendingTransition(R.anim.down_in, R.anim.down_out);

            }
        });

        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "");
        //mostrarLista2("https://www.rgym.online/gymsystem/vmovil/listarCarrito.php?usu_usucli=" + usuario);
        mostrarLista2("https://www.rgym.online/gymsystem/vmovil/listarCarrito.php?usu_usucli=70202379");
    }

    private void getProducts (){
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        ProductoModelo productoModelo = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                productoModelo = new ProductoModelo();
                                JSONObject jsonObject = null;
                                jsonObject = array.getJSONObject(i);
                                productoModelo.setIdP(jsonObject.optString("id_prod"));
                                productoModelo.setIdSub(jsonObject.optString("id_sub"));
                                productoModelo.setIdMar(jsonObject.optString("id_mar"));
                                productoModelo.setTitle(jsonObject.optString("nom_prod"));
                                productoModelo.setPrice(jsonObject.optDouble("pre_prod"));
                                productoModelo.setStock(jsonObject.optInt("stk_prod"));
                                productoModelo.setImage(jsonObject.optString("img_prod"));

                                products.add(productoModelo);

                            }
                            adapterP = new ProductoAdapter2(Productos.this,products);
                            recyclerView.setAdapter(adapterP);


                        }catch (Exception e){

                        }

                        //mAdapter = new ProductoAdapter(Productos.this,products);
                       // adapterP = new ProductoAdapter(Productos.this,products);
                        //recyclerView.setAdapter(adapterP);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(Productos.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(Productos.this).add(stringRequest);

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
                                        CarritoModelo.getInt("precio_uni_car"),
                                        CarritoModelo.getInt("cantidad_car"),
                                        CarritoModelo.getString("img_prod")
                                ));
                            }
                            cartBadgeTextView.setVisibility(View.VISIBLE);
                            CarritoAdapter carritoAdapter = new CarritoAdapter(Productos.this, listaCarrito3);
                            //Toast.makeText(getApplicationContext(), "SI HAY PRODUCTOS", Toast.LENGTH_SHORT).show();

                            nro10 = Integer.valueOf(listaCarrito3.size());
                            calcularCantidadItemss();
                            carritoAdapter.notifyDataSetChanged();
                            if (nro10 == 0) {
                                cartBadgeTextView.setVisibility(View.GONE);
                            } else {
                                //btnprocesopagar.setEnabled(true);
                            }

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

        if (item.getItemId() == R.id.cartFragment2){
            //this.overridePendingTransition(R.anim.above_in, R.anim.above_out);
            //Toast.makeText(Productos.this,"RESUMEN",Toast.LENGTH_SHORT).show();
        }



        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu_tres, menu);

        final MenuItem menuItem = menu.findItem(R.id.cartFragment2);
        View actionView = menuItem.getActionView();

        cartBadgeTextView = actionView.findViewById(R.id.cart_badge_text_view);
        cartBadgeTextView.setVisibility(View.GONE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Productos.this, ListaCarrito.class);
                startActivity(intent);
            }
        });
        //MenuItem searchItem = menu.findItem(R.id.action_searchh);
        //SearchView searchView = (SearchView) searchItem.getActionView();

        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //searchView.setQueryHint("Nombre producto");



        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterP.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterP.getFilter().filter(newText);
                return false;
            }
        });*/
        return true;
    }


    public void calcularCantidadItemss(){
        int i = 0;
        totalCant = 0;
        while(i < nro10){
            totalCant = totalCant + (Integer.valueOf(listaCarrito3.get(i).getCantidadProducto()));
            i++;
        }
        cartBadgeTextView.setText(""+totalCant);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
        Intent intent = new Intent(Productos.this, MenuPrincipalProductos.class);
        startActivity(intent);
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        adapterP.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterP.getFilter().filter(newText);
        return false;
    }

    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}