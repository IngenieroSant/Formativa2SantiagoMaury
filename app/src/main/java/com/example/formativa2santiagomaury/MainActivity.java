package com.example.formativa2santiagomaury;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvSaludo;
    private Button btnFibonacci, btnEcuacion, btnOrdenar, btnPuzzle;
    private MediaPlayer sonidoTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSaludo = findViewById(R.id.tvSaludo);
        btnFibonacci = findViewById(R.id.btnFibonacci);
        btnEcuacion = findViewById(R.id.btnEcuacion);
        btnOrdenar = findViewById(R.id.btnOrdenar);
        btnPuzzle = findViewById(R.id.btnPuzzle);

        // Obtener el nombre enviado desde la actividad anterior
        String nombre = getIntent().getStringExtra("nombre_usuario");
        tvSaludo.setText("¡Hola, " + nombre + "!");

        btnFibonacci.setOnClickListener(v -> {
            reproducirSonido();
            startActivity(new Intent(MainActivity.this, FibonacciActivity.class));
        });

        btnEcuacion.setOnClickListener(v -> {
            reproducirSonido();
            startActivity(new Intent(MainActivity.this, EcuacionActivity.class));
        });

        btnOrdenar.setOnClickListener(v -> {
            reproducirSonido();
            startActivity(new Intent(MainActivity.this, OrdenarActivity.class));
        });

        btnPuzzle.setOnClickListener(v -> {
            reproducirSonido();
            startActivity(new Intent(MainActivity.this, PuzzleActivity.class));
        });
    }

    private void reproducirSonido() {
        if (sonidoTouch != null) {
            sonidoTouch.release();
        }
        sonidoTouch = MediaPlayer.create(this, R.raw.touch);
        sonidoTouch.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sonidoTouch != null) {
            sonidoTouch.release();
            sonidoTouch = null;
        }
    }
}
