package com.example.projecte2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext(); // Para usar en el diálogo
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("#" + order.getId());
        holder.tvDate.setText(order.getFechaFormateada());
        holder.tvUserEmail.setText(order.getUsuario().getEmail());
        holder.tvTotalPrice.setText(order.getTotal_precio() + "€");
        holder.tvOrderStatus.setText(order.getEstado());

        holder.tvOrderStatus.setOnClickListener(v -> {
            String currentStatus = order.getEstado();
            showChangeStatusDialog(order.getId(), currentStatus);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvUserEmail, tvTotalPrice, tvOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvTotalPrice = itemView.findViewById(R.id.tvOrderAmount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }

    // ===========================
    // Método para cambiar estado
    // ===========================
    private void showChangeStatusDialog(int orderId, String currentStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cambiar estado del pedido");

        String[] estados = {"pendiente", "enviado", "entregado", "cancelado"};

        builder.setItems(estados, (dialog, which) -> {
            String nuevoEstado = estados[which];

            SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
            String token = prefs.getString("token", "");

            ApiService apiService = RetrofitClient.getApiService();

            apiService.updateOrderStatus(orderId, nuevoEstado, "Bearer " + token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Estado actualizado a " + nuevoEstado, Toast.LENGTH_SHORT).show();
                        // Actualizar localmente y refrescar
                        for (Order o : orders) {
                            if (o.getId() == orderId) {
                                o.setEstado(nuevoEstado);
                                break;
                            }
                        }
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error al actualizar estado: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Fallo de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

}
