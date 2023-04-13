package com.example.gymproject.adapter.PedidoFinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymproject.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.shuhart.stepview.StepView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DetallePedidoFinal extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar mActionBar;
    ExtendedFloatingActionButton fabsoporte;
    TextView txtDetalleEstadoPedido, txtDetalleFechaPedido, txtDetalleNombreProducto,txtDetallePedidoFinalTotal,
            txtnropedido, txtSubtotalDetallePedido, txtIGVDetallePedido,txtCancelarPedido,
            txtDetalleTipoPedido, txtDetalleDireccionPedido;
    RequestQueue request;
    StepView stepView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido_final);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mToolbar = findViewById(R.id.toolbarDetalleFinalPedido);

        request = Volley.newRequestQueue(getApplicationContext());



        fabsoporte = findViewById(R.id.fabsoporte);
        txtDetalleEstadoPedido = findViewById(R.id.txtDetalleEstadoPedido);
        txtDetalleFechaPedido = findViewById(R.id.txtDetalleFechaPedido);
        txtDetalleNombreProducto = findViewById(R.id.txtDetalleNombreProducto);
        txtDetallePedidoFinalTotal = findViewById(R.id.txtDetallePedidoFinalTotal);
        txtDetalleTipoPedido = findViewById(R.id.txtDetalleTipoPedido);
        txtDetalleDireccionPedido = findViewById(R.id.txtDetalleDireccionPedido);
        txtSubtotalDetallePedido = findViewById(R.id.txtSubtotalDetallePedido);
        txtIGVDetallePedido = findViewById(R.id.txtIGVDetallePedido);
        txtnropedido =findViewById(R.id.txttemporal);
        txtCancelarPedido = findViewById(R.id.txtCancelarPedido);

        txtCancelarPedido.setPaintFlags(txtCancelarPedido.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtCancelarPedido.setText("Cancelar pedido");

        // Configuracion action bar
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_volver_atras));

        Intent intent = getIntent();
        String estadoDP = intent.getStringExtra("estado");
        String fechaDP = intent.getStringExtra("fecha");
        String montoDP = intent.getStringExtra("montoT");
        String nroDP = intent.getStringExtra("nro");
        String tipo = intent.getStringExtra("tipo");
        String direc = intent.getStringExtra("direc");


        txtnropedido.setText(nroDP);
        if (intent !=null){

            mActionBar.setTitle("Pedido #"+nroDP);
            txtDetalleEstadoPedido.setText(estadoDP);
            txtDetalleFechaPedido.setText(fechaDP);
            txtDetallePedidoFinalTotal.setText("s/"+montoDP+"");
            txtDetalleTipoPedido.setText(tipo);
            txtDetalleDireccionPedido.setText(direc);
            double total = Double.valueOf(montoDP);
            double subtotal = (total/118)*100;
            String subTP = Double.toString(subtotal);
            txtSubtotalDetallePedido.setText(String.format(Locale.ENGLISH, "s/%.2f", subtotal));

            double IGV = total-subtotal;
            txtIGVDetallePedido.setText(String.format(Locale.ENGLISH, "s/%.2f", IGV));


            switch (estadoDP){
                case "pro":
                    txtDetalleEstadoPedido.setText("En progreso...");
                    break;
                case "prs":
                    txtDetalleEstadoPedido.setText("Procesado");
                    break;
                case "env":
                    txtDetalleEstadoPedido.setText("En camino...");
                    break;
                case "pgd":
                    txtDetalleEstadoPedido.setText("Pagado");
                    break;
                case "anu":
                    txtDetalleEstadoPedido.setText("Anulado");
                    txtDetalleEstadoPedido.setTextColor(Color.RED);
                    break;

            }

            mostrarProductosDetalleFinal("https://rgym.online/gymsystem/vmovil/listarDPPF.php?id_ped=" + txtnropedido.getText().toString());
        }


        txtCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (estadoDP.equals("prs") || estadoDP.equals("env") || estadoDP.equals("pgd") || estadoDP.equals("anu")){
                    Toast.makeText(DetallePedidoFinal.this, "Este pedido no puede ser anulado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DetallePedidoFinal.this, "Este pedido si se puede ser anulado", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    ///MOSTRAR TOTAL CARRITO EA
    private void mostrarProductosDetalleFinal(String urlDPPF){

        txtDetalleNombreProducto.setText("");
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, urlDPPF, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int f= 0; f < response.length(); f++){
                            try {
                                JSONObject objeto = new JSONObject(response.get(f).toString());
                                txtDetalleNombreProducto.append(objeto.getString("nom_prod")+"("+objeto.getString("cant_prod")+")\n");
                                txtDetalleNombreProducto.append("s/"+objeto.getString("pref_prod")+"\n");
                                txtDetalleNombreProducto.append("\n");
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


}