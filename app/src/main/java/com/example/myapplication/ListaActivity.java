package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class ListaActivity extends AppCompatActivity {

    PasseioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        dao = new PasseioDAO(this);
        ListView lista = findViewById(R.id.lista);

        Cursor cursor = dao.listar();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{"nome"},
                new int[]{android.R.id.text1},
                0
        );

        lista.setAdapter(adapter);

        lista.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(this, DetalhesActivity.class);
            i.putExtra("id_passeio", id);
            startActivity(i);
        });
    }
}