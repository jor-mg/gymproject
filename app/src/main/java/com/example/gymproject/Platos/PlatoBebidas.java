package com.example.gymproject.Platos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gymproject.R;


public class PlatoBebidas extends Fragment {
    View view;
    TextView text_bebidas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_plato_bebidas, container, false);

        text_bebidas = view.findViewById(R.id.text_bebidas);

        for (int i = 0; i <= 400 ; i += 1){
            text_bebidas.append("Bebidas aquiiiiiiiiiiiiii\n");
        }

        return view;
    }
}