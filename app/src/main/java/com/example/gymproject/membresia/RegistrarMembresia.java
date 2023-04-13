package com.example.gymproject.membresia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.LoadingDialogActualizar;
import com.example.gymproject.MenuPrincipal;
import com.example.gymproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrarMembresia extends AppCompatActivity  implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText txtid2,txtdnimem, txtciclo2, txtfechai, txtfechaf, txtnomcompleto;
    Button btnagregarmem,btnlistado2,btnactualizarmen,btneliminarmem;
    Switch sw;
    ImageButton btnbuscarcli2,btnbuscaridm;
    TextView capturaclase, capturatipo, capturacliente;
    TextView a;
    ImageButton btnvalidaridmem, btnvalidardni2, btnvalidarnome, btnvalidarciclom, btnvalidarf1, btnvalidarf2,btncalendario;
    Spinner spinnerclase, spinnertipo;
    LinearLayout layoutIdMem,layoutRegistroDni2,layoutRegistroNombres2, layoutRegistroCiclo, layoutRegistroF1, layoutRegistroF2;
    ///CLASE///
    ArrayList<String> claseLista = new ArrayList<>();
    ArrayAdapter<String> claseAdapter;

    ///TIPO_MEMBRESIA///
    ArrayList<String> tipoLista = new ArrayList<>();
    ArrayAdapter<String> tipoAdapter;


    RequestQueue request;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_membresia);
        requestQueue = Volley.newRequestQueue(this);
        //COMBOBOX
        spinnerclase = (Spinner) findViewById(R.id.spinnerclase);
        spinnertipo = (Spinner) findViewById(R.id.spinnertipo);
        request = Volley.newRequestQueue(getApplicationContext());
        //CAJAS DE TEXTO
        txtid2 = (EditText) findViewById(R.id.txtid2);
        txtdnimem = (EditText) findViewById(R.id.txtdnimem);
        txtciclo2 = (EditText) findViewById(R.id.txtciclo2);
        txtfechai = (EditText) findViewById(R.id.txtfechai);
        txtfechaf = (EditText) findViewById(R.id.txtfechaf);
        //SWITCH
        sw = (Switch) findViewById(R.id.sw);
        //
        txtnomcompleto = (EditText) findViewById(R.id.txtnomcompleto);
        //
        layoutIdMem = (LinearLayout) findViewById(R.id.layoutIdMem);
        layoutRegistroDni2 = (LinearLayout) findViewById(R.id.layoutRegistroDni2);
        layoutRegistroNombres2 = (LinearLayout) findViewById(R.id.layoutRegistroNombres2);
        layoutRegistroCiclo = (LinearLayout) findViewById(R.id.layoutRegistroCiclo);
        layoutRegistroF1 = (LinearLayout) findViewById(R.id.layoutRegistroF1);
        layoutRegistroF2 = (LinearLayout) findViewById(R.id.layoutRegistroF2);
        //CHECKS
        btnvalidaridmem = (ImageButton) findViewById(R.id.btnvalidaridmem);
        btnvalidardni2 = (ImageButton) findViewById(R.id.btnvalidardni2);
        btnvalidarnome = (ImageButton) findViewById(R.id.btnvalidarnome);
        btnvalidarciclom = (ImageButton) findViewById(R.id.btnvalidarciclom);
        btnvalidarf1 = (ImageButton) findViewById(R.id.btnvalidarf1);
        btnvalidarf2 = (ImageButton) findViewById(R.id.btnvalidarf2);
        ///boton de calendario
        btncalendario=(ImageButton) findViewById(R.id.btncalendario);
        //BOTONES
        btnagregarmem = (Button) findViewById(R.id.btnagregarmem);
        btnlistado2 = (Button) findViewById(R.id.btnlistado2);
        btnactualizarmen = (Button) findViewById(R.id.btnactualizarmen);
        btneliminarmem = (Button) findViewById(R.id.btneliminarmem) ;
        //BOTONES DE BUSQUEDA
        btnbuscarcli2 = (ImageButton) findViewById(R.id.btnbuscarcli2);
        btnbuscaridm = (ImageButton) findViewById(R.id.btnbuscaridm);
        // textview que almacenan id //
        capturaclase = (TextView) findViewById(R.id.capturaclase);
        capturatipo = (TextView) findViewById(R.id.capturatipo);
        capturacliente = (TextView) findViewById(R.id.capturacliente);
        a = (TextView) findViewById(R.id.a);
        txtciclo2.setText("1");
        btnvalidarciclom.setVisibility(View.VISIBLE);
        btnvalidarf1.setVisibility(View.VISIBLE);
        validacionesMem();
        ///FECHA con BOTON calendario///
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        btncalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegistrarMembresia.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = year+"-"+month+"-"+day;
                        txtfechaf.setText(date);
                        //btnvalidarf2.setVisibility(View.VISIBLE);

                        //animacion calendario//
                        TranslateAnimation animation = new TranslateAnimation(0, -90, 0, 0);
                        animation.setDuration(1000);
                        animation.setFillAfter(false);
                        animation.setAnimationListener(new MyAnimationListener());
                        btncalendario.startAnimation(animation);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        ///FECHA EN CAJA DE TEXTO///
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        txtfechai.setText(fDate);

        ///INHABIITAR IMAGENBUTTON//
        btnvalidarf2.setEnabled(true);


        // activar caja de texto ciclo //
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw.isChecked()){
                    txtciclo2.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Ciclo habilitado", Toast.LENGTH_SHORT).show();
                }else{
                    txtciclo2.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Ciclo deshabilitado", Toast.LENGTH_SHORT).show();

                }
            }
        });

        /// boton eliminar membresia ///
        btneliminarmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarMembresia();
            }
        });



        /// boton actualizar membresia ///
        btnactualizarmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (btnvalidardni2.getVisibility() == View.VISIBLE && btnvalidarnome.getVisibility() == View.VISIBLE && btnvalidarciclom.getVisibility() == View.VISIBLE
                        && btnvalidarf1.getVisibility() == View.VISIBLE && btnvalidarf2.getVisibility() == View.VISIBLE)
                    actualizarMembresia();
                else{
                    validacionCajaIncompleta();
                    Toast.makeText(getApplicationContext(), "Complete correctamente los campos", Toast.LENGTH_SHORT).show();
                }*/
                actualizarMembresia();
            }
        });


         /// boton agregar membresia //
        btnagregarmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnvalidardni2.getVisibility() == View.VISIBLE && btnvalidarnome.getVisibility() == View.VISIBLE && btnvalidarciclom.getVisibility() == View.VISIBLE
                        && btnvalidarf1.getVisibility() == View.VISIBLE && btnvalidarf2.getVisibility() == View.VISIBLE)
                registroMembresia();
                else{
                    validacionCajaIncompleta();
                    Toast.makeText(getApplicationContext(), "Complete correctamente los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /// boton buscar cliente DNI|NOMBRE|APELLIDOS ///
        btnbuscarcli2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bordeEdi();

                buscarCliente2("https://xprojectgymx.000webhostapp.com/crud/busquedaCliente.php?dni=" + txtdnimem.getText() + "");
            }
        });

        btnlistado2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostrarMembresia=new Intent(getApplicationContext(), ListaMembresia.class);
                startActivity(mostrarMembresia);
            }
        });

        /// boton buscar membresia mediante ID ///
        btnbuscaridm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarMembresia("https://xprojectgymx.000webhostapp.com/crud/consultarMembresia.php?id_membresia="+ txtid2.getText() + "");
            }
        });


        comboClase();
        comboTipo();


    }



    //// CAPTURA NOMBRE DE LAS CLASES //////
    private void comboClase() {
        String url = "https://xprojectgymx.000webhostapp.com/crud/comboClase.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("clase");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombreclase = jsonObject.optString("nombre");

                        claseLista.add(nombreclase);

                        claseAdapter = new ArrayAdapter<>(RegistrarMembresia.this,
                                android.R.layout.simple_spinner_item, claseLista);
                        claseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerclase.setAdapter(claseAdapter);
                        spinnerclase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String mora = spinnerclase.getSelectedItem().toString();

                                idComboClase("https://xprojectgymx.000webhostapp.com/crud/comboClase2.php?nombre=" + mora + "");
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    /// CAPTURAR ID DE CLASE ///
    private void idComboClase(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        capturaclase.setText(jsonObject.getString("id_clase"));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion" + error, Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }





    /// CAPTURAR NOMBRE DEL TIPO DE MEMBMRESIA ///
    private void comboTipo() {
        String url = "https://xprojectgymx.000webhostapp.com/crud/comboTipo.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("tipom");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombretipo = jsonObject.optString("nombre");
                        tipoLista.add(nombretipo);

                        tipoAdapter = new ArrayAdapter<>(RegistrarMembresia.this,
                                android.R.layout.simple_spinner_item, tipoLista);
                        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnertipo.setAdapter(tipoAdapter);
                        spinnertipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String mora1 = spinnertipo.getSelectedItem().toString();

                                idComboTipo("https://xprojectgymx.000webhostapp.com/crud/comboTipo2.php?nombre=" + mora1 + "");
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    /// CAPTURA ID DEL TIPO DE MEMBRESIA ///
    private void idComboTipo(String URL) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        capturatipo.setText(jsonObject.getString("id_tipo"));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion" + error, Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }




      /// CAPTURAR DNI Y NOMBRE|APLLIDO DEL CLIENTE ///
    private void buscarCliente2(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String id = jsonObject.optString("id_cliente");
                        String name = jsonObject.optString("nombre");
                        String ape = jsonObject.optString("apellido");
                        txtnomcompleto.setText(name + " " + ape);
                        capturacliente.setText(id);

                        btnvalidardni2.setVisibility(View.VISIBLE);
                        btnvalidarnome.setVisibility(View.VISIBLE);
                        //txtnomcompleto.setText(jsonObject.getString("nombre"));


                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                layoutRegistroDni2.setBackgroundResource(R.drawable.background_input2);

                a.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "Error de conexion" + error, Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    /// BUSCAR MEMBRESIA POR ID ///
      public void buscarMembresia(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        txtdnimem.setText(jsonObject.getString("dni"));
                        txtnomcompleto.setText(jsonObject.getString("fullname"));
                        /*String nombrec =jsonObject.getString("nom_clase");
                        String [] vect = nombrec.split(" ");
                        spinnerclase.setAdapter(new ArrayAdapter<String>(RegistrarMembresia.this, android.R.layout.simple_spinner_dropdown_item, vect));*/

                       /* String nombret =jsonObject.getString("nom_tipo");
                        String [] vect1 = nombret.split(" ");
                        spinnertipo.setAdapter(new ArrayAdapter<String>(RegistrarMembresia.this, android.R.layout.simple_spinner_dropdown_item, vect1));*/

                        txtciclo2.setText(jsonObject.getString("ciclos"));
                        txtfechai.setText(jsonObject.getString("fecha1"));
                        txtfechaf.setText(jsonObject.getString("fecha2"));

                        btnvalidarnome.setVisibility(View.VISIBLE);
                        btnvalidarf2.setVisibility(View.VISIBLE);
                        //animacion calendario//
                        TranslateAnimation animation = new TranslateAnimation(0, -90, 0, 0);
                        animation.setDuration(1000);
                        animation.setFillAfter(false);
                        animation.setAnimationListener(new MyAnimationListener());
                        btncalendario.startAnimation(animation);

                    } catch (JSONException e) {
                        //Toast.makeText(getApplicationContext(),  e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //btnvalidar.setImageResource(R.drawable.ic_equis_error);

                //layoutMem.setBackgroundResource(R.drawable.background_input2);

                //Toast.makeText(getApplicationContext(), "Error de conexion" + error, Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    /// ACTUALIZACIÓN DE membresia ////
    private void actualizarMembresia() {
        Alertactualizar();
        String url = "https://xprojectgymx.000webhostapp.com/crud/actualizarMembresia.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(getApplicationContext(), "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No se actualizó", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta", "" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                String id_membresia = txtid2.getText().toString();
                String id_cliente = capturacliente.getText().toString();
                String id_clase = capturaclase.getText().toString();
                String id_tipo = capturatipo.getText().toString();
                String ciclos = txtciclo2.getText().toString();
                String f_inicio = txtfechai.getText().toString();
                String f_fin = txtfechaf.getText().toString();
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id_membresia", id_membresia);
                parametros.put("id_cliente", id_cliente);
                parametros.put("id_clase", id_clase);
                parametros.put("id_tipo", id_tipo);
                parametros.put("ciclos", ciclos);
                parametros.put("f_inicio", f_inicio);
                parametros.put("f_fin", f_fin);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    /// ELIMINAR membresia ///
    private void eliminarMembresia() {
        String url = "https://xprojectgymx.000webhostapp.com/crud/eliminarMembresia.php?id_membresia="+txtid2.getText().toString();
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("elimina")) {
                    txtid2.setText("");
                    txtdnimem.setText("");
                    txtnomcompleto.setText("");
                    txtciclo2.setText("");
                    txtfechaf.setText("");
                    txtfechai.setText("");

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




    /// REGISTRO DE membresia ///
    public void registroMembresia() {

        String url = "https://xprojectgymx.000webhostapp.com/crud/registrarMembresia.php?id_cliente=" + capturacliente.getText().toString()
                + "&id_clase=" + capturaclase.getText().toString()
                + "&id_tipo=" + capturatipo.getText().toString()
                + "&ciclos=" + txtciclo2.getText().toString()
                + "&f_inicio=" + txtfechai.getText().toString()
                + "&f_fin=" + txtfechaf.getText().toString();

        url = url.replace(" ", "%50");
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
        layoutRegistroF2.setBackgroundResource(R.drawable.background_input);
        Toast.makeText(getApplicationContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
        txtdnimem.setText("");
        capturaclase.setText("");
        capturatipo.setText("");
        txtciclo2.setText("");
        txtfechai.setText("");
        txtfechaf.setText("");

    }

    /// CAJA DE TEXTO CONFIRMAR !ERROR ///
    public void bordeEdi(){
        if (txtnomcompleto.toString().length() >= 1){
            layoutRegistroDni2.setBackgroundResource(R.drawable.background_input);
            a.setVisibility(View.GONE);

        }
    }

    /// alerta actualizando ///
    public void Alertactualizar() {
        LoadingDialogActualizar LoadingDialogActualizar = new LoadingDialogActualizar(RegistrarMembresia.this);

        LoadingDialogActualizar.startLoadingDialog();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                LoadingDialogActualizar.dismissDialog();
            }
        }, 1000);
    }

    public void onBackPressed(){
        finish();
        Intent intent = new Intent(RegistrarMembresia.this , MenuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu_registrar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_limpiar:
                txtdnimem.setText("");
                txtnomcompleto.setText("");
                capturaclase.setText("");
                capturatipo.setText("");
                //txtciclo2.setText("");
                //txtfechai.setText("");
                txtfechaf.setText("");

                btnvalidaridmem.setVisibility(View.GONE);
                btnvalidardni2.setVisibility(View.GONE);
                btnvalidarnome.setVisibility(View.GONE);
                //btnvalidarciclom.setVisibility(View.GONE);
                //btnvalidarf1.setVisibility(View.GONE);
                btnvalidarf2.setVisibility(View.GONE);
                layoutRegistroDni2.setBackgroundResource(R.drawable.background_input);
                layoutRegistroF2.setBackgroundResource(R.drawable.background_input);
                layoutRegistroNombres2.setBackgroundResource(R.drawable.background_input);
                layoutRegistroCiclo.setBackgroundResource(R.drawable.background_input);
                TranslateAnimation animation = new TranslateAnimation(0, 95, 0, 0);
                animation.setDuration(1000);
                animation.setFillAfter(false);
                animation.setAnimationListener(new MyAnimationListener2());
                btncalendario.startAnimation(animation);


                return true;
            case R.id.action_lista:
                Intent intent2 = new Intent(RegistrarMembresia.this, ListaMembresia.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void validacionesMem() {
        txtciclo2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtciclo2.getText().toString().isEmpty()){
                    btnvalidarciclom.setVisibility(View.GONE);
                }
                if (txtciclo2.getText().toString().equals("0")){
                    txtciclo2.setText("");
                }
                if (txtciclo2.getText().toString().length() >= 1){
                    btnvalidarciclom.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /// VALIDACIÓN NO ESPACIOS | MÍNIMO 8 NÚMEROS ///
        txtdnimem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtdnimem.getText().toString().length() < 8) {
                    btnvalidardni2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtdnimem.getText().toString().length() == 8) {
                    btnvalidardni2.setVisibility(View.VISIBLE);
                }
                if (Pattern.compile(" {1,}").matcher(txtdnimem.getText().toString()).find()) {
                    btnvalidardni2.setVisibility(View.GONE);

                }
            }
        });
    }

    /// VALIDAR QUE CAJA DE TEXTO FALTA COMPLETAR CORRECTAMENTE ///
    public void validacionCajaIncompleta() {
        /*if(btnvalidaridmem.getVisibility()==View.GONE){
            layoutIdMem.setBackgroundResource(R.drawable.background_input2);
        }else {
            if (btnvalidaridmem.getVisibility() == View.VISIBLE){
                layoutIdMem.setBackgroundResource(R.drawable.background_input);
            }
        }*/
        if(btnvalidardni2.getVisibility()==View.GONE){
            layoutRegistroDni2.setBackgroundResource(R.drawable.background_input2);
        }else{
            layoutRegistroDni2.setBackgroundResource(R.drawable.background_input);
        }
        if(btnvalidarnome.getVisibility()==View.GONE){
            layoutRegistroNombres2.setBackgroundResource(R.drawable.background_input2);
        }else{
            layoutRegistroNombres2.setBackgroundResource(R.drawable.background_input);
        }
        if(btnvalidarciclom.getVisibility()==View.GONE){
            layoutRegistroCiclo.setBackgroundResource(R.drawable.background_input2);
        }else{
            layoutRegistroCiclo.setBackgroundResource(R.drawable.background_input);
        }
        if(btnvalidarf1.getVisibility()==View.GONE){
            layoutRegistroF1.setBackgroundResource(R.drawable.background_input2);
        }else{
            layoutRegistroF1.setBackgroundResource(R.drawable.background_input);
        }
        if(btnvalidarf2.getVisibility()==View.GONE){
            layoutRegistroF2.setBackgroundResource(R.drawable.background_input2);
        }else{
            if(btnvalidarf2.getVisibility()==View.VISIBLE) {
                layoutRegistroF2.setBackgroundResource(R.drawable.background_input);
            }
        }
    }

    //CALENDARIO APARECE CHECK
    private class MyAnimationListener implements Animation.AnimationListener {
        @Override public void onAnimationEnd(Animation animation) {
            btncalendario.clearAnimation();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(btncalendario.getWidth(), btncalendario.getHeight());
            lp.setMargins(-285, 5, 0,0 );
            btnvalidarf2.setVisibility(View.VISIBLE);
            btncalendario.setLayoutParams(lp);

        }

        @Override public void onAnimationRepeat(Animation animation) {

        }

        @Override public void onAnimationStart(Animation animation) { }
    }

    //CALENDARIO OCULTA CHECK
    private class MyAnimationListener2 implements Animation.AnimationListener {
        @Override public void onAnimationEnd(Animation animation) {
            btncalendario.clearAnimation();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(btncalendario.getWidth(), btncalendario.getHeight());
            lp.setMargins(-185, 5, 0,0 );
            btnvalidarf2.setVisibility(View.GONE);
            btncalendario.setLayoutParams(lp);

        }

        @Override public void onAnimationRepeat(Animation animation) {

        }

        @Override public void onAnimationStart(Animation animation) { }
    }

}