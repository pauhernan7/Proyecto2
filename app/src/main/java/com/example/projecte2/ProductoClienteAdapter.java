package com.example.projecte2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoClienteAdapter extends RecyclerView.Adapter<ProductoClienteAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private Context context;

    public ProductoClienteAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_cliente, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        holder.tvCategoria.setText("Categoría: " + producto.getCategoria());
        holder.tvPrecio.setText("Preu: " + producto.getPrecio() + "€");

        // Cargar imagen desde SharedPreferences si está guardada
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String imagenBase64 = prefs.getString("imagen_producto_" + producto.getId(), null);

        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            String url = "data:image/jpeg;base64," + imagenBase64;
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.logo_empresa)
                    .into(holder.ivImagenProducto);
        } else {
            holder.ivImagenProducto.setImageResource(R.drawable.logo_empresa);
        }

        holder.btnComprar.setOnClickListener(v -> {
            String token = prefs.getString("token", "");
            int usuarioId = prefs.getInt("user_id", 0);
            int tiendaId = producto.getTienda_id();

            ItemCarrito item = new ItemCarrito(producto.getId(), 1);
            item.setNombre(producto.getNombre());  // Asegúrate que estos campos
            item.setPrecio(producto.getPrecio()); // lleguen al backend
            // Eliminamos item.setImagenBase64();

            ApiService apiService = RetrofitClient.getApiService();
            Call<Void> call = apiService.agregarAlCarrito("Bearer " + token, usuarioId, tiendaId, item);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Afegit al carret!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al afegir al carret", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Fallo de connexió", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImagenProducto;
        TextView tvNombre, tvDescripcion, tvCategoria, tvPrecio;
        Button btnComprar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagenProducto = itemView.findViewById(R.id.ivImagenProducto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnComprar = itemView.findViewById(R.id.btnComprar);
        }
    }


}
