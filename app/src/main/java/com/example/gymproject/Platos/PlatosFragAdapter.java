package com.example.gymproject.Platos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gymproject.adapter.PedidoFinal.PedidoHistorial;
import com.example.gymproject.adapter.PedidoFinal.PedidoProgreso;

public class PlatosFragAdapter extends FragmentStateAdapter {
    public PlatosFragAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new PlatoVeganos();
            case 2:
                return new PlatoEnsaladas();
            case 3:
                return new PlatoPostres();
            case 4:
                return new PlatoBolws();
            case 5:
                return new PlatoBebidas();
        }
        return new PlatoCarnes();
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
