package com.example.gymproject.adapter.CarritoDeCompra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymproject.R;
import com.example.gymproject.producto.MenuPrincipalProductos;
import com.example.gymproject.producto.Productos;

public class ConfirmPagoDesign extends AppCompatActivity {
    TextView txtpedidorealizado2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pago_design);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtpedidorealizado2 = findViewById(R.id.txtpedidorealizado2);

        txtpedidorealizado2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtpedidorealizado2.setText("Seguir comprando");


        txtpedidorealizado2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfirmPagoDesign.this, "Seguir comprando", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent (ConfirmPagoDesign.this, MenuPrincipalProductos.class);
                startActivity(intent);
            }
        });
    }
}