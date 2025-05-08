package com.example.projecte2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Producto> productos;
    private final ApiService apiService;
    private final Context context;
    private final String token;

    public ProductAdapter(Context context, List<Producto> productos, ApiService apiService, String token) {
        this.context = context;
        this.productos = productos;
        this.apiService = apiService;
        this.token = token;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.tvNombre.setText("Nom: " + producto.getNombre());
        holder.tvDescripcion.setText("Descripció: " + producto.getDescripcion());
        holder.tvPrecio.setText("Preu: " + producto.getPrecio() + " €");
        holder.tvCategoria.setText("Categoria: " + producto.getCategoria());

        // Obtener la imagen codificada en base64 desde SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String encodedImage = prefs.getString("imagen_producto_" + producto.getId(), null);

        if (encodedImage != null) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);

            // Cargar la imagen con Glide
            Glide.with(context)
                    .asBitmap()
                    .load(decodedString)
                    .placeholder(R.drawable.logo_empresa) // Imagen mientras carga
                    .error(R.drawable.logo_empresa) // Imagen si hay error
                    .into(holder.ivImagenProducto);
        } else {
            // Si no hay imagen, mostrar un placeholder
            Glide.with(context)
                    .load(R.drawable.logo_empresa)
                    .into(holder.ivImagenProducto);
            Log.d("ProductAdapter", "No se encontró imagen codificada para el producto con ID: " + producto.getId());
        }



        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarProductoActivity.class);
            intent.putExtra("producto", producto); // el objeto completo
            context.startActivity(intent);
        });


        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar producto")
                    .setMessage("¿Estás seguro de eliminar este producto?")
                    .setPositiveButton("Eliminar", (dialog, which) -> eliminarProducto(holder.getAdapterPosition(), producto.getId()))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }


    private void eliminarProducto(int position, int productoId) {
        if (position == RecyclerView.NO_POSITION) return;

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Eliminando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String authHeader = "Bearer " + token;
        Log.d("DEBUG", "Auth Header: " + authHeader);
        Log.d("DEBUG", "Eliminando producto ID: " + productoId);

        apiService.eliminarProducto(productoId, authHeader).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Log.d("RESPONSE_CODE", "Código de respuesta: " + response.code());

                if (response.isSuccessful() || response.code() == 204) {
                        Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        productos.remove(position); // Elimina el producto de la lista
                        notifyItemRemoved(position); // Notifica al adapter que se eliminó
                        notifyItemRangeChanged(position, productos.size()); // Actualiza el rango de la lista


                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e("ERROR_PARSING", "No se pudo leer el cuerpo de error", e);
                    }

                    Log.e("ERROR_DELETE", "Error al eliminar. Código: " + response.code() + " | Body: " + errorBody);
                    Toast.makeText(context, "Error del servidor al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ERROR_DELETE", "Fallo de conexión", t);
                Toast.makeText(context, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio, tvCategoria;
        Button btnEditar, btnEliminar;
        ImageView ivImagenProducto;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            ivImagenProducto = itemView.findViewById(R.id.ivImagenProducto); // Asegúrate de que este ID existe en item_producto.xml
        }
    }

    public void updateData(List<Producto> nuevosProductos) {
        productos.clear();
        productos.addAll(nuevosProductos);
        notifyDataSetChanged();
    }
}
