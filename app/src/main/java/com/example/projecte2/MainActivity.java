package com.example.projecte2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button masSobreNosotros, registerButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Referencias a las vistas
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        masSobreNosotros = findViewById(R.id.aboutUsButton);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Click en el botón de "Mas Sobre Nosotros"
        masSobreNosotros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MasSobreNosotros.class);
                startActivity(intent);
            }
        });

        // Click en el botón de "Registrarse"
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Click en el botón de "Login"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    realizarLogin(email, password);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, ingresa tu email y contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para realizar el login utilizando Retrofit
    private void realizarLogin(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        ApiService api = RetrofitClient.getApiService();

        Call<LoginResponse> call = api.login(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse login = response.body();

                    // Guardar el token y la información del usuario
                    saveUserData(login);

                    // Redirigir según el rol del usuario
                    if (login.getRol().equals("admin")) {
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, CatalogoClienteActivity.class);
                        startActivity(intent);
                    }
                    finish();  // Cerrar MainActivity
                } else {
                    Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para guardar los datos del usuario (token, user_id, etc.)
    private void saveUserData(LoginResponse loginResponse) {
        getSharedPreferences("user_data", MODE_PRIVATE)
                .edit()
                .putString("token", loginResponse.getToken())
                .putString("refresh", loginResponse.getRefresh())
                .putInt("user_id", loginResponse.getUserId())
                .putString("email", loginResponse.getEmail())
                .putString("rol", loginResponse.getRol())
                .apply();

    }
}
