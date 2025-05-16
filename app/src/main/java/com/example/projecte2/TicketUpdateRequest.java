package com.example.projecte2;

public class TicketUpdateRequest {
    private String estado;
    private String respuesta;
    private String asunto;
    private String mensaje;

    public TicketUpdateRequest(String estado, String respuesta, String asunto, String mensaje) {
        this.estado = estado;
        this.respuesta = respuesta;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getMensaje() {
        return mensaje;
    }
}


