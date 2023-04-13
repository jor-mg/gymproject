package com.example.gymproject.pago;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.MenuPrincipal;
import com.example.gymproject.R;
import com.example.gymproject.adapter.PagoAdapter;
import com.example.gymproject.helper.MyButtonClickListener;
import com.example.gymproject.helper.MySwipeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaPago extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerPago;
    PagoAdapter adapter;
    ArrayList<PagoModelo> listaPago2;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    TextView txtcontadorpago;
    SwipeRefreshLayout swipeRefreshLayoutPago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pago);
        listaPago2 = new ArrayList<>();
        recyclerPago = (RecyclerView) findViewById(R.id.rePago);
        recyclerPago.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerPago.setHasFixedSize(true);
        request = Volley.newRequestQueue(getApplicationContext());
        txtcontadorpago = (TextView) findViewById(R.id.txtcontadorpago);
        swipeRefreshLayoutPago = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutPago);

        swipeRefreshLayoutPago.setOnRefreshListener(this);

        MySwipeHelper SwipeHelper = new MySwipeHelper(this, recyclerPago, 200)
        {

            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(ListaPago.this,
                        "Eliminar",
                        30,
                        0,
                        Color.parseColor("#60b5f7"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                String valuep = listaPago2.get(pos).getIdpa();
                                Toast.makeText(ListaPago.this, "Eliminar  "+valuep, Toast.LENGTH_SHORT).show(); //
                                eliminarPago("https://xprojectgymx.000webhostapp.com/crud/eliminarPago.php?id_pago="+valuep); //
                                listaPago2.clear();
                                cargarLista();

                            }
                        }));
                buffer.add(new MyButton(ListaPago.this,
                        "Actualizar",
                        30,
                        0,
                        Color.parseColor("#ffc59c"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(ListaPago.this, "Actualizar click", Toast.LENGTH_SHORT).show();

                            }
                        }));
            }
        };


        cargarLista();

    }

    public void obtenerActualizacion2(){
        swipeRefreshLayoutPago.setRefreshing(true);
        listaPago2.clear();
        cargarLista();

    }

    private void cargarLista() {
        String url = "https://raygymproject.000webhostapp.com/consultarPago3.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, this, this);
        request.add(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se pudo conectar", Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        PagoModelo pagoModelo = null;
        JSONArray json = response.optJSONArray("pago");
        try {
            for (int i=0; i < json.length(); i++){
                pagoModelo=new PagoModelo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                pagoModelo.setIdpa(jsonObject.optString("id_pago"));
                pagoModelo.setIdmem(jsonObject.optString("id_membresia"));
                pagoModelo.setNombrecli(jsonObject.optString("fullname"));
                pagoModelo.setMonto(jsonObject.optString("monto"));

                pagoModelo.setDnip(jsonObject.optString("dni"));
                pagoModelo.setTipop(jsonObject.optString("nom_tipo"));
                pagoModelo.setClasep(jsonObject.optString("nom_clase"));
                pagoModelo.setCiclosp(jsonObject.optString("ciclos"));


                listaPago2.add(pagoModelo);
                txtcontadorpago.setText("Total de pagos : " +listaPago2.size());
            }
            adapter = new PagoAdapter(listaPago2);
            recyclerPago.setAdapter(adapter);
            swipeRefreshLayoutPago.setRefreshing(false);
        }catch (JSONException e){
            e.printStackTrace();
            swipeRefreshLayoutPago.setRefreshing(false);
            Toast.makeText(getApplicationContext(), "Error de conexion al servidor"+response, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {

    }

    public void onRefresh(){
        obtenerActualizacion2();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu_listas, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Ingrese DNI/NOMBRE");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent1 = new Intent(ListaPago.this, MenuPrincipal.class);
                startActivity(intent1);
                return true;
            case R.id.action_add:
                Intent intent2 = new Intent(ListaPago.this, RegistrarPago.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void onBackPressed(){
        finish();
        Intent intent = new Intent(ListaPago.this , MenuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }


    // ELIMINAR PAGO ///
    private void eliminarPago(String url) {
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



}