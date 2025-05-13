package com.example.projecte2;

public class TicketResponse {
    private int id;
    private String asunto;
    private String mensaje;
    private String estado;
    private String respuesta;
    private String fecha_creacion;
    private String fecha_actualizacion;
    private int usuario; // o un objeto, según tu serializer

    // Getters (añade setters si necesitas)
    public int getId() { return id; }
    public String getAsunto() { return asunto; }
    public String getMensaje() { return mensaje; }
    public String getEstado() { return estado; }
    public String getRespuesta() { return respuesta; }
    public String getFecha_creacion() { return fecha_creacion; }
    public String getFecha_actualizacion() { return fecha_actualizacion; }
    public int getUsuario() { return usuario; }
}


