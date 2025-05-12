package com.example.projecte2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteCatalogActivity extends AppCompatActivity
        implements HeaderFragment.OnMenuClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerCatalog;
    private ProductoClienteAdapter productoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_catalog); // Asegúrate de tener este layout correctamente creado

        // Configurar Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Configurar header del NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView tvRol = headerView.findViewById(R.id.tvRol);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String rol = prefs.getString("rol", "No definido");
        String email = prefs.getString("email", "correo@ejemplo.com");
        tvRol.setText(rol);
        tvEmail.setText(email);

        // Configurar HeaderFragment
        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.headerFragment);
        if (headerFragment != null) {
            headerFragment.setOnMenuClickListener(this);
            headerFragment.setTitle("Catàleg");
        }

        recyclerCatalog = findViewById(R.id.recyclerCatalog);
        recyclerCatalog.setLayoutManager(new LinearLayoutManager(this));

        // Cargar catálogo
        setupCatalog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCatalog();
    }

    private void setupCatalog() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        ApiService apiService = RetrofitClient.getApiService();

        Call<List<Producto>> call = apiService.listarProductos();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    productoAdapter = new ProductoClienteAdapter(productos, ClienteCatalogActivity.this);
                    recyclerCatalog.setAdapter(productoAdapter);
                } else {
                    Toast.makeText(ClienteCatalogActivity.this, "Error al obtener productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(ClienteCatalogActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMenuClick() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {
            Toast.makeText(this, "Ja ets a Catàleg", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_carrito) {
            startActivity(new Intent(this, CarritoActivity.class));
            finish();
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(this, SupportActivity.class));
            finish();
        } else if (id == R.id.logout) {
            getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
