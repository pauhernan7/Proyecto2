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

import com.example.projecte2.MainActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity
        implements HeaderFragment.OnMenuClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

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

        // Configurar el HeaderFragment
        setupHeaderFragment();

        // ============================
        // Cargar pedidos desde Retrofit
        // ============================

        RecyclerView recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String token = prefs.getString("token", ""); // Asegúrate de almacenar el token previamente

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getOrders("Bearer " + token).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderAdapter adapter = new OrderAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(OrdersActivity.this, "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrdersActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupHeaderFragment() {
        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.headerFragment);

        if (headerFragment != null) {
            headerFragment.setOnMenuClickListener(this);
            headerFragment.setTitle("Comandes");
        }
    }

    @Override
    public void onMenuClick() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            startActivity(new Intent(this, DashboardActivity.class));
        } else if (id == R.id.nav_catalog) {
            startActivity(new Intent(this, CatalogActivity.class));
        } else if (id == R.id.nav_orders) {
            Toast.makeText(this, "Ya estás en Pedidos", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(this, SupportActivity.class));
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
}
