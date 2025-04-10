package com.example.formativa2santiagomaury;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EcuacionActivity extends AppCompatActivity {

    private EditText etA, etB, etC;
    private Button btnCalcular;
    private TextView tvResultado;
    private MediaPlayer sonidoTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecuacion);

        etA = findViewById(R.id.etA);
        etB = findViewById(R.id.etB);
        etC = findViewById(R.id.etC);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);

        btnCalcular.setOnClickListener(v -> {
            reproducirSonido();

            try {
                double a = Double.parseDouble(etA.getText().toString());
                double b = Double.parseDouble(etB.getText().toString());
                double c = Double.parseDouble(etC.getText().toString());

                double discriminante = b * b - 4 * a * c;

                if (a == 0) {
                    tvResultado.setText("a no puede ser 0.");
                } else if (discriminante >= 0) {
                    double x1 = (-b + Math.sqrt(discriminante)) / (2 * a);
                    double x2 = (-b - Math.sqrt(discriminante)) / (2 * a);
                    String res = "x1 = " + String.format("%.3f", x1) +
                            "\nx2 = " + String.format("%.3f", x2);
                    tvResultado.setText(res);
                } else {
                    double real = -b / (2 * a);
                    double imag = Math.sqrt(-discriminante) / (2 * a);
                    String res = "x1 = " + String.format("%.3f", real) + " + " + String.format("%.3f", imag) + "i" +
                            "\nx2 = " + String.format("%.3f", real) + " - " + String.format("%.3f", imag) + "i";
                    tvResultado.setText(res);
                }

            } catch (Exception e) {
                tvResultado.setText("Error: verifica los datos.");
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
