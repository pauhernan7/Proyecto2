package com.example.projecte2;

public class ItemCarrito {
    private int producto_id;
    private String nombre;
    private double precio;
    private int cantidad;


    public ItemCarrito(int productoId, int cantidad) {
        this.producto_id = productoId;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getProducto_id() { return producto_id; }
    public void setProducto_id(int producto_id) { this.producto_id = producto_id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
