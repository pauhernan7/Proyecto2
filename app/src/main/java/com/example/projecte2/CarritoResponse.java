package com.example.projecte2;

import java.util.List;

public class CarritoResponse {
    private int id;  // id del carrito
    private List<ItemCarrito> productos;
    private double total;
    private String imagenBase64; // O String urlImagen;


    public CarritoResponse() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<ItemCarrito> getProductos() { return productos; }
    public void setProductos(List<ItemCarrito> productos) { this.productos = productos; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
