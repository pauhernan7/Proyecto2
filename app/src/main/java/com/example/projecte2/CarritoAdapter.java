package com.example.projecte2;

import android.content.Context;
import android.content.SharedPreferences;
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

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private List<ItemCarrito> productos;
    private Context context;

    public CarritoAdapter(Context context, List<ItemCarrito> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCarrito producto = productos.get(position);

        // Configurar los textos
        holder.tvNombre.setText("Nom: " + producto.getNombre());
        holder.tvPrecio.setText("Preu: " + producto.getPrecio() + "€");
        holder.tvCantidad.setText("Quantitat: " + producto.getCantidad());
        holder.tvSubtotal.setText("Subtotal: " + producto.getSubtotal() + "€");

        // Obtener imagen desde SharedPreferences usando el ID del producto
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String clave = "imagen_producto_" + producto.getId();
        String imagenBase64 = prefs.getString(clave, null);

        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            String url = "data:image/jpeg;base64," + imagenBase64;
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.logo_empresa)
                    .error(R.drawable.logo_empresa)
                    .into(holder.ivProducto);
        } else {
            holder.ivProducto.setImageResource(R.drawable.logo_empresa);
        }

        // Eliminar producto del carrito
        holder.btnEliminar.setOnClickListener(v -> {
            String token = prefs.getString("token", "");
            int usuarioId = prefs.getInt("user_id", 0);
            int productoId = producto.getId();

            ApiService apiService = RetrofitClient.getApiService();
            apiService.eliminarDelCarrito("Bearer " + token, usuarioId, productoId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                int pos = holder.getAdapterPosition();
                                productos.remove(pos);
                                notifyItemRemoved(pos);
                                Toast.makeText(context, "Producte eliminat", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error en eliminar", Toast.LENGTH_SHORT).show();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;
        TextView tvNombre, tvPrecio, tvCantidad, tvSubtotal;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
