package com.example.projecte2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Producto> productos;
    private ApiService apiService;
    private Context context;
    private String token;

    public ProductAdapter(Context context, List<Producto> productos, ApiService apiService, String token) {
        this.context = context;
        this.productos = productos;
        this.apiService = apiService;
        this.token = token;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        holder.tvPrecio.setText(String.format("%.2f €", producto.getPrecio()));

        holder.btnEditar.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Editar: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
            // Aquí puedes abrir una nueva actividad o mostrar un diálogo para editar
        });
    
        holder.btnEliminar.setOnClickListener(v -> {
            Log.d("TOKEN_DEBUG", "Token antes de eliminar: " + token);
            String authHeader = "Bearer " + token;

            apiService.eliminarProducto(producto.getId(), authHeader).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("DELETE_DEBUG", "Código de respuesta: " + response.code());

                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Producte eliminat", Toast.LENGTH_SHORT).show();

                        // Buscar el índice del producto por ID
                        int index = -1;
                        for (int i = 0; i < productos.size(); i++) {
                            if (productos.get(i).getId() == producto.getId()) {
                                index = i;
                                break;
                            }
                        }

                        if (index != -1) {
                            productos.remove(index);
                            notifyItemRemoved(index);
                        } else {
                            notifyDataSetChanged(); // fallback
                        }
                    } else {
                        Toast.makeText(context, "Error al eliminar: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Error de connexió: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio;
        Button btnEditar, btnEliminar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
