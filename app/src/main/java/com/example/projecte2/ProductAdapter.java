package com.example.projecte2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        holder.tvPrecio.setText(String.format("%.2f €", producto.getPrecio()));

        holder.btnEditar.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Editar: " + producto.getNombre(), Toast.LENGTH_SHORT).show()
        );

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


        apiService.eliminarProducto(productoId, authHeader).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("RESPONSE_CODE", "Código de respuesta: " + response.code());

                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    ((CatalogActivity) context).recargarDatosDesdeServidor(); // Esto ya se encarga de refrescar el RecyclerView
                } else {
                    Toast.makeText(context, "Error del servidor al eliminar", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR_DELETE", "Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Fallo de conexión", Toast.LENGTH_SHORT).show();
                Log.e("ERROR_DELETE", "onFailure", t);
            }
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

    public void updateData(List<Producto> nuevosProductos) {
        productos.clear();
        productos.addAll(nuevosProductos);
        notifyDataSetChanged();
    }
}
