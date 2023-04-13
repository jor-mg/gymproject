package com.example.gymproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.gymproject.databinding.ActivityUbicacionPedidoBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UbicacionPedido extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityUbicacionPedidoBinding binding;

    private LatLng latLng;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    String latitudd, longitudd, direccioncorta;
    EditText txtlati, txtlong;
    EditText txtnombre, txtnombre2;
    Button btncontinuar;
    String direccion,ref1,ref2,lati,longi;
    String direcValidacion,ref1Va,ref2Va,latiVa,longiVa;
    SharedPreferences preferences;
    Double la, lo;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityUbicacionPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        txtlati = findViewById(R.id.txtlati);
        txtlong = findViewById(R.id.txtlong);

        txtnombre = findViewById(R.id.txtnombre);
        txtnombre2 = findViewById(R.id.txtnombre2);

        btncontinuar = findViewById(R.id.btncontinuar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(UbicacionPedido.this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);





        //<--Recuperar datos SharedPreferencesUbicacion
        preferences = getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
        direcValidacion = preferences.getString("direccion", "");
        ref1Va = preferences.getString("referencia1", "");
        ref2Va = preferences.getString("referencia2", "");
        latiVa = preferences.getString("latitud", "");
        longiVa = preferences.getString("longitud", "");
        /*Toast.makeText(UbicacionPedido.this, "Dire:" + txtnombre.getText().toString()
                    + "Ref1: " +txtnombre2.getText().toString() + "Lati: " +txtlati.getText().toString()
                    + "Long: " +txtlong.getText().toString(), Toast.LENGTH_SHORT).show();*/
        if (direcValidacion.isEmpty()){
            Toast.makeText(UbicacionPedido.this, "No se encontró ubicación", Toast.LENGTH_SHORT).show();
        }else{
            recuperarDatos();
        }
        //-->


        /*if (txtnombre.getText().toString().length() == 0){
            latLng = new LatLng(-9.065548, -78.581731);
        }else{
            latLng = new LatLng(la, lo);
        }*/








        btncontinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref1 = txtnombre2.getText().toString();
                guardarDatos();
                Intent intent = new Intent(UbicacionPedido.this, ConfirmUbicacionPedido.class);
                startActivity(intent);

            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //<--Validación de campo vacio SINO mostrar coordenadas del SharedPreferencesUbicacion
        if (txtnombre.getText().toString().length() == 0){

            latLng = new LatLng(-9.0653072,  -78.5833476);
            LatLng sydney = new LatLng(-9.0653072, -78.5833476);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Super Rayner Gym"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        }else{
            latLng = new LatLng(la, lo);
            LatLng sydney = new LatLng(la, lo);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Mi ubicación actual"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        }
        //FinValidación-->

        //LatLng sydney = new LatLng(-9.065548, -78.581731);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Mi ubicación actual"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                //markerOptions.title(latLng.latitude + ":" + latLng.longitude);
                txtlati.setText("" + latLng.latitude);
                txtlong.setText("" + latLng.longitude);
                latitudd = String.valueOf(latLng.latitude);
                longitudd = String.valueOf(latLng.longitude);
                mMap.clear();
                mMap.addMarker(markerOptions);
                
                
                try {
                    getLocationDetails();    
                }catch (Exception e){
                    Toast.makeText(UbicacionPedido.this, "¡UPS! No se pudo reconocer el nombre de la calle", Toast.LENGTH_SHORT).show();
                }
                

            }

        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    String phoneNumber = "999999";
                    String message = "Latitude = " + latitudd + "Longitude = " + longitudd;
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void getLocationDetails() {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        if (!(txtlati.getText().toString().isEmpty() || txtlong.getText().toString().isEmpty())){
            latitude = Double.parseDouble(txtlati.getText().toString());
            longitude = Double.parseDouble(txtlong.getText().toString());

            lati = txtlati.getText().toString();
            longi = txtlong.getText().toString();

            latLng = new LatLng(latitude,longitude);

        }
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(UbicacionPedido.this, Locale.getDefault());

        String address = null;
        String city = null;
        String state = null;
        String country = null;
        String postalCode = null;
        String knonName = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getCountryName();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getCountryCode();
            knonName = addresses.get(0).getFeatureName();

            //txtnombre2.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }


        mMap.addMarker(new MarkerOptions().position(latLng).title("Mi ubicación"));

        int index = 0;
        index = address.indexOf(",");
        direccioncorta = address.substring(0,index+0); //index+1 to skip =


        txtnombre.setText(direccioncorta);


        /*
        campo coordenadas = -9.0653072,-78.5833476
        https://maps.google.com/?q=-9.0653072,-78.5833476


        */

    }

    private void guardarDatos() {
        direccion = txtnombre.getText().toString();
        preferences = this.getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("direccion", direccion);
        editor.putString("referencia1", ref1);
        editor.putString("referencia2", "Ninguna");
        editor.putString("referencia3", "Ninguna");
        //editor.putString("latitud", lati);
        //editor.putString("longitud", longi);
        editor.putString("latitud", txtlati.getText().toString());
        editor.putString("longitud", txtlong.getText().toString());
        editor.putBoolean("sesionUbi", true);
        editor.apply();
        editor.commit();
    }

    private void recuperarDatos() {
        preferences = this.getSharedPreferences("guardarDatosUbicacion", Context.MODE_PRIVATE);
        txtnombre.setText(preferences.getString("direccion", ""));
        txtnombre2.setText(preferences.getString("referencia1", ""));
        txtlati.setText(preferences.getString("latitud", ""));
        txtlong.setText(preferences.getString("longitud", ""));
        la = Double.parseDouble(txtlati.getText().toString());
        lo = Double.parseDouble(txtlong.getText().toString());

    }


}