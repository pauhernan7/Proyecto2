package com.example.projecte2;

import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.navigation.NavigationView;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity
        implements HeaderFragment.OnMenuClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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
        setupHeaderFragment();

        // Configurar el gráfico de productos
        setupProductChart();

        // Mostrar el producto más vendido
        setupMostSoldProduct();

        // Configurar la lista de últimos pedidos
        setupRecentOrders();
    }

    private void setupHeaderFragment() {
        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.headerFragment);

        if (headerFragment != null) {
            headerFragment.setOnMenuClickListener(this);
            headerFragment.setTitle("Dashboard"); // Ahora este método funciona
        }


    }

    private void setupProductChart() {
        PieChart pieChart = findViewById(R.id.pieChart);
        TextView tvMostSoldProduct = findViewById(R.id.tvMostSoldProduct);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", null);  // Asegúrate de guardar el token al iniciar sesión

        if (token == null) {
            Toast.makeText(this, "Token no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<ProductoVendido>> call = apiService.getProductosVendidos("Bearer " + token);

        call.enqueue(new Callback<List<ProductoVendido>>() {
            @Override
            public void onResponse(Call<List<ProductoVendido>> call, Response<List<ProductoVendido>> response) {
                if (response.isSuccessful()) {
                    List<ProductoVendido> productos = response.body();
                    if (productos == null || productos.isEmpty()) {
                        Toast.makeText(DashboardActivity.this, "No hay productos vendidos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<PieEntry> entries = new ArrayList<>();
                    int totalVendidos = 0;

                    ProductoVendido masVendido = productos.get(0);
                    for (ProductoVendido producto : productos) {
                        entries.add(new PieEntry(producto.getCantidad_vendida(), producto.getNombre()));
                        totalVendidos += producto.getCantidad_vendida();

                        if (producto.getCantidad_vendida() > masVendido.getCantidad_vendida()) {
                            masVendido = producto;
                        }
                    }

                    PieDataSet dataSet = new PieDataSet(entries, "Productos Vendidos");
                    dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN);
                    dataSet.setValueTextSize(14f);
                    dataSet.setValueTextColor(Color.WHITE);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getPieLabel(float value, PieEntry pieEntry) {
                            // value ya es el porcentaje si `setUsePercentValues(true)`
                            int cantidad = (int) pieEntry.getValue();  // cantidad_vendida
                            return cantidad + " (" + Math.round(value) + "%)";
                        }
                    });

                    pieChart.setData(data);
                    pieChart.setUsePercentValues(true);
                    pieChart.setEntryLabelColor(Color.BLACK);
                    pieChart.setEntryLabelTextSize(12f);

                    Description description = new Description();
                    description.setText("Total vendidos: " + totalVendidos);
                    description.setTextSize(14f);
                    pieChart.setDescription(description);
                    pieChart.invalidate();

                    // Actualizar texto del producto más vendido
                    tvMostSoldProduct.setText(masVendido.getNombre() + " (" + masVendido.getCantidad_vendida() + " unidades)");
                } else {
                    Toast.makeText(DashboardActivity.this, "Error al obtener productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductoVendido>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupMostSoldProduct() {
        TextView tvMostSoldProduct = findViewById(R.id.tvMostSoldProduct);

        // Datos de prueba
        int producto1 = 3;
        int producto2 = 5;
        int producto3 = 9;

        String mostSoldProduct;
        if (producto3 >= producto2 && producto3 >= producto1) {
            mostSoldProduct = "Producto C (" + producto3 + " unidades)";
        } else if (producto2 >= producto1 && producto2 >= producto3) {
            mostSoldProduct = "Producto B (" + producto2 + " unidades)";
        } else {
            mostSoldProduct = "Producto A (" + producto1 + " unidades)";
        }

        tvMostSoldProduct.setText(mostSoldProduct);
    }

    private void setupRecentOrders() {
        RecyclerView recyclerView = findViewById(R.id.rvRecentOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Datos de prueba
        List<Order> recentOrders = Arrays.asList(
                new Order("#1234", "15/06/2023", "€125.50"),
                new Order("#1233", "14/06/2023", "€89.99"),
                new Order("#1232", "14/06/2023", "€45.20"),
                new Order("#1231", "13/06/2023", "€210.00"),
                new Order("#1230", "12/06/2023", "€65.75")
        );

        OrderAdapter adapter = new OrderAdapter(recentOrders);
        recyclerView.setAdapter(adapter);
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
            // Ya estamos en Dashboard
            Toast.makeText(this, "Ya estás en Dashboard", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_catalog) {
            startActivity(new Intent(this, CatalogActivity.class));
        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(this, OrdersActivity.class));
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
