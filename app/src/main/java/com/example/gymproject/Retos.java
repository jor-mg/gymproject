package com.example.gymproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class Retos extends AppCompatActivity implements Custom_dialog_pasos.FinalizoCustomDialogPasos {

    private TextView steps;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    private Integer stepOriginal = 0;
    private Integer cantidadPasos = 0;
    private Double cantidadMiles = 0.1;

    DonutProgress donutProgress;
    TextView txtcantidadactual,txtcalorias,txtmiles;
    EditText pasoscambiados;
    ImageView cambiarpasos;
    Context contexto;
    boolean run = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retos);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //btncambia = findViewById(R.id.btncambia);
        //txtpasos2 =  findViewById(R.id.txtpasos2);
        Toolbar toolbarPerfil = findViewById(R.id.toolbarPerfil);
        setSupportActionBar(toolbarPerfil);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_volver_atras));
        mActionBar.setTitle("");


        toolbarPerfil.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuPrincipal.class));
                Retos.this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
            }
        });


        donutProgress = findViewById(R.id.circlestep);
        pasoscambiados = findViewById(R.id.pasoscambiados);
        cambiarpasos = findViewById(R.id.cambiarpasos);

        //contador de pasos//
        steps = findViewById(R.id.steps);
        //contador calorias
        txtcalorias = findViewById(R.id.txtcalorias);
        //contador miles(km)
        txtmiles = findViewById(R.id.txtmiles);
        //servicio sensor//
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        contexto = this;


        pasoscambiados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Custom_dialog_pasos(contexto, Retos.this);
                Toast.makeText(getApplicationContext(), "FUNCIONA", Toast.LENGTH_SHORT).show();
            }
        });


        //INICIAR VARIABLES FIJAS//
        Integer n1 = 200;
        Integer n2 = n1/100;

        SensorEventListener stepDetector  = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent!= null){
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if (MagnitudeDelta > 2){
                        stepCount ++;

                        //AUMENTO DE MILES (KM)
                        for (int i = 0; i <= n1 ; i += 161){
                            if (stepCount == i ){
                                cantidadMiles += 0.1;
                                DecimalFormat precision = new DecimalFormat("0.0");
                                txtmiles.setText(precision.format(cantidadMiles));
                            }
                        }


                        //AUMENTO DE CALORIAS
                        for (int i = 0; i <= n1 ; i += 27){
                            if (stepCount == i ){
                                cantidadPasos ++;
                                txtcalorias.setText(cantidadPasos.toString());
                            }
                        }

                        //AUMENTO DEL PROGRESS CIRCLE
                        for (int i = 0 ; i <= n1 ; i += n2){
                            if (stepCount == i ){
                                stepOriginal ++; ///CCIRCULO PRGORESS
                            }
                        }

                        //circuloporcentaje
                        if (stepOriginal <= 100){
                            updateProgressBar();
                            //stepOriginal += stepOriginal;
                        }
                    }
                    steps.setText(stepCount.toString());

                }
            }

            private void updateProgressBar() {
                donutProgress.setDonut_progress(String.valueOf(stepOriginal));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);



    }

    //RECUPERACION DE DATOS STEPS
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount",0);
    }




    ///VARIABLE DEL CUSTOM_DIALOG_PASOS
    @Override
    public void ResultadoCustomDialogPasos(String pasoseleccionado) {
        pasoscambiados.setText(pasoseleccionado);
    }
}