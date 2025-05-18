package com.example.projecte2;

import com.google.gson.annotations.SerializedName;

public class TicketResponse {
    private int id;
    private int usuario;

    @SerializedName("usuario_email")
    private String usuarioEmail;

    private String asunto;
    private String mensaje;
    private String estado;

    @SerializedName("estado_display")
    private String estadoDisplay;

    private String respuesta;

    @SerializedName("fecha_creacion")
    private String fechaCreacion;

    @SerializedName("fecha_actualizacion")
    private String fechaActualizacion;

    // Getters
    public int getId() { return id; }
    public int getUsuario() { return usuario; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public String getAsunto() { return asunto; }
    public String getMensaje() { return mensaje; }
    public String getEstado() { return estado; }
    public String getEstadoDisplay() { return estadoDisplay; }
    public String getRespuesta() { return respuesta; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaActualizacion() { return fechaActualizacion; }
}
