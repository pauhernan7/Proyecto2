package com.example.projecte2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TicketClienteAdapter extends RecyclerView.Adapter<TicketClienteAdapter.TicketViewHolder> {

    private List<TicketResponse> tickets = new ArrayList<>();
    private Context context;

    public TicketClienteAdapter(Context context) {
        this.context = context;
    }

    public void setTickets(List<TicketResponse> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket_cliente, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketResponse ticket = tickets.get(position);
        holder.asunto.setText("Assumpte: " + ticket.getAsunto());
        holder.estado.setText("Estat: " + ticket.getEstadoDisplay());
        holder.mensaje.setText("Missatge: " + ticket.getMensaje());
        holder.respuesta.setText("Resposta: " + (ticket.getRespuesta() != null ? ticket.getRespuesta() : "Encara sense resposta"));

        switch (ticket.getEstado()) {
            case "resuelto":
                holder.estado.setTextColor(Color.GREEN);
                break;
            case "pendiente":
                holder.estado.setTextColor(Color.RED);
                break;
            case "en_proceso":
                holder.estado.setTextColor(Color.YELLOW);
                break;
            default:
                holder.estado.setTextColor(Color.GRAY);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView asunto, estado, mensaje, respuesta;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            asunto = itemView.findViewById(R.id.tvAsunto);
            estado = itemView.findViewById(R.id.tvEstado);
            mensaje = itemView.findViewById(R.id.tvMensaje);
            respuesta = itemView.findViewById(R.id.tvRespuesta);
        }
    }
}
