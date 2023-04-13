package com.example.gymproject.adapter.PedidoFinal;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

public class PedidoFragAdapter extends FragmentStateAdapter {


    public PedidoFragAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1:
                return new PedidoHistorial();
        }
        return new PedidoProgreso();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getItemCount() {
        return 2;
    }



}
