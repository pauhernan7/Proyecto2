package com.example.projecte2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarProductoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private EditText etNombre, etDescripcion, etPrecio, etStock;
    private ImageView ivImagenProducto;
    private Button btnSeleccionarImagen, btnGuardarCambios;
    private Producto producto;

    private Uri nuevaImagenUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        ivImagenProducto = findViewById(R.id.ivImagenProducto);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        producto = (Producto) getIntent().getSerializableExtra("producto");

        if (producto != null) {
            etNombre.setText(producto.getNombre());
            etDescripcion.setText(producto.getDescripcion());
            etPrecio.setText(String.valueOf(producto.getPrecio()));
            etStock.setText(String.valueOf(producto.getStock()));

            SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
            String imagenBase64 = prefs.getString("imagen_producto_" + producto.getId(), null);
            if (imagenBase64 != null) {
                byte[] imageBytes = Base64.decode(imagenBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivImagenProducto.setImageBitmap(bitmap);
            }
        }

        btnSeleccionarImagen.setOnClickListener(v -> seleccionarImagen());
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            nuevaImagenUri = data.getData();
            ivImagenProducto.setImageURI(nuevaImagenUri);
        }
    }

    private String convertirImagenABase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

            int maxWidth = 800;
            if (bitmap.getWidth() > maxWidth) {
                int newHeight = (int) (bitmap.getHeight() * ((float) maxWidth / bitmap.getWidth()));
                bitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarCambios() {
        String nuevoNombre = etNombre.getText().toString();
        String nuevaDescripcion = etDescripcion.getText().toString();
        double nuevoPrecio = Double.parseDouble(etPrecio.getText().toString());
        int nuevoStock = Integer.parseInt(etStock.getText().toString());

        producto.setNombre(nuevoNombre);
        producto.setDescripcion(nuevaDescripcion);
        producto.setPrecio(nuevoPrecio);
        producto.setStock(nuevoStock);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String authHeader = "Bearer " + token;

        ApiService apiService = RetrofitClient.getApiService();
        Call<Producto> call = apiService.actualizarProducto(producto.getId(), producto, authHeader);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    if (nuevaImagenUri != null) {
                        String imagenBase64 = convertirImagenABase64(nuevaImagenUri);
                        if (imagenBase64 != null) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("imagen_producto_" + producto.getId(), imagenBase64);
                            editor.apply();
                        }
                    }

                    Toast.makeText(EditarProductoActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditarProductoActivity.this, "Error al actualizar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText(EditarProductoActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
