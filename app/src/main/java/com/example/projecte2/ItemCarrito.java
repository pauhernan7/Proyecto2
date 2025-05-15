package com.example.projecte2;

public class ItemCarrito {
    private int id;  // antes producto_id, ahora debe ser id para mapear JSON
    private String nombre;
    private double precio;
    private int cantidad;
    private double subtotal;  // agregar para mapear JSON

    // Constructor vacío necesario para Retrofit/Gson
    public ItemCarrito() {}

    public ItemCarrito(int id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }


    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
