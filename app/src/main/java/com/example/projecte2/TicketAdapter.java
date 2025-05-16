package com.example.projecte2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<TicketResponse> tickets = new ArrayList<>();
    private Context context;

    public TicketAdapter(Context context) {
        this.context = context;
    }

    public void setTickets(List<TicketResponse> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketResponse ticket = tickets.get(position);

        holder.asuntoText.setText("Assumpte: " + ticket.getAsunto());
        holder.estadoText.setText("Estat: " + ticket.getEstado());
        holder.mensajeText.setText("Missatge: " + ticket.getMensaje());
        holder.respuestaText.setText("Resposta: " + ticket.getRespuesta());

        // Colores por estado
        switch (ticket.getEstado()) {
            case "resuelto":
                holder.estadoText.setTextColor(Color.GREEN);
                break;
            case "en_proceso":
                holder.estadoText.setTextColor(Color.YELLOW);
                break;
            default:
                holder.estadoText.setTextColor(Color.RED);
                break;
        }

        // Si es admin, permitir actualizar
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String rol = prefs.getString("rol", "");
        if ("admin".equalsIgnoreCase(rol)) {
            holder.estadoText.setOnClickListener(v -> mostrarDialogoActualizarTicket(ticket, position));
        }
    }

    private void mostrarDialogoActualizarTicket(TicketResponse ticket, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Actualitzar ticket");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_actualizar_ticket, null);
        final Spinner spinnerEstado = viewInflated.findViewById(R.id.spinnerEstado);
        final EditText inputRespuesta = viewInflated.findViewById(R.id.inputRespuesta);

        // Spinner opciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.estados_ticket, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        builder.setView(viewInflated);

        builder.setPositiveButton("Actualitzar", (dialog, which) -> {
            String nuevoEstado = spinnerEstado.getSelectedItem().toString();
            String nuevaRespuesta = inputRespuesta.getText().toString();
            actualizarTicket(ticket.getId(), nuevoEstado, nuevaRespuesta, position);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void actualizarTicket(int ticketId, String estado, String respuesta, int position) {
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        ApiService apiService = RetrofitClient.getApiService();
        TicketUpdateRequest updateRequest = new TicketUpdateRequest(
                estado,
                respuesta,
                tickets.get(position).getAsunto(),
                tickets.get(position).getMensaje()
        );

        apiService.actualizarTicket(ticketId, updateRequest, "Bearer " + token).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if (response.isSuccessful()) {
                    TicketResponse actualizado = response.body();
                    tickets.set(position, actualizado);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Ticket actualitzat correctament", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al actualitzar", Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API", "Error: " + errorBody);
                        Toast.makeText(context, "Error: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(context, "Error de connexió", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView asuntoText, mensajeText, estadoText, respuestaText;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            asuntoText = itemView.findViewById(R.id.ticketAsunto);
            mensajeText = itemView.findViewById(R.id.ticketMensaje);
            estadoText = itemView.findViewById(R.id.ticketEstado);
            respuestaText = itemView.findViewById(R.id.ticketRespuesta);
        }
    }
}
