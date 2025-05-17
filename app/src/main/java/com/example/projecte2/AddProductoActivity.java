package com.example.projecte2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Base64;

public class AddProductoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView ivImagenProducto;
    private Uri selectedImageUri = null;

    private EditText etNombre, etDescripcion, etPrecio, etStock, etCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        etCategoria = findViewById(R.id.etCategoria);
        ivImagenProducto = findViewById(R.id.ivImagenProducto);
        Button btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnSeleccionarImagen.setOnClickListener(v -> seleccionarImagen());
        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ivImagenProducto.setImageURI(selectedImageUri);
        }
    }

    private String convertirImagenABase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            // Decodificar con opciones para reducir el tamaño
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // Reduce la imagen a la mitad

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

            // Redimensionar si es necesario (ejemplo: máximo 800px de ancho)
            int maxWidth = 800;
            if (bitmap.getWidth() > maxWidth) {
                int newHeight = (int) (bitmap.getHeight() * ((float) maxWidth / bitmap.getWidth()));
                bitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream); // Calidad al 80%
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        double precio = Double.parseDouble(etPrecio.getText().toString());
        int stock = Integer.parseInt(etStock.getText().toString());
        String categoria = etCategoria.getText().toString();
        int tiendaId = 1;

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setDescripcion(descripcion);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setStock(stock);
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setTienda_id(tiendaId);

        // Convertir imagen seleccionada a base64
        final String imagenBase64;
        if (selectedImageUri != null) {
            imagenBase64 = convertirImagenABase64(selectedImageUri);
        } else {
            imagenBase64 = null;
        }

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String authHeader = "Bearer " + token;

        ApiService apiService = RetrofitClient.getApiService();
        Call<Producto> call = apiService.crearProducto(authHeader, nuevoProducto);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    Producto creado = response.body();

                    // Guardar la imagen SOLO después de obtener el ID del producto
                    if (imagenBase64 != null && creado != null) {
                        SharedPreferences.Editor editor = prefs.edit();
                        String clave = "imagen_producto_" + creado.getId();
                        editor.putString(clave, imagenBase64);
                        editor.apply();
                        android.util.Log.d("AddProductoActivity", "Imagen guardada con clave: " + clave);

                    }

                    // Volver con resultado a CatalogActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("producto", creado);
                    setResult(RESULT_OK, resultIntent);
                    finish();

                } else {
                    Toast.makeText(AddProductoActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText(AddProductoActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

