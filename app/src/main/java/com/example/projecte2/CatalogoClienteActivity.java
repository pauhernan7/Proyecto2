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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogoClienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerCatalogo;
    private ProductoAdapter productoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_cliente);

        // Configurar el Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtener el header del NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView tvRol = headerView.findViewById(R.id.tvRol);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);

        // Obtener los datos de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String rol = prefs.getString("rol", "No definido");
        String email = prefs.getString("email", "correo@ejemplo.com");

        // Establecer los datos en el header
        tvRol.setText(rol);
        tvEmail.setText(email);

        // Configurar RecyclerView
        recyclerCatalogo = findViewById(R.id.recyclerCatalogo);
        recyclerCatalogo.setLayoutManager(new LinearLayoutManager(this));

        // Cargar los productos
        setupCatalogo();

    }

    private void setupCatalogo() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", ""); // Asegúrate de almacenar el token previamente

        ApiService apiService = RetrofitClient.getApiService();

        Call<List<Producto>> call = apiService.listarProductos();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    productoAdapter = new ProductoAdapter(productos, CatalogoClienteActivity.this);
                    recyclerCatalogo.setAdapter(productoAdapter);
                } else {
                    Toast.makeText(CatalogoClienteActivity.this, "Error al obtener productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(CatalogoClienteActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {
            // Ya estamos en Catàleg, solo cerramos el menú
            Toast.makeText(this, "Ya estás en Catálogo", Toast.LENGTH_SHORT).show();
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
}
