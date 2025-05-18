package com.example.projecte2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class VerTicketsSoporteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HeaderFragment.OnMenuClickListener {


    private RecyclerView recyclerView;
    private TicketClienteAdapter adapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tickets_soporte);

        // Configurar el Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtener el header del NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView tvRol = headerView.findViewById(R.id.tvRol);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        tvRol.setText(prefs.getString("rol", "No definido"));
        tvEmail.setText(prefs.getString("email", "correo@ejemplo.com"));

        // Configurar HeaderFragment
        setupHeaderFragment();

        recyclerView = findViewById(R.id.recyclerTicketsCliente);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketClienteAdapter(this);
        recyclerView.setAdapter(adapter);

        cargarTicketsCliente();
    }

    private void setupHeaderFragment() {
        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.headerFragment);

        if (headerFragment != null) {
            headerFragment.setOnMenuClickListener(this);
            headerFragment.setTitle("Veure tickets");
        }
    }

    @Override
    public void onMenuClick() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {
            startActivity(new Intent(this, ClienteCatalogActivity.class));
            finish();
        } else if (id == R.id.nav_carrito) {
            startActivity(new Intent(this, CarritoActivity.class));
            finish();
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(this, CrearTicketActivity.class));
            finish();
        } else if (id == R.id.nav_verTickets) {
            Toast.makeText(this, "Ja ets a veure tickets", Toast.LENGTH_SHORT).show();

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

    private void cargarTicketsCliente() {
        SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        ApiService apiService = RetrofitClient.getApiService();
        apiService.obtenerMisTickets("Bearer " + token).enqueue(new Callback<List<TicketResponse>>() {
            @Override
            public void onResponse(Call<List<TicketResponse>> call, Response<List<TicketResponse>> response) {
                if (response.isSuccessful()) {
                    List<TicketResponse> tickets = response.body();
                    adapter.setTickets(tickets);
                } else {
                    Toast.makeText(VerTicketsSoporteActivity.this, "Error al carregar tickets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TicketResponse>> call, Throwable t) {
                Toast.makeText(VerTicketsSoporteActivity.this, "Error de connexió", Toast.LENGTH_SHORT).show();
                Log.e("API", "Fallo: ", t);
            }
        });
    }
}
