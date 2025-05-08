package com.example.projecte2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.projecte2.MainActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends AppCompatActivity
        implements HeaderFragment.OnMenuClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerCatalog;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Configurar el Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // Obtener el header del NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Obtener los TextViews del header
        TextView tvRol = headerView.findViewById(R.id.tvRol);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);

        // Obtener los datos de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String rol = prefs.getString("rol", "No definido");
        String email = prefs.getString("email", "correo@ejemplo.com");

        // Establecer los datos en el header
        tvRol.setText(rol);
        tvEmail.setText(email);


        // Configurar el header fragment
        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.headerFragment);

        if (headerFragment != null) {
            headerFragment.setOnMenuClickListener(this);
            headerFragment.setTitle("Catàleg"); // Establecer título
        }


        recyclerCatalog = findViewById(R.id.recyclerCatalog);
        recyclerCatalog.setLayoutManager(new LinearLayoutManager(this));

        // Aquí configurarías tu RecyclerView para el catálogo
        setupCatalog();

        Button btnAddProducto = findViewById(R.id.btnAddProducto);
        btnAddProducto.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogActivity.this, AddProductoActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCatalog();
    }

    private void setupCatalog() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", ""); // Asegúrate de almacenar el token previamente

        ApiService apiService = RetrofitClient.getApiService();

        Call<List<Producto>> call = apiService.listarProductos();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    productAdapter = new ProductAdapter(CatalogActivity.this, productos, apiService, token);
                    recyclerCatalog.setAdapter(productAdapter);
                } else {
                    Toast.makeText(CatalogActivity.this, "Error al obtener productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(CatalogActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
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
        } else if (id == R.id.logout) {
            // Borrar SharedPreferences
            getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Ir a la pantalla de login y cerrar actividades anteriores
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

    public void recargarDatosDesdeServidor() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        ApiService apiService = RetrofitClient.getApiService();

        Call<List<Producto>> call = apiService.listarProductos();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (productAdapter != null) {
                        productAdapter.updateData(response.body());
                    } else {
                        productAdapter = new ProductAdapter(CatalogActivity.this, response.body(), apiService, token);
                        recyclerCatalog.setAdapter(productAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(CatalogActivity.this, "Error al recargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}