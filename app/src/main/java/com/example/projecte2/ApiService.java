package com.example.projecte2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/login/")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/registro/")  // Asegúrate de que esta ruta sea correcta según tu servidor
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("api/app/productos-vendidos/")
    Call<List<ProductoVendido>> getProductosVendidos(@Header("Authorization") String token);

    @GET("api/app/pedidos/ultimos/")
    Call<List<Order>> getUltimosOrders(@Header("Authorization") String token);

}

