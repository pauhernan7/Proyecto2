package com.example.projecte2;

import java.util.List;

public class CarritoResponse {
    private List<ItemCarrito> productos;

    public List<ItemCarrito> getProductos() {
        return productos;
    }

    public void setProductos(List<ItemCarrito> productos) {
        this.productos = productos;
    }
}
