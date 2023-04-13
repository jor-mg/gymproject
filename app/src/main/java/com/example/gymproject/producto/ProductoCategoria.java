package com.example.gymproject.producto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.gymproject.DetalleProduco;
import com.example.gymproject.R;
import com.example.gymproject.adapter.MenuPrincipalProductos.MarcaModelo;
import com.example.gymproject.adapter.MenuPrincipalProductos.MarcasAdapter;
import com.example.gymproject.adapter.ProductoAdapter2;
import com.example.gymproject.adapter.SubcategoriaAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductoCategoria extends AppCompatActivity implements SearchView.OnQueryTextListener  {
    Toolbar toolbar_produc;
    ActionBar mActionBar;
    SearchView svSearchProducto;
    RecyclerView subcategoria_recyclerView,productos_recyclerView;
    RecyclerView.LayoutManager manager;
    RequestQueue requestQueue;
    ProgressBar progressbarSub;
    ArrayList<SubcategoriaModelo> subcategoria;
    ArrayList<ProductoModelo> products;
    SubcategoriaAdapter subAdapter;
    ProductoAdapter2 adapterP;
    LinearLayout contenedor_alerta, contenedor_alerta_vacio;
    public static String seleccionSub = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_categoria);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar_produc = findViewById(R.id.toolbar_produc);
        setSupportActionBar(toolbar_produc);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_volver_atras));

        contenedor_alerta = findViewById(R.id.contenedor_alerta);
        contenedor_alerta_vacio = findViewById(R.id.contenedor_alerta_vacio);

        progressbarSub = findViewById(R.id.progressbarSub);
        subcategoria = new ArrayList<>();

        subcategoria_recyclerView = findViewById(R.id.subcategoria_recyclerView);
        subcategoria_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        Intent intent = getIntent();
        String identificador = intent.getStringExtra("id");
        String nombreSub = intent.getStringExtra("nombre");

        if (intent !=null){

            mActionBar.setTitle(nombreSub);
        }


        listarSubCategorias("https://rgym.online/gymsystem/vmovil/listarSubCategoria.php?id_cat="+identificador);



        products = new ArrayList<>();
        productos_recyclerView = findViewById(R.id.productos_recyclerView);
        manager = new GridLayoutManager(ProductoCategoria.this, 2);
        productos_recyclerView.setLayoutManager(manager);



        //listarProductoSubcategoria("https://rgym.online/gymsystem/vmovil/listarProductoSubcategoria.php?id_sub="+seleccionSub);

        svSearchProducto = findViewById(R.id.svSearchProducto);
        svSearchProducto.setOnQueryTextListener(this);
    }

    private void listarSubCategorias(String urlSubCate){
        progressbarSub.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSubCate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressbarSub.setVisibility(View.GONE);

                        SubcategoriaModelo subcategoriaModelo = null;
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                subcategoriaModelo = new SubcategoriaModelo();
                                JSONObject jsonObject = null;
                                jsonObject = array.getJSONObject(i);
                                subcategoriaModelo.setIdSub(jsonObject.optString("id_sub"));
                                subcategoriaModelo.setNomSub(jsonObject.optString("nom_sub"));
                                subcategoriaModelo.setIdCat(jsonObject.optString("id_cat"));



                                //subcategoria.add(new SubcategoriaModelo("99", "Todo", "999"));

                                subcategoria.add(subcategoriaModelo);

                                Toast.makeText(ProductoCategoria.this, "Correcto todo", Toast.LENGTH_SHORT).show();

                            }
                            contenedor_alerta.setVisibility(View.VISIBLE);
                            subAdapter = new SubcategoriaAdapter(ProductoCategoria.this,subcategoria);
                            subcategoria_recyclerView.setAdapter(subAdapter);


                        }catch (Exception e){

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressbarSub.setVisibility(View.GONE);
                Toast.makeText(ProductoCategoria.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(ProductoCategoria.this).add(stringRequest);
    }


    public void listarProductoSubcategoria(String urlProdSub){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlProdSub,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

                            int canProdSub = Integer.valueOf(products.size());
                            if (canProdSub == 0){
                                contenedor_alerta_vacio.setVisibility(View.VISIBLE);
                                Toast.makeText(ProductoCategoria.this, "No hay productos", Toast.LENGTH_SHORT).show();
                            }else{
                                contenedor_alerta_vacio.setVisibility(View.GONE);
                            }
                            contenedor_alerta.setVisibility(View.GONE);
                            adapterP = new ProductoAdapter2(ProductoCategoria.this,products);
                            productos_recyclerView.setAdapter(adapterP);


                        }catch (Exception e){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductoCategoria.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(ProductoCategoria.this).add(stringRequest);

    }

    public void obtenerActualizacion(){
        products.clear();
        //listarProductoSubcategoria("https://rgym.online/gymsystem/vmovil/listarProductoSubcategoria.php?id_sub="+seleccionSub);
    }

    //<--Buscar por nombre o marca
    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            adapterP.getFilter().filter(query);

        }catch (Exception e){
            Toast.makeText(ProductoCategoria.this, "Seleccione una categoría, por favor", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            adapterP.getFilter().filter(newText);
        }catch (Exception e){
            Toast.makeText(ProductoCategoria.this, "Seleccione una categoría, por favor", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
    //FIN->>
}