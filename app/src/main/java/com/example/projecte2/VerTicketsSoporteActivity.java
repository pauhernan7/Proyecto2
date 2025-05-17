package com.example.projecte2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerTicketsSoporteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TicketClienteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tickets_soporte);

        recyclerView = findViewById(R.id.recyclerTicketsCliente);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketClienteAdapter(this);
        recyclerView.setAdapter(adapter);

        cargarTicketsCliente();
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
