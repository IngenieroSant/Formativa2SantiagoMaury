package com.example.formativa2santiagomaury;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class PuzzleActivity extends AppCompatActivity {

    private GridLayout gridPuzzle;
    private TextView tvGanaste;
    private TextView tvEstadisticas;
    private Button btnReiniciar, btnVerEstadisticas, btnReiniciarEstadisticas, btnMusica;
    private ArrayList<Button> botones = new ArrayList<>();
    private ArrayList<String> numeros;

    private int movimientos = 0;
    private final int MAX_MOVIMIENTOS = 200;
    private boolean juegoActivo = true;

    private MediaPlayer musicaFondo;
    private MediaPlayer sonidoMovimiento;

    private SharedPreferences prefs;
    private int recordMovimientos;
    private int victorias;
    private int derrotas;

    private boolean musicaActiva = true;
    private boolean estadisticasVisibles = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        gridPuzzle = findViewById(R.id.gridPuzzle);
        tvGanaste = findViewById(R.id.tvGanaste);
        tvEstadisticas = findViewById(R.id.tvEstadisticas);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnVerEstadisticas = findViewById(R.id.btnVerEstadisticas);
        btnReiniciarEstadisticas = findViewById(R.id.btnReiniciarEstadisticas);
        btnMusica = findViewById(R.id.btnMusica);

        prefs = getSharedPreferences("puzzle_stats", MODE_PRIVATE);
        recordMovimientos = prefs.getInt("record", 0);
        victorias = prefs.getInt("victorias", 0);
        derrotas = prefs.getInt("derrotas", 0);

        // Ocultar estadísticas al iniciar
        tvEstadisticas.setVisibility(View.GONE);

        musicaFondo = MediaPlayer.create(this, R.raw.gamemusic);
        musicaFondo.setLooping(true);
        musicaFondo.start();

        iniciarJuego();

        btnReiniciar.setOnClickListener(v -> {
            reproducirSonidoTouch();
            iniciarJuego();
        });

        btnVerEstadisticas.setOnClickListener(v -> {
            reproducirSonidoTouch();
            estadisticasVisibles = !estadisticasVisibles;
            if (estadisticasVisibles) {
                mostrarEstadisticas();
                btnVerEstadisticas.setText("Ocultar Estadísticas");
            } else {
                tvEstadisticas.setVisibility(View.GONE);
                btnVerEstadisticas.setText("Ver Estadísticas");
            }
        });

        btnReiniciarEstadisticas.setOnClickListener(v -> {
            reproducirSonidoTouch();
            recordMovimientos = 0;
            victorias = 0;
            derrotas = 0;
            prefs.edit().clear().apply();
            tvEstadisticas.setVisibility(View.GONE);
            btnVerEstadisticas.setText("Ver Estadísticas");
            estadisticasVisibles = false;
        });

        // BOTÓN DE MÚSICA
        btnMusica.setOnClickListener(v -> {
            reproducirSonidoTouch();
            if (musicaFondo != null) {
                if (musicaFondo.isPlaying()) {
                    musicaFondo.pause();
                    btnMusica.setText("🔇");
                    musicaActiva = false;
                } else {
                    musicaFondo.start();
                    btnMusica.setText("🔊");
                    musicaActiva = true;
                }
            }
        });
    }

    private void iniciarJuego() {
        gridPuzzle.removeAllViews();
        botones.clear();
        tvGanaste.setText("");
        movimientos = 0;
        juegoActivo = true;
        btnReiniciar.setVisibility(View.GONE);

        numeros = new ArrayList<>();
        for (int i = 1; i <= 8; i++) numeros.add(String.valueOf(i));
        numeros.add("");
        Collections.shuffle(numeros);

        for (int i = 0; i < 9; i++) {
            Button btn = new Button(this);
            btn.setText(numeros.get(i));
            btn.setTextSize(24);
            btn.setWidth(200);
            btn.setHeight(200);
            btn.setOnClickListener(this::mover);
            botones.add(btn);
            gridPuzzle.addView(btn);
        }
    }

    private void mover(View view) {
        if (!juegoActivo) return;

        Button btnClic = (Button) view;
        int indexClic = botones.indexOf(btnClic);
        int indexVacio = buscarVacio();

        if (esMovible(indexClic, indexVacio)) {
            String texto = btnClic.getText().toString();
            btnClic.setText("");
            botones.get(indexVacio).setText(texto);

            if (sonidoMovimiento != null) sonidoMovimiento.release();
            sonidoMovimiento = MediaPlayer.create(this, R.raw.puzzle);
            sonidoMovimiento.start();

            movimientos++;

            if (verificarGanador()) {
                juegoActivo = false;
                int puntaje = Math.max(1000 - (movimientos * 10), 100);
                String mensaje = "🎉 ¡Ganaste!\n\nMovimientos: " + movimientos + "\nPuntaje: " + puntaje;

                if (recordMovimientos == 0 || movimientos < recordMovimientos) {
                    recordMovimientos = movimientos;
                    prefs.edit().putInt("record", movimientos).apply();
                    mensaje += "\n✨ ¡Nuevo récord!";
                }
                victorias++;
                prefs.edit().putInt("victorias", victorias).apply();

                tvGanaste.setText(mensaje);

                MediaPlayer mp = MediaPlayer.create(this, R.raw.ganar);
                mp.start();
                mp.setOnCompletionListener(MediaPlayer::release);
            } else if (movimientos >= MAX_MOVIMIENTOS) {
                juegoActivo = false;
                tvGanaste.setText("💥 ¡Perdiste! 🥲 \nMovimientos máximos alcanzados.");
                btnReiniciar.setVisibility(View.VISIBLE);

                derrotas++;
                prefs.edit().putInt("derrotas", derrotas).apply();

                MediaPlayer mp = MediaPlayer.create(this, R.raw.game_over);
                mp.start();
                mp.setOnCompletionListener(MediaPlayer::release);
            } else {
                tvGanaste.setText("Movimientos: " + movimientos);
            }
        }
    }

    private int buscarVacio() {
        for (int i = 0; i < botones.size(); i++) {
            if (botones.get(i).getText().toString().equals("")) {
                return i;
            }
        }
        return -1;
    }

    private boolean esMovible(int clic, int vacio) {
        return (Math.abs(clic - vacio) == 1 && clic / 3 == vacio / 3) || Math.abs(clic - vacio) == 3;
    }

    private boolean verificarGanador() {
        for (int i = 0; i < 8; i++) {
            if (!botones.get(i).getText().toString().equals(String.valueOf(i + 1))) return false;
        }
        return botones.get(8).getText().toString().equals("");
    }

    private void mostrarEstadisticas() {
        String texto = "📊 Estadísticas:\n\n" +
                "📈Récord de movimientos: " + (recordMovimientos == 0 ? "N/A" : recordMovimientos) + "\n" +
                "🏆Victorias: " + victorias + "\n" +
                "💥Derrotas: " + derrotas;

        tvEstadisticas.setText(texto);
        tvEstadisticas.setVisibility(View.VISIBLE);
    }

    private void reproducirSonidoTouch() {
        MediaPlayer touch = MediaPlayer.create(this, R.raw.touch);
        touch.start();
        touch.setOnCompletionListener(MediaPlayer::release);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicaFondo != null) {
            musicaFondo.stop();
            musicaFondo.release();
        }
        if (sonidoMovimiento != null) {
            sonidoMovimiento.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (musicaFondo != null && musicaFondo.isPlaying()) {
            musicaFondo.pause();
            if (btnMusica != null) btnMusica.setText("🔇");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicaFondo != null && musicaActiva && !musicaFondo.isPlaying()) {
            musicaFondo.start();
            if (btnMusica != null) btnMusica.setText("🔊");
        }
    }
}
