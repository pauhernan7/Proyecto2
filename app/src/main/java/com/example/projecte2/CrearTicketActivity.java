package com.example.projecte2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.net.Uri;
public class CrearTicketActivity extends AppCompatActivity
        implements HeaderFragment.OnMenuClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private EditText editTextAsunto, editTextMensaje;
    private Button btnEnviar, btnMoreHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ticket);

        // Configurar el Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtener el header del NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView tvRol = headerView.findViewById(R.id.tvRol);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        tvRol.setText(prefs.getString("rol", "No definido"));
        tvEmail.setText(prefs.getString("email", "correo@ejemplo.com"));

        // Configurar HeaderFragment
        setupHeaderFragment();

        editTextAsunto = findViewById(R.id.etAsunto);
        editTextMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnMoreHelp = findViewById(R.id.btnMoreHelp);

        // Configurar listeners para las preguntas frecuentes
        setupFAQListeners();

        btnEnviar.setOnClickListener(view -> {
            String asunto = editTextAsunto.getText().toString().trim();
            String mensaje = editTextMensaje.getText().toString().trim();

            if (asunto.isEmpty() || mensaje.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarTicket(asunto, mensaje);
        });

        btnMoreHelp.setOnClickListener(v -> {
            // Abrir página web de ayuda
            String url = "https://chatgpt.com"; // Cambia por tu URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }

    private void setupFAQListeners() {
        // Pregunta 1
        View question1 = findViewById(R.id.question1);
        TextView answer1 = findViewById(R.id.tvAnswer1);
        ImageView arrow1 = findViewById(R.id.ivArrow1);

        question1.setOnClickListener(v -> toggleAnswer(answer1, arrow1));

        // Pregunta 2
        View question2 = findViewById(R.id.question2);
        TextView answer2 = findViewById(R.id.tvAnswer2);
        ImageView arrow2 = findViewById(R.id.ivArrow2);

        question2.setOnClickListener(v -> toggleAnswer(answer2, arrow2));

        // Pregunta 3
        View question3 = findViewById(R.id.question3);
        TextView answer3 = findViewById(R.id.tvAnswer3);
        ImageView arrow3 = findViewById(R.id.ivArrow3);

        question3.setOnClickListener(v -> toggleAnswer(answer3, arrow3));

        // Pregunta 4
        View question4 = findViewById(R.id.question4);
        TextView answer4 = findViewById(R.id.tvAnswer4);
        ImageView arrow4 = findViewById(R.id.ivArrow4);

        question4.setOnClickListener(v -> toggleAnswer(answer4, arrow4));
    }

    private void toggleAnswer(TextView answer, ImageView arrow) {
        if (answer.getVisibility() == View.VISIBLE) {
            answer.setVisibility(View.GONE);
            arrow.animate().rotation(0).setDuration(200).start(); // Vuelve a posición original
        } else {
            answer.setVisibility(View.VISIBLE);
            arrow.animate().rotation(90).setDuration(200).start(); // Rota 90 grados hacia abajo
        }
    }

    private void setupHeaderFragment() {
        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.headerFragment);

        if (headerFragment != null) {
            headerFragment.setOnMenuClickListener(this);
            headerFragment.setTitle("Crear Ticket");
        }
    }

    private void enviarTicket(String asunto, String mensaje) {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = RetrofitClient.getApiService();
        TicketRequest request = new TicketRequest(asunto, mensaje);
        Call<TicketResponse> call = api.crearTicket(request, "Bearer " + token);

        call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearTicketActivity.this, "Ticket creado correctamente", Toast.LENGTH_LONG).show();
                    editTextAsunto.setText("");
                    editTextMensaje.setText("");
                } else {
                    Toast.makeText(CrearTicketActivity.this, "Error al crear el ticket", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(CrearTicketActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMenuClick() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {
            startActivity(new Intent(this, ClienteCatalogActivity.class));
            finish();
        } else if (id == R.id.nav_carrito) {
            startActivity(new Intent(this, CarritoActivity.class));
            finish();
        } else if (id == R.id.nav_support) {
            Toast.makeText(this, "Ja ets a Suport", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.logout) {
            getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}