package com.example.projecte2;

public class Order {
    private int id;
    private Usuario usuario;
    private double total_precio;
    private String fecha_creacion;
    private String estado;

    // Getters
    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public double getTotal_precio() { return total_precio; }
    public String getEstado() { return estado; }

    //Setters
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Solo queremos yyyy-MM-dd
    public String getFechaFormateada() {
        return fecha_creacion != null ? fecha_creacion.substring(0, 10) : "";
    }

    public static class Usuario {
        private String email;
        public String getEmail() { return email; }
    }
}

