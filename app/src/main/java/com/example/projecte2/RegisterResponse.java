package com.example.projecte2;

public class RegisterResponse {

    private String mensaje;
    private int user_id;

    // Constructor
    public RegisterResponse(String mensaje, int user_id) {
        this.mensaje = mensaje;
        this.user_id = user_id;
    }

    // Getters y setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }
}
