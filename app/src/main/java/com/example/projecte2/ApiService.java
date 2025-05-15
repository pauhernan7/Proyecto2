package com.example.projecte2;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/api/login/")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/registro/")  // Asegúrate de que esta ruta sea correcta según tu servidor
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("api/app/productos-vendidos/")
    Call<List<ProductoVendido>> getProductosVendidos(@Header("Authorization") String token);

    @GET("api/app/pedidos/ultimos/")
    Call<List<Order>> getUltimosOrders(@Header("Authorization") String token);

    @GET("api/app/productos/")
    Call<List<Producto>> listarProductos();

    @POST("api/app/productos/crear/")
    Call<Producto> crearProducto(@Body Producto producto, @Header("Authorization") String token);

    @PUT("api/app/productos/{id}/")
    Call<Producto> actualizarProducto(@Path("id") int id, @Body Producto producto, @Header("Authorization") String token);

    @DELETE("api/app/productos/{id}/")
    Call<ResponseBody> eliminarProducto(
            @Path("id") int id,
            @Header("Authorization") String authToken
    );

    @POST("api/app/productos/crear/")
    Call<Producto> crearProducto(
            @Header("Authorization") String token,
            @Body Producto producto
    );

    @POST("api/carrito/añadir/{usuario_id}/{tienda_id}/")
    Call<Void> agregarAlCarrito(
            @Header("Authorization") String token,  // Para la autorización con Bearer
            @Path("usuario_id") int usuarioId,  // El ID del usuario
            @Path("tienda_id") int tiendaId,  // El ID de la tienda
            @Body ItemCarrito item  // El producto que se va a añadir al carrito
    );


    @GET("api/carrito/{usuario_id}/")
    Call<CarritoResponse> verCarrito(@Path("usuario_id") int usuarioId, @Header("Authorization") String authHeader);



    @POST("api/soporte/")
    Call<TicketResponse> crearTicket(
            @Body TicketRequest ticket,
            @Header("Authorization") String token
    );

    @GET("api/soporte/")
    Call<List<TicketResponse>> getTodosLosTickets(@Header("Authorization") String token);


}

