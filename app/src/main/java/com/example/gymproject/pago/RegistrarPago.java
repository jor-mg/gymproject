package com.example.gymproject.pago;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.LoadingDialogMembresia;
import com.example.gymproject.MenuPrincipal;
import com.example.gymproject.R;
import com.example.gymproject.membresia.ListaMembresia;
import com.example.gymproject.membresia.MembresiaModelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegistrarPago extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private MembresiaModelo membresiaModelo;
    EditText txtidpago3, txtmembresia3, txtnomcompleto3, txtciclo3,txttipo3, txtclase3, txtmonto13;
    TextView capturamonto,capturaciclo,correorec;
    ImageButton btnbuscarmem3,btnvalidar,btnbuscarpag;
    Button btnactualizarpa,btnagregapa,btnlistado3,btneliminarpa;
    LinearLayout layoutMem;
    RequestQueue request;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;


    Session session;
    ProgressDialog pdialog = null;
    Context context = null;
    String rec, subject, textMessage;
    String email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pago);
        request = Volley.newRequestQueue(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);
        txtidpago3 = (EditText) findViewById(R.id.txtidpago3);
        txtmembresia3 = (EditText) findViewById(R.id.txtmembresia3);
        txtnomcompleto3 = (EditText) findViewById(R.id.txtnomcompleto3);
        txtciclo3 = (EditText) findViewById(R.id.txtciclo3);
        txttipo3 = (EditText) findViewById(R.id.txttipo3);
        txtclase3 = (EditText) findViewById(R.id.txtclase3);
        txtmonto13 = (EditText) findViewById(R.id.txtmonto13);

        capturamonto = (TextView) findViewById(R.id.capturamonto);
        capturaciclo = (TextView) findViewById(R.id.capturaciclo);
        correorec = (TextView) findViewById(R.id.correorec);

        layoutMem = (LinearLayout) findViewById(R.id.layoutMem);

        email = "kokem2xd.2000@gmail.com";
        pass = "Jorge159753.jmg";



        ///recibe ID de membresia///
        Intent intent = getIntent();
        String idmembresia = intent.getStringExtra("id2");
        txtmembresia3.setText(idmembresia);


        btnbuscarmem3 = (ImageButton) findViewById(R.id.btnbuscarmem3);
        btnvalidar = (ImageButton) findViewById(R.id.btnvalidar);
        btnbuscarpag = (ImageButton) findViewById(R.id.btnbuscarpag);

        btnactualizarpa = (Button) findViewById(R.id.btnactualizarpa);
        btnagregapa = (Button) findViewById(R.id.btnagregapa);
        btneliminarpa = (Button) findViewById(R.id.btneliminarpa);
        btnlistado3 = (Button) findViewById(R.id.btnlistado3);

        LoadingDialogMembresia loadingDialogMembresia = new LoadingDialogMembresia(RegistrarPago.this);


        /// boton buscar pago mediante ID //
        btnbuscarpag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarPago("https://xprojectgymx.000webhostapp.com/crud/busquedaPago.php?id_pago="+ txtidpago3.getText() + "");
            }
        });


        /// verifica si el ID membresia existe ///
        btnvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               loadingDialogMembresia.startLoadingDialog();

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialogMembresia.dismissDialog();
                    }
                },  1000);
                bordeEdi();
              buscarMembresia4("https://xprojectgymx.000webhostapp.com/crud/busquedaMembresia.php?id_membresia="+ txtmembresia3.getText() + "");
            }
        });

        /// BOTON PARA REGISTRAR PAGO //
        btnagregapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroPago();
                rec = correorec.getText().toString().trim();
                subject = "Detalle de pago";
                textMessage = "Holaaaaaa";

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Properties props = new Properties();
                /*props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");*/
                props.put("mail.smtp.host", "smtp.googlegmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                try {
                    session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email, pass);
                        }
                    });

                    if (session != null){
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(email));
                        message.setSubject(subject);
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("kokem2000@hotmail.com"));
                        message.setContent(textMessage, "text/html; charset=utf-8");
                        Transport.send(message);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }


                //RetreiveFeedTask task = new RetreiveFeedTask();
                //task.execute();
               /* try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                    message.setSubject(subject);
                    message.setText(textMessage);

                    new SendMail().execute(message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }*/
            }
        });

        /// BOTON PARA ACTUALIZAR ///
        btnactualizarpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /// BOTON PARA LLAMAR ACTIVITY LISTA DE MEMBRESIA ///
        btnbuscarmem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(RegistrarPago.this, ListaMembresia.class);
                startActivity(intent );
            }
        });

        /// BOTON ELIMINAR PAGO MEDIANTE ID //
        btneliminarpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPago();
            }
        });

        /// BOTON ABRIR LISTA DE PAGOS ///
        btnlistado3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarPago.this, ListaPago.class);
                startActivity(intent );
            }
        });
    }

    /// REGISTRAR PAGO ///
    public void registroPago() {

        String url = "https://xprojectgymx.000webhostapp.com/crud/registrarPago.php?id_membresia=" + txtmembresia3.getText().toString()
                + "&monto=" + txtmonto13.getText().toString()
               ;

        url = url.replace(" ", "%20");
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
        Toast.makeText(getApplicationContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
        txtmembresia3.setText("");
        txtmonto13.setText("");
        txtnomcompleto3.setText("");
        txtciclo3.setText("");
        txttipo3.setText("");
        txtclase3.setText("");

    }

    /// CAPTURAR NOMBRECOMPLETO | CICLOS | CLASE | TIPO_MEMBRESIA ///
    public void buscarMembresia4(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        txtnomcompleto3.setText(jsonObject.getString("fullname"));
                        String ciclo =jsonObject.optString("ciclos");//4
                        capturaciclo.setText(ciclo);//VARIABLE CICLO
                        txtciclo3.setText(ciclo);
                        String tipo =jsonObject.optString("nom_tipo");
                        String costo = jsonObject.optString("costo");//100
                        //txttipo3.setText(jsonObject.getString("nom_tipo"));
                        txttipo3.setText(tipo + " (S/." +costo+ ")");
                        txtclase3.setText(jsonObject.getString("nom_clase"));
                        String total =jsonObject.optString("Sum");
                        txtmonto13.setText(total);
                        String correo = jsonObject.optString("correo");
                        correorec.setText(correo);


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),  e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                btnvalidar.setImageResource(R.drawable.ic_equis_error);

                layoutMem.setBackgroundResource(R.drawable.background_input2);

                Toast.makeText(getApplicationContext(), "Error de conexion" + error, Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    /// BUSCAR PAGO MEDIANTE ID ///
        public void buscarPago(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        txtmembresia3.setText(jsonObject.getString("id_membresia"));
                        txtnomcompleto3.setText(jsonObject.getString("fullname"));
                        txtciclo3.setText(jsonObject.getString("ciclos"));
                        txttipo3.setText(jsonObject.getString("nom_tipo"));
                        txtclase3.setText(jsonObject.getString("nom_clase"));
                        txtmonto13.setText(jsonObject.getString("monto"));

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



    /// ELIMINAR PAGO ///
    private void eliminarPago() {
        String url = "https://xprojectgymx.000webhostapp.com/crud/eliminarPago.php?id_pago=" + txtidpago3.getText().toString();
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("elimina")) {
                    txtmembresia3.setText("");
                    txtnomcompleto3.setText("");
                    txttipo3.setText("");
                    txtciclo3.setText("");
                    txtclase3.setText("");
                    txtmonto13.setText("");
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

    /// CAJA DE TEXTO CONFIRMAR !ERROR ///
    public void bordeEdi(){
        if (txtciclo3.toString().length() >= 1){
            btnvalidar.setImageResource(R.drawable.ic_baseline_check_circle_24);

            layoutMem.setBackgroundResource(R.drawable.background_input);
            //a.setVisibility(View.GONE);

        }
    }

    public void onBackPressed(){
        finish();
        Intent intent = new Intent(RegistrarPago.this , MenuPrincipal.class);
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
                txtmembresia3.setText("");
                txtnomcompleto3.setText("");
                txttipo3.setText("");
                txtciclo3.setText("");
                txtclase3.setText("");
                txtmonto13.setText("");

                return true;
            case R.id.action_lista:
                Intent intent2 = new Intent(RegistrarPago.this, ListaPago.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class SendMail extends  AsyncTask<Message,String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Message... messages) {
            try {

                Transport.send(messages[0]);
                return "Exitoso";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), "Mensaje enviado", Toast.LENGTH_LONG).show();
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }
}