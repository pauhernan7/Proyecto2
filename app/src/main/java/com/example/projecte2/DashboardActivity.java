package com.example.projecte2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Datos de prueba para el gráfico
        int producto1 = 3;
        int producto2 = 5;
        int producto3 = 9;
        int totalProductos = producto1 + producto2 + producto3;

        // Configurar el gráfico
        setupPieChart(producto1, producto2, producto3, totalProductos);

        // Mostrar el producto más vendido
        showMostSoldProduct(producto1, producto2, producto3);

        // Configurar el RecyclerView de últimos pedidos
        setupRecentOrdersRecyclerView();
    }

    private void setupPieChart(int producto1, int producto2, int producto3, int totalProductos) {
        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(producto1, "Producto A"));
        entries.add(new PieEntry(producto2, "Producto B"));
        entries.add(new PieEntry(producto3, "Producto C"));

        PieDataSet dataSet = new PieDataSet(entries, "Productos Vendidos");
        dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

        Description description = new Description();
        description.setText("Total vendidos: " + totalProductos);
        description.setTextSize(14f);
        pieChart.setDescription(description);
        pieChart.invalidate();
    }

    private void showMostSoldProduct(int producto1, int producto2, int producto3) {
        TextView tvMostSoldProduct = findViewById(R.id.tvMostSoldProduct);

        // Determinar el producto más vendido
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

    private void setupRecentOrdersRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvRecentOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Datos de prueba para los últimos pedidos
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
}