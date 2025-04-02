package com.example.projecte2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class CatalogActivity extends AppCompatActivity
        implements HeaderFragment.OnMenuClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Configurar el Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Configurar el header fragment
        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.headerFragment);

        if (headerFragment != null) {
            headerFragment.setOnMenuClickListener(this);
            headerFragment.setTitle("Catàleg"); // Establecer título
        }

        // Aquí configurarías tu RecyclerView para el catálogo
        setupCatalog();
    }

    private void setupCatalog() {
        // Configuración de tu lista de productos
        // ...
    }

    @Override
    public void onMenuClick() {
        // Abrir el menú al hacer clic en el icono
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else if (id == R.id.nav_catalog) {
            // Ya estamos en Catàleg, solo cerramos el menú
            Toast.makeText(this, "Ja ets a Catàleg", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(this, OrdersActivity.class));
            finish();
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(this, SupportActivity.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}