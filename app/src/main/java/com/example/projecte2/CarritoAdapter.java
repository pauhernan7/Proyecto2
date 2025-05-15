package com.example.projecte2;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

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

        holder.tvNombre.setText("Nom: " + producto.getNombre());
        holder.tvPrecio.setText("Preu: " + producto.getPrecio() + "€");
        holder.tvCantidad.setText("Quantitat: " + producto.getCantidad());
        holder.tvSubtotal.setText("Subtotal: " + producto.getSubtotal() + "€");

        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String imagenBase64 = prefs.getString("imagen_producto_" + producto.getId(), null);

        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            String url = "data:image/jpeg;base64," + imagenBase64;
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.logo_empresa)
                    .into(holder.ivProducto);
        } else {
            holder.ivProducto.setImageResource(R.drawable.logo_empresa);
        }

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;  // nuevo
        TextView tvNombre, tvPrecio, tvCantidad, tvSubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
        }
    }
}
