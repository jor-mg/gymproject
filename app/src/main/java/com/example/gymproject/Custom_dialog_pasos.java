package com.example.gymproject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Custom_dialog_pasos {

    public interface FinalizoCustomDialogPasos{
        void ResultadoCustomDialogPasos(String pasoseleccionado);
    }

    private FinalizoCustomDialogPasos interfaz;

    public Custom_dialog_pasos(Context contexto, FinalizoCustomDialogPasos actividad){
        interfaz = actividad;
        final Dialog dialog =  new Dialog(contexto);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.custom_dialog_pasos);

        Button btncancelarpasos = (Button) dialog.findViewById(R.id.btncancelarpasos);
        Button btnconfirmarpasos = (Button) dialog.findViewById(R.id.btnconfirmarpasos);
        TextView txtcant = (TextView) dialog.findViewById(R.id.txtcant);


        NumberPicker numberpickerpasos = (NumberPicker) dialog.findViewById(R.id.numberpickerpasos);
        numberpickerpasos.setMinValue(100);
        numberpickerpasos.setMaxValue(10000);

        numberpickerpasos.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                picker.setValue((newVal < oldVal)?oldVal-100:oldVal+100);
                String value = "" + picker.getValue();
                txtcant.setText(value);
                String pasoseleccionado = txtcant.getText().toString();
            }
        });



        ////////////////////////////////////////////////////

        btncancelarpasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnconfirmarpasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoCustomDialogPasos(txtcant.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //////////////////////////////

}
