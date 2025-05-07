package com.example.projecte2;

public class Pedido {
    private int id;
    private Usuario usuario;
    private double total_precio;
    private String fecha_creacion;

    // Getters
    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public double getTotal_precio() { return total_precio; }

    // Solo queremos yyyy-MM-dd
    public String getFechaFormateada() {
        return fecha_creacion != null ? fecha_creacion.substring(0, 10) : "";
    }

    public static class Usuario {
        private String email;
        public String getEmail() { return email; }
    }
}

