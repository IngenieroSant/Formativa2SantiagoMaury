package com.example.formativa2santiagomaury;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InicioActivity extends AppCompatActivity {

    private EditText edtNombre;
    private Button btnContinuar;
    private MediaPlayer sonidoTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        edtNombre = findViewById(R.id.edtNombre);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setEnabled(false);

        edtNombre.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnContinuar.setEnabled(!s.toString().trim().isEmpty());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnContinuar.setOnClickListener(v -> {
            reproducirSonido();
            String nombre = edtNombre.getText().toString().trim();
            Intent intent = new Intent(InicioActivity.this, MainActivity.class);
            intent.putExtra("nombre_usuario", nombre);
            startActivity(intent);
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
