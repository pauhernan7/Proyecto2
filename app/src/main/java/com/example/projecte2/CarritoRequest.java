package com.example.projecte2;

public class CarritoRequest {
    private int producto_id;
    private int cantidad;

    public CarritoRequest(int producto_id, int cantidad) {
        this.producto_id = producto_id;
        this.cantidad = cantidad;
    }
}

