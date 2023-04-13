package com.example.gymproject.membresia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.MenuPrincipal;
import com.example.gymproject.R;
import com.example.gymproject.adapter.MembresiaAdapter;
import com.example.gymproject.helper.MyButtonClickListener;
import com.example.gymproject.helper.MySwipeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaMembresia extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> , SearchView.OnQueryTextListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerMembresias;
    MembresiaAdapter adapter;
    ArrayList<MembresiaModelo> listaMembresia2;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    TextView valor;
    TextView txtcontadormem;
    SharedPreferences sp;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_membresia);
        listaMembresia2 = new ArrayList<>();
        recyclerMembresias = (RecyclerView) findViewById(R.id.reMembesia);
        recyclerMembresias.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerMembresias.setHasFixedSize(true);
        request = Volley.newRequestQueue(getApplicationContext());
        sp = getSharedPreferences("Registros", 0);

        valor = (TextView) findViewById(R.id.valor);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        txtcontadormem = (TextView) findViewById(R.id.txtcontadormem);

        swipeRefreshLayout.setOnRefreshListener(this);

        MySwipeHelper SwipeHelper = new MySwipeHelper(this, recyclerMembresias, 200)
        {

            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(ListaMembresia.this,
                        "Eliminar",
                        30,
                        0,
                        Color.parseColor("#60b5f7"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                String valuem = listaMembresia2.get(pos).getId2();
                                eliminarMembresia("https://xprojectgymx.000webhostapp.com/crud/eliminarMembresia.php?id_membresia="+valuem, valuem); //
                                listaMembresia2.clear();
                                cargarLista();

                            }
                        }));
                buffer.add(new MyButton(ListaMembresia.this,
                        "Actualizar",
                        30,
                        0,
                        Color.parseColor("#ffc59c"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(ListaMembresia.this, "Actualizar click", Toast.LENGTH_SHORT).show();

                            }
                        }));
            }
        };



       cargarLista();





    }

    public void obtenerActualizacion(){
        swipeRefreshLayout.setRefreshing(true);
        listaMembresia2.clear();
        cargarLista();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu_listas, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Ingrese CÓDIGO/NOMBRE");
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
                Intent intent1 = new Intent(ListaMembresia.this, MenuPrincipal.class);
                startActivity(intent1);
                return true;
            case R.id.action_add:
                Intent intent2 = new Intent(ListaMembresia.this, RegistrarMembresia.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void cargarLista() {
        String url = "https://xprojectgymx.000webhostapp.com/crud/consultarMembresia2.php";
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
        MembresiaModelo membresiaModelo = null;

        JSONArray json = response.optJSONArray("membresia");
        try {
            for (int i=0; i < json.length(); i++){
                membresiaModelo=new MembresiaModelo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                membresiaModelo.setId2(jsonObject.optString("id_membresia"));
                membresiaModelo.setCliente2(jsonObject.optString("fullname"));
                membresiaModelo.setClase2(jsonObject.optString("nom_clase"));
                membresiaModelo.setTipo2(jsonObject.optString("nom_tipo"));
                membresiaModelo.setMeses2(jsonObject.optString("ciclos"));
                membresiaModelo.setInicio2(jsonObject.optString("f_inicio"));
                membresiaModelo.setFin2(jsonObject.optString("f_fin"));
                listaMembresia2.add(membresiaModelo);
                txtcontadormem.setText("Total de membresias : " +listaMembresia2.size());

            }
            adapter = new MembresiaAdapter(ListaMembresia.this,listaMembresia2);
            recyclerMembresias.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);

        }catch (JSONException e){
            swipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error de conexion al servidor"+response, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }



    public void onRefresh(){
        obtenerActualizacion();
    }

    public void onBackPressed(){
        finish();
        Intent intent = new Intent(ListaMembresia.this , MenuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    /// ELIMINAR membresia ///
    private void eliminarMembresia(String url, String valuem) {
        ///String url = "https://xprojectgymx.000webhostapp.com/crud/eliminarMembresia.php?id_membresia="+txtid2.getText().toString();
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
                        prohibidoM(valuem);
                        //Toast.makeText(getApplicationContext(), "No se ha eliminado", Toast.LENGTH_SHORT).show();
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


    /// ALERTA DE OK|CANCEL ///
    public void prohibidoM(String valuem){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListaMembresia.this);
        builder.setTitle("¡AVISO!");
        builder.setMessage("La membresia ("+valuem+")"+" tiene un pago registrado, verifique.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}