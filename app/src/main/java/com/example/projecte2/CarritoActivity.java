package com.example.projecte2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

public class CarritoActivity extends AppCompatActivity
        implements HeaderFragment.OnMenuClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerCarrito;
    private CarritoAdapter carritoAdapter;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

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
            headerFragment.setTitle("Carrito");
        }


        recyclerCarrito = findViewById(R.id.recyclerCarrito);
        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));
        Button btnComprar = findViewById(R.id.btnComprar);
        btnComprar.setOnClickListener(v -> {
            // Mostrar "procesando"
            ProgressDialog progressDialog = new ProgressDialog(CarritoActivity.this);
            progressDialog.setMessage("Procesando el pago...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Simular el pago (esperar 3 segundos)
            new Handler().postDelayed(() -> {
                progressDialog.dismiss();
                Toast.makeText(CarritoActivity.this, "Compra realizada con éxito", Toast.LENGTH_LONG).show();

                // Ir a ClienteCatalogActivity
                Intent intent = new Intent(CarritoActivity.this, ClienteCatalogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }, 3000); // 3 segundos de espera
        });


        cargarCarritoDesdeServidor();
    }

    private void cargarCarritoDesdeServidor() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        String bearerToken = "Bearer " + token;
        int usuarioId = prefs.getInt("user_id", -1);  // Asegúrate de que esté guardado al iniciar sesión

        if (usuarioId == -1) {
            Toast.makeText(this, "ID d'usuari no trobat", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<CarritoResponse> call = apiService.verCarrito(usuarioId, bearerToken);

        call.enqueue(new Callback<CarritoResponse>() {
            @Override
            public void onResponse(Call<CarritoResponse> call, Response<CarritoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ItemCarrito> productos = response.body().getProductos();
                    carritoAdapter = new CarritoAdapter(CarritoActivity.this, productos);
                    recyclerCarrito.setAdapter(carritoAdapter);
                } else {
                    Toast.makeText(CarritoActivity.this, "No s'ha pogut carregar el carret", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarritoResponse> call, Throwable t) {
                Toast.makeText(CarritoActivity.this, "Error de connexió", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(this, ClienteCatalogActivity.class));
            finish();
        } else if (id == R.id.nav_carrito) {
            Toast.makeText(this, "Ja ets a Carrito", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(this, CrearTicketActivity.class));
            finish();
        } else if (id == R.id.nav_verTickets) {
            startActivity(new Intent(this, VerTicketsSoporteActivity.class));
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
