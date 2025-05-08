package com.example.projecte2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// EditarProductoActivity.java
public class EditarProductoActivity extends AppCompatActivity {
    private EditText etNombre, etDescripcion, etPrecio, etStock, etCategoria;
    private Button btnGuardar;
    private ApiService apiService;
    private String token;
    private int productoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        etCategoria = findViewById(R.id.etCategoria);
        btnGuardar = findViewById(R.id.btnGuardar);

        productoId = getIntent().getIntExtra("producto_id", -1);
        String nombre = getIntent().getStringExtra("nombre");
        String descripcion = getIntent().getStringExtra("descripcion");
        double precio = getIntent().getDoubleExtra("precio", 0);
        int stock = getIntent().getIntExtra("stock", 0);
        String categoria = getIntent().getStringExtra("categoria");

        etNombre.setText(nombre);
        etDescripcion.setText(descripcion);
        etPrecio.setText(String.valueOf(precio));
        etStock.setText(String.valueOf(stock));
        etCategoria.setText(categoria);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        token = prefs.getString("token", "");
        apiService = RetrofitClient.getApiService();

        btnGuardar.setOnClickListener(v -> actualizarProducto());
    }

    private void actualizarProducto() {
        Producto producto = new Producto();
        producto.setNombre(etNombre.getText().toString());
        producto.setDescripcion(etDescripcion.getText().toString());
        producto.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
        producto.setStock(Integer.parseInt(etStock.getText().toString()));
        producto.setCategoria(etCategoria.getText().toString());

        Call<Producto> call = apiService.actualizarProducto(productoId, producto, "Bearer " + token);
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarProductoActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                    finish(); // cerrar y volver al catálogo
                } else {
                    Toast.makeText(EditarProductoActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText(EditarProductoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
