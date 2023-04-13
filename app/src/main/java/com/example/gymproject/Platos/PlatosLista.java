package com.example.gymproject.Platos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.gymproject.R;
import com.example.gymproject.adapter.PedidoFinal.PedidoFragAdapter;
import com.google.android.material.tabs.TabLayout;

public class PlatosLista extends AppCompatActivity {

    TabLayout tabLayoutM;
    ViewPager2 pager5;
    PlatosFragAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platos_lista);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tabLayoutM = findViewById(R.id.tab_layoutMenu);
        pager5 = findViewById(R.id.view_pager5);


        FragmentManager fm = getSupportFragmentManager();
        adapter = new PlatosFragAdapter(fm, getLifecycle());
        pager5.setAdapter(adapter);

        tabLayoutM.addTab(tabLayoutM.newTab().setText("CARNES"));
        tabLayoutM.addTab(tabLayoutM.newTab().setText("VEGANO"));
        tabLayoutM.addTab(tabLayoutM.newTab().setText("ENSALADAS"));
        tabLayoutM.addTab(tabLayoutM.newTab().setText("POSTRES"));
        tabLayoutM.addTab(tabLayoutM.newTab().setText("BOLWS"));
        tabLayoutM.addTab(tabLayoutM.newTab().setText("BEBIDAS"));

        tabLayoutM.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager5.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager5.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayoutM.selectTab(tabLayoutM.getTabAt(position));
            }
        });

    }
}