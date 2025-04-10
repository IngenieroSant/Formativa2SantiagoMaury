package com.example.formativa2santiagomaury;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class OrdenarActivity extends AppCompatActivity {

    private EditText etNumeros;
    private RadioButton rbAscendente, rbDescendente;
    private Button btnOrdenar;
    private TextView tvResultado;
    private MediaPlayer sonidoTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenar);

        etNumeros = findViewById(R.id.etNumeros);
        rbAscendente = findViewById(R.id.rbAscendente);
        rbDescendente = findViewById(R.id.rbDescendente);
        btnOrdenar = findViewById(R.id.btnOrdenar);
        tvResultado = findViewById(R.id.tvResultado);

        btnOrdenar.setOnClickListener(v -> {
            reproducirSonido(); // 🎵 Reproduce sonido al presionar

            String input = etNumeros.getText().toString().trim();
            if (input.isEmpty()) {
                tvResultado.setText("Por favor ingresa números.");
                return;
            }

            try {
                String[] partes = input.split(",");
                List<Double> numeros = new ArrayList<>();
                for (String parte : partes) {
                    numeros.add(Double.parseDouble(parte.trim()));
                }

                if (rbAscendente.isChecked()) {
                    Collections.sort(numeros);
                } else {
                    numeros.sort(Collections.reverseOrder());
                }

                tvResultado.setText("Resultado:\n" + numeros.toString());

            } catch (NumberFormatException e) {
                tvResultado.setText("Error: Ingresa solo números válidos separados por comas.");
            }
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
