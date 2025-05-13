package com.example.projecte2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerCarrito;
    private CarritoAdapter carritoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerCarrito = findViewById(R.id.recyclerCarrito);
        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));

        cargarCarritoDesdeServidor();
    }

    private void cargarCarritoDesdeServidor() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        int usuarioId = prefs.getInt("user_id", -1);  // Asegúrate de que esté guardado al iniciar sesión

        if (usuarioId == -1) {
            Toast.makeText(this, "ID d'usuari no trobat", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<CarritoResponse> call = apiService.verCarrito(usuarioId);

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
}
