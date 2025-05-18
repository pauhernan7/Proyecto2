package com.example.projecte2;

import com.google.gson.annotations.SerializedName;

public class ItemCarrito {
    @SerializedName(value = "producto_id", alternate = {"id"})
    private int producto_id;
    private String nombre;
    private double precio;
    private int cantidad;
    private double subtotal;
    private String imagenBase64;




    // Constructor vacío necesario para Retrofit/Gson
    public ItemCarrito() {}

    public ItemCarrito(int producto_id, int cantidad) {
        this.producto_id = producto_id;
        this.cantidad = cantidad;
    }


    // Getters y setters
    public int getId() { return this.producto_id; }
    public void setId(int producto_id) { this.producto_id = producto_id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }
}
