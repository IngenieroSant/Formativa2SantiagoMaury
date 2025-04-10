package com.example.formativa2santiagomaury;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FibonacciActivity extends AppCompatActivity {

    private EditText etCantidad;
    private Button btnGenerar;
    private TextView tvResultado;
    private MediaPlayer sonidoTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);

        etCantidad = findViewById(R.id.etCantidad);
        btnGenerar = findViewById(R.id.btnGenerar);
        tvResultado = findViewById(R.id.tvResultado);

        btnGenerar.setOnClickListener(v -> {
            reproducirSonido();

            String input = etCantidad.getText().toString().trim();
            if (!input.isEmpty()) {
                int n = Integer.parseInt(input);
                StringBuilder resultado = new StringBuilder();
                int a = 0, b = 1;
                for (int i = 0; i < n; i++) {
                    resultado.append(a).append(i < n - 1 ? ", " : "");
                    int temp = a + b;
                    a = b;
                    b = temp;
                }
                tvResultado.setText(resultado.toString());
            } else {
                tvResultado.setText("Por favor ingresa un número.");
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
