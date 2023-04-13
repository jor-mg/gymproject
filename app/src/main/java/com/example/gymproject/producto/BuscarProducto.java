package com.example.gymproject.producto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.example.gymproject.MenuPrincipal;
import com.example.gymproject.R;

public class BuscarProducto extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar = findViewById(R.id.dashboard_toolbarr);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();


        ///RETROCEDER///
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuPrincipalProductos.class));
                BuscarProducto.this.overridePendingTransition(R.anim.down_in, R.anim.down_out);

            }
        });
        //////////////

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_searchh);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setIconifiedByDefault(true);


        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Buscar en tienda");

        return true;

    }

}