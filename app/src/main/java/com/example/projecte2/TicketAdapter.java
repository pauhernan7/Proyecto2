package com.example.projecte2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<TicketResponse> tickets = new ArrayList<>();

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

        // Establecer los datos del ticket
        holder.asuntoText.setText("Assumpte: " + ticket.getAsunto());
        holder.estadoText.setText("Estat: " + ticket.getEstado());
        holder.mensajeText.setText("Missatge: " + ticket.getMensaje());

        // Cambiar el color del estado dependiendo del valor
        if ("resuelto".equals(ticket.getEstado())) {
            holder.estadoText.setTextColor(Color.GREEN);
        } else if ("en_proceso".equals(ticket.getEstado())) {
            holder.estadoText.setTextColor(Color.YELLOW);
        } else {
            holder.estadoText.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView asuntoText, mensajeText, estadoText;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            asuntoText = itemView.findViewById(R.id.ticketAsunto);
            mensajeText = itemView.findViewById(R.id.ticketMensaje);
            estadoText = itemView.findViewById(R.id.ticketEstado);
        }
    }
}
