package com.example.gymproject.producto;

import static com.example.gymproject.adapter.CarritoDeCompra.ListaCarrito.totalCant;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.ListaUbicacion;
import com.example.gymproject.MenuPrincipal;
import com.example.gymproject.R;
import com.example.gymproject.UbicacionPedido;
import com.example.gymproject.adapter.CarritoAdapter;
import com.example.gymproject.adapter.CarritoDeCompra.CarritoModelo;
import com.example.gymproject.adapter.CarritoDeCompra.ListaCarrito;
import com.example.gymproject.adapter.CategoriaAdapter;
import com.example.gymproject.adapter.MenuPrincipalProductos.MarcaModelo;
import com.example.gymproject.adapter.MenuPrincipalProductos.MarcasAdapter;
import com.example.gymproject.adapter.MenuPrincipalProductos.PromocionesAdapter;
import com.example.gymproject.adapter.PedidoFinal.ListaPedido;
import com.example.gymproject.adapter.PedidoFinal.PedidoHistorialModelo;
import com.example.gymproject.adapter.PedidoHistorialAdapter;
import com.example.gymproject.adapter.PedidoProgressAdapterMenu;
import com.example.gymproject.adapter.ProductoAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class MenuPrincipalProductos extends AppCompatActivity {

    RecyclerView reProteinasT,reMarcas, reCategorias, rePedProgreso;
    CategoriaAdapter categoriaAdapter;
    ProductoAdapter productoAdapter;
    MarcasAdapter marcasAdapter;
    PedidoProgressAdapterMenu pedidoAdapter;
    ArrayList<CategoriaModelo> categoriaList;
    ArrayList<ProductoModelo> productsProte;
    ArrayList<MarcaModelo> marcasList;
    ArrayList<PedidoHistorialModelo> pedidosProgreso;
    int nro10;
    ViewPager viewPager;
    ArrayList<Integer> images = new ArrayList<>();
    PromocionesAdapter promocionesAdapter;
    TextView cartBadgeTextView;
    ProgressBar progressMenuProducts;
    ArrayList<CarritoModelo> listaCarrito3;
    Toolbar mToolbar;
    LinearLayout txttituloprogreso;
    ShimmerFrameLayout shimmer_productos, shimmer_categorias;
    SharedPreferences preferences;
    String validacionUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_productos);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mToolbar = findViewById(R.id.toolbarMenuPrincipalProdcutos);
        setSupportActionBar(mToolbar);
        progressMenuProducts = findViewById(R.id.progressMenuProducts);

        viewPager = findViewById(R.id.viewPager);


        images.add(R.drawable.promouno);
        images.add(R.drawable.promodos);
        images.add(R.drawable.promotres);

        txttituloprogreso = findViewById(R.id.txttituloprogreso);
        shimmer_productos = findViewById(R.id.shimmer_productos);
        shimmer_categorias = findViewById(R.id.shimmer_categorias);
        shimmer_productos.startShimmer();
        shimmer_categorias.startShimmer();


        promocionesAdapter = new PromocionesAdapter(this, images);
        viewPager.setPadding(0,0,0,0);
        viewPager.setAdapter(promocionesAdapter);
        ////////////////////////////////////////////////////////////////POR CATEGORIAS
        reCategorias = findViewById(R.id.reCategorias);
        reCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriaList = new ArrayList<>();
        ////////////////////////////////////////////////////////////////POR MARCAS
        reMarcas = findViewById(R.id.reMarcas);
        reMarcas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        marcasList = new ArrayList<>();
        ////////////////////////////////////////////////////////////////POR PRODUCTOS
        reProteinasT = findViewById(R.id.reProteinasT);
        //reProteinasT.setHasFixedSize(true);
        reProteinasT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //manager = new GridLayoutManager(MenuPrincipalProductos.this, 2);
        //reProteinasT.setLayoutManager(manager);
        productsProte = new ArrayList<>();
        CarritoAdapter carritoAdapter = new CarritoAdapter(MenuPrincipalProductos.this, listaCarrito3);
        listaCarrito3 = new ArrayList<>();
        ////////////////////////////////////////////////////////////////PEDIDOS EN PROGRESO
        pedidosProgreso = new ArrayList<>();
        rePedProgreso = findViewById(R.id.rePedProgreso);
        rePedProgreso.setHasFixedSize(true);
        rePedProgreso.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));


        ProductosProteinaAll();
        CategoriasAll();

        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "");
        String idCliente = preferences.getString("idcliente", "");
        mostrarLista2("https://www.rgym.online/gymsystem/vmovil/listarCarrito.php?usu_usucli=" + usuario);
        MarcasAll();
        pedidoPendiente("https://www.rgym.online/gymsystem/vmovil/listarProgresoPedido.php?id_cli="+idCliente);

    }

    private void CategoriasAll(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://rgym.online/gymsystem/vmovil/listarCategorias.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CategoriaModelo categoriaModelo = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                categoriaModelo = new CategoriaModelo();
                                JSONObject jsonObject = null;
                                jsonObject = array.getJSONObject(i);

                                categoriaModelo.setIdCategoria(jsonObject.optString("id_cat"));
                                categoriaModelo.setNombreCategoria(jsonObject.optString("nom_cat"));

                                categoriaList.add(categoriaModelo);

                            }
                            shimmer_categorias.stopShimmer();
                            shimmer_categorias.setVisibility(View.GONE);
                            reCategorias.setVisibility(View.VISIBLE);
                            categoriaAdapter = new CategoriaAdapter(MenuPrincipalProductos.this,categoriaList);
                            reCategorias.setAdapter(categoriaAdapter);

                        }catch (Exception e){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(MenuPrincipalProductos.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(MenuPrincipalProductos.this).add(stringRequest);
    }

    private void ProductosProteinaAll(){
        progressMenuProducts.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.rgym.online/gymsystem/vmovil/listarProducto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressMenuProducts.setVisibility(View.GONE);
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

                                productsProte.add(productoModelo);

                            }
                            shimmer_productos.stopShimmer();
                            shimmer_productos.setVisibility(View.GONE);
                            reProteinasT.setVisibility(View.VISIBLE);
                            productoAdapter = new ProductoAdapter(MenuPrincipalProductos.this,productsProte);
                            reProteinasT.setAdapter(productoAdapter);


                        }catch (Exception e){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(MenuPrincipalProductos.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(MenuPrincipalProductos.this).add(stringRequest);

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
                            CarritoAdapter carritoAdapter = new CarritoAdapter(MenuPrincipalProductos.this, listaCarrito3);
                            //Toast.makeText(getApplicationContext(), "hola2", Toast.LENGTH_SHORT).show();

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

    private void MarcasAll(){
        marcasList.add(new MarcaModelo("1","Metrix",R.drawable.metrix));
        marcasList.add(new MarcaModelo("2","Muscletech",R.drawable.muscletech));
        marcasList.add(new MarcaModelo("3","One",R.drawable.one));
        marcasList.add(new MarcaModelo("4","Organic",R.drawable.organic));
        marcasList.add(new MarcaModelo("5","PureFit",R.drawable.purefit));

        marcasAdapter = new MarcasAdapter(MenuPrincipalProductos.this,marcasList);
        reMarcas.setAdapter(marcasAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_dos, menu);

        final MenuItem menuItem = menu.findItem(R.id.cartFragment);
        View actionView = menuItem.getActionView();

        cartBadgeTextView = actionView.findViewById(R.id.cart_badge_text_view);

        //cartBadgeTextView.setText(""+totalCant);
        cartBadgeTextView.setVisibility(View.GONE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MenuPrincipalProductos.this, ListaCarrito.class);
                startActivity(intent);
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        if (item.getItemId() == R.id.pedidosItems){
            Intent intent2 = new Intent(MenuPrincipalProductos.this, ListaPedido.class);
            startActivity(intent2);
            //this.overridePendingTransition(R.anim.above_in, R.anim.above_out);
        }
        if (item.getItemId() == R.id.direccionItems){
            //<--Recuperar datos SharedPreferencesUbicacion
            preferences = getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
            validacionUbicacion = preferences.getString("direccion", "");
            if (validacionUbicacion.isEmpty()){
                Toast.makeText(MenuPrincipalProductos.this, "No se encontró ubicación", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MenuPrincipalProductos.this, UbicacionPedido.class);
                startActivity(intent2);
            }else{
                Intent intent2 = new Intent(MenuPrincipalProductos.this, ListaUbicacion.class);
                startActivity(intent2);
            }
            //-->




            //this.overridePendingTransition(R.anim.above_in, R.anim.above_out);
        }
        if (item.getItemId() == R.id.searchProduct){
            Intent intent = new Intent(MenuPrincipalProductos.this, Productos.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.above_in, R.anim.above_out);
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        }



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

    private void pedidoPendiente(String urlPp){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        PedidoHistorialModelo pedidoHistorialModelo = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                pedidoHistorialModelo = new PedidoHistorialModelo();
                                JSONObject jsonObject = null;
                                jsonObject = array.getJSONObject(i);

                                pedidoHistorialModelo.setEstado(jsonObject.optString("est_ped"));
                                pedidoHistorialModelo.setNropedido(jsonObject.optString("id_ped"));
                                pedidoHistorialModelo.setTotal(jsonObject.optString("tot_ped"));
                                pedidoHistorialModelo.setFecha(jsonObject.optString("fec_ped"));
                                pedidoHistorialModelo.setTipo(jsonObject.optString("tip_ped"));
                                pedidoHistorialModelo.setDir(jsonObject.optString("dir_ped"));

                                pedidosProgreso.add(pedidoHistorialModelo);

                            }
                            pedidoAdapter = new PedidoProgressAdapterMenu(MenuPrincipalProductos.this,pedidosProgreso);
                            rePedProgreso.setAdapter(pedidoAdapter);

                            int cantidadProgreso = Integer.valueOf(pedidosProgreso.size());
                            if (cantidadProgreso == 0){
                                txttituloprogreso.setVisibility(View.GONE);
                                rePedProgreso.setVisibility(View.GONE);
                            }
                        }catch (Exception e){

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(MenuPrincipalProductos.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(MenuPrincipalProductos.this).add(stringRequest);
    }

    public void onBackPressed() {
        //this.finish();

        Intent refresh = new Intent(this, MenuPrincipal.class);
        //Start the same Activity
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
        overridePendingTransition(0, 0);

        overridePendingTransition(0, 0);
        finish();
        startActivity(refresh);

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}