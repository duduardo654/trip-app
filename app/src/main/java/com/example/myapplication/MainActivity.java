package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtNome, edtDescricao, edtInicio, edtFim;
    Spinner spnTipo;
    Button btnIniciar, btnLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNome      = findViewById(R.id.edtNome);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtInicio    = findViewById(R.id.edtInicio);
        edtFim       = findViewById(R.id.edtFim);
        spnTipo      = findViewById(R.id.spnTipo);
        btnIniciar   = findViewById(R.id.btnIniciar);
        btnLista     = findViewById(R.id.btnLista);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipos_passeio,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipo.setAdapter(adapter);

        btnIniciar.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("nome",   edtNome.getText().toString());
            i.putExtra("tipo",   spnTipo.getSelectedItem().toString());
            i.putExtra("desc",   edtDescricao.getText().toString());
            i.putExtra("inicio", edtInicio.getText().toString());
            i.putExtra("fim",    edtFim.getText().toString());
            startActivity(i);
        });

        btnLista.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ListaActivity.class);
            startActivity(i);
        });
    }
}