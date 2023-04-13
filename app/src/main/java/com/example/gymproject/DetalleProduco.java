package com.example.gymproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.gymproject.Database.OrderContract;
import com.example.gymproject.adapter.CarritoDeCompra.ListaCarrito;
import com.example.gymproject.producto.MenuPrincipalProductos;
import com.example.gymproject.producto.Productos;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

public class DetalleProduco extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Response.Listener<JSONObject>, Response.ErrorListener {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView mImage;
    private TextView mTitle, mRating, mPrice;

    public Uri mCurrentCarUri;
    boolean hasAllRequiredValues = false;

    TextView quantitynumber,quantitytotal,txtpruebausuario,txtpruebaidentificador,textocantidad;
    Button addtoCart;
    Button addtoCart1, shoptoCart;
    ImageView btnresProducto, btnAddProducto,btnDetalleOpen,btnDetalleClose;
    EditText edtCantidadProducto;
    LinearLayout contenedor_info;
    int quantity = 1;
    int quantity2= 1;
    int nrot = 0;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_products);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        request = Volley.newRequestQueue(getApplicationContext());

        textocantidad = findViewById(R.id.textocantidad);

        btnDetalleOpen = findViewById(R.id.btnDetalleOpen);
        btnDetalleClose = findViewById(R.id.btnDetalleClose);

        contenedor_info = findViewById(R.id.contenedor_info);

        txtpruebausuario = findViewById(R.id.txtpruebausuario);
        txtpruebaidentificador = findViewById(R.id.txtpruebaidentificador);

        mToolbar = findViewById(R.id.toolbar);

        mImage = findViewById(R.id.image_view);
        mPrice = findViewById(R.id.price);
        mRating = findViewById(R.id.stock);
        mTitle = findViewById(R.id.name);

        //detalle
        btnAddProducto = findViewById(R.id.btnAddProducto);
        btnresProducto = findViewById(R.id.btnresProducto);

        addtoCart1 = findViewById(R.id.addtoCart1);
        shoptoCart = findViewById(R.id.shoptoCart);
        //quantitynumber = findViewById(R.id.quantitynumber);
        quantitytotal = findViewById(R.id.quantitytotal);
        //addtoCart = findViewById(R.id.addtoCart);
        edtCantidadProducto= findViewById(R.id.edtCantidadProducto);

        // Configuracion action bar
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_volver_atras));


        mToolbar.setNavigationIcon(R.drawable.ic_volver_atras);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), Productos.class));
                onBackPressed();
                //DetalleProduco.this.overridePendingTransition(R.anim.down_in, R.anim.down_out);

            }
        });

        //cache
        Intent intent = getIntent();
        String identificador = intent.getStringExtra("id");
        String identificadorSub = intent.getStringExtra("idSub");
        String identificadorMar = intent.getStringExtra("idMar");
        double price = intent.getDoubleExtra("price",0);
        int cantStock = intent.getIntExtra("stk",0);
        String title = intent.getStringExtra("title");
        String image = intent.getStringExtra("image");

        if (intent !=null){

            mActionBar.setTitle(title);
            txtpruebaidentificador.setText(identificador);
            mTitle.setText(title);
            mRating.setText("Stock: "+cantStock);
            mPrice.setText(String.valueOf(price));
            Glide.with(DetalleProduco.this).load(image).into(mImage);

            quantitytotal.setText(String.valueOf("Total: S/"+price));
        }

        SharedPreferences preferences = getSharedPreferences("guardarDatos", Context.MODE_PRIVATE);
        String usuario1 = preferences.getString("usuario", "");
        txtpruebausuario.setText(usuario1);

        //int canti = Integer.valueOf(edtCantidadProducto.getText().toString());




        addtoCart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Integer.valueOf(edtCantidadProducto.getText().toString()) == 0){

                    Snackbar snackbar = Snackbar.make(v, "Por favor, ingrese la cantidad", Snackbar.LENGTH_LONG);
                    snackbar.setDuration(1800);
                    snackbar.show();
                }else if(Integer.valueOf(edtCantidadProducto.getText().toString()) > cantStock ) {
                    Snackbar snackbar = Snackbar.make(v, "Cantidad incorrecta", Snackbar.LENGTH_LONG);
                    snackbar.setDuration(1800);
                    snackbar.show();

                }else{
                    registrarProductoTemp();
                    recuperarNroCarrito();
                    guardarNroCarrito();
                    Snackbar snackbar = Snackbar.make(v, "Se agregó el producto al carrito: " + title.toString(), Snackbar.LENGTH_LONG);
                    snackbar.setDuration(2100);
                    snackbar.show();
                }


                //SaveCart();
            }
        });

        shoptoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.valueOf(edtCantidadProducto.getText().toString()) == 0){

                    Snackbar snackbar = Snackbar.make(v, "Por favor, ingrese la cantidad", Snackbar.LENGTH_LONG);
                    snackbar.setDuration(1800);
                    snackbar.show();

                }else if (Integer.valueOf(edtCantidadProducto.getText().toString()) > cantStock ){
                    Snackbar snackbar = Snackbar.make(v, "Cantidad incorrecta", Snackbar.LENGTH_LONG);
                    snackbar.setDuration(1800);
                    snackbar.show();
                }else{
                    registrarProductoTemp();
                    Snackbar snackbar = Snackbar.make(v, "Se agregó el producto al carrito: " + title.toString(), Snackbar.LENGTH_LONG);
                    snackbar.setDuration(2100);
                    snackbar.show();

                    recuperarNroCarrito();
                    guardarNroCarrito();

                    Intent intent = new Intent(DetalleProduco.this, ListaCarrito.class);
                    startActivity(intent);
                }
                //SaveCart();
            }
        });



        btnAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    double precioP = Double.valueOf(mPrice.getText().toString());
                    quantity++;
                quantity2++;
                    double precioTP = precioP * quantity;
                    String nuevoprecio = String.valueOf(precioTP);
                    quantitytotal.setText("Total: S/."+nuevoprecio);
                    displayQuantity();
            }
        });

        btnresProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double precioP = Double.valueOf(mPrice.getText().toString());


               if (quantity == 0){
                    //Toast.makeText(getApplicationContext(), "No puede ingresar una cantidad inferior a 0", Toast.LENGTH_SHORT).show();
                   addtoCart1.setEnabled(false);
                   shoptoCart.setEnabled(false);
                    Snackbar snackbar= Snackbar.make(v, "No puede ingresar una cantidad inferior a 0", Snackbar.LENGTH_LONG);
                    snackbar.setDuration(1500);
                    snackbar.show();
                }else{
                    quantity--;
                   quantity2--;
                    displayQuantity();
                    double precioTP = precioP * quantity;
                    String nuevoprecio = String.valueOf(precioTP);
                    quantitytotal.setText("Total: S/."+nuevoprecio);
                }

            }
        });


        btnDetalleOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDetalleOpen.setVisibility(View.GONE);
                btnDetalleClose.setVisibility(View.VISIBLE);
                contenedor_info.setVisibility(View.VISIBLE);
            }
        });

        btnDetalleClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDetalleClose.setVisibility(View.GONE);
                btnDetalleOpen.setVisibility(View.VISIBLE);
                contenedor_info.setVisibility(View.GONE);
            }
        });

    }



    /// REGISTRO DE PRODUCTO TEMP ///
    public void registrarProductoTemp() {
        /*String url = "https://raygymproject.000webhostapp.com/registrarCarritoTempo.php?id=" + txtpruebaidentificador.getText().toString()
                + "&cantidad_car=" + edtCantidadProducto.getText().toString()
                + "&precio_uni_car=" + mPrice.getText().toString()
                + "&usu_usuario=" + txtpruebausuario.getText().toString();*/

        String url = "https://www.rgym.online/gymsystem/vmovil/registrarCarritoTempo.php?id_prod=" + txtpruebaidentificador.getText().toString()
                + "&cantidad_car=" + edtCantidadProducto.getText().toString()
                + "&precio_uni_car=" + mPrice.getText().toString()
                + "&usu_usucli=" + txtpruebausuario.getText().toString();

        url = url.replace(" ", "%40");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());


    }

    @Override
    public void onResponse(JSONObject response) {

        //Toast.makeText(getApplicationContext(), "producto añadido", Toast.LENGTH_SHORT).show();


    }

    /////SQLite
    private boolean SaveCart() {

        String name = mTitle.getText().toString();
        String price = mPrice.getText().toString();
        String quantity = quantitynumber.getText().toString();

        ContentValues values = new ContentValues();
        values.put(OrderContract.OrderEntry.COLUMN_NAME, name);
        values.put(OrderContract.OrderEntry.COLUMN_PRICE, price);
        values.put(OrderContract.OrderEntry.COLUMN_QUANTITY, quantity);

        if (mCurrentCarUri == null){
            Uri newUri = getContentResolver().insert(OrderContract.OrderEntry.CONTENT_URI, values);
            if (newUri == null){
                Toast.makeText(this, "Failed to cart", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Succes to cart", Toast.LENGTH_SHORT).show();
            }
        }

        hasAllRequiredValues = true;
        return hasAllRequiredValues;


    }

    private void displayQuantity() {
        edtCantidadProducto.setText(String.valueOf(quantity));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {OrderContract.OrderEntry._ID,
                OrderContract.OrderEntry.COLUMN_NAME,
                OrderContract.OrderEntry.COLUMN_PRICE,
                OrderContract.OrderEntry.COLUMN_QUANTITY
        };

        return new CursorLoader(this, mCurrentCarUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null | cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()){
            int name = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME);
            int price = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE);
            int quantity = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_QUANTITY);

            String nameofproduct = cursor.getString(name);
            String priceofproduct = cursor.getString(price);
            String quantityofproduct = cursor.getString(quantity);

            mTitle.setText(nameofproduct);
            mPrice.setText(priceofproduct);
            quantitynumber.setText(quantityofproduct);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        mTitle.setText("");
        mPrice.setText("");
        quantitynumber.setText("");

    }

    ////SQLite

    @Override
    public void onBackPressed() {
        this.finish();

        /*Intent refresh = new Intent(this, MenuPrincipalProductos.class);
        startActivity(refresh);//Start the same Activity*/
        overridePendingTransition(R.anim.down_in, R.anim.down_out);


    }

    private void recuperarNroCarrito() {
        SharedPreferences preferences = getSharedPreferences("carrito_numero", Context.MODE_PRIVATE);
        int nam = preferences.getInt("number", 0);
        nrot = nam + quantity2;
        String a = String.valueOf(nrot);
        //textocantidad.setText(""+a);
    }

    private void guardarNroCarrito(){
        SharedPreferences preferences = getSharedPreferences("carrito_numero", Context.MODE_PRIVATE);
        //int nro = preferences.getInt("numero", );

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("number" , nrot);

        editor.commit();
        ///textocantidad.setText(""+quantity);
    }


}