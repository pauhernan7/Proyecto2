package com.example.projecte2;

public class ItemCarrito {
    private int producto_id;  // antes producto_id, ahora debe ser producto_id para mapear JSON
    private String nombre;
    private double precio;
    private int cantidad;
    private double subtotal;  // agregar para mapear JSON

    // Constructor vacío necesario para Retrofit/Gson
    public ItemCarrito() {}

    public ItemCarrito(int producto_id, int cantidad) {
        this.producto_id = producto_id;
        this.cantidad = cantidad;
    }


    // Getters y setters
    public int getId() { return producto_id; }
    public void setId(int producto_id) { this.producto_id = producto_id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
