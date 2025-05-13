package com.example.projecte2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvCantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
        }
    }
}
