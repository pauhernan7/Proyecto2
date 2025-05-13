package com.example.projecte2;

public class TicketRequest {
    private String asunto;
    private String mensaje;

    public TicketRequest(String asunto, String mensaje) {
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    // Getters y setters
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

