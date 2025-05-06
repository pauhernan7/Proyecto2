package com.example.projecte2;

public class RegisterRequest {

    private String email;
    private String username;
    private String password;
    private String rol;

    // Constructor
    public RegisterRequest(String email, String username, String password, String rol) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

