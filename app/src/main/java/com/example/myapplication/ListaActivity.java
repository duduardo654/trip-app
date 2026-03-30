package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.ListView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    PasseioDAO dao;
    ArrayList<Passeio> lista;
    PasseioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        dao = new PasseioDAO(this);
        ListView listView = findViewById(R.id.lista);

        lista   = dao.listarPasseios();
        adapter = new PasseioAdapter(this, lista);
        listView.setAdapter(adapter);

        listView.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_UP) return false;

            int x = (int) event.getX();
            int y = (int) event.getY();

            int position = listView.pointToPosition(x, y);
            if (position == ListView.INVALID_POSITION) return false;

            View itemView = listView.getChildAt(
                    position - listView.getFirstVisiblePosition()
            );
            if (itemView == null) return false;

            CheckBox chk = itemView.findViewById(R.id.chkPasseio);
            if (chk == null) return false;

            int[] chkLoc   = new int[2];
            int[] listLoc  = new int[2];
            chk.getLocationOnScreen(chkLoc);
            listView.getLocationOnScreen(listLoc);

            int chkLeft   = chkLoc[0] - listLoc[0];
            int chkTop    = chkLoc[1] - listLoc[1];
            int chkRight  = chkLeft + chk.getWidth();
            int chkBottom = chkTop  + chk.getHeight();

            if (x >= chkLeft && x <= chkRight && y >= chkTop && y <= chkBottom) {
                Passeio p = lista.get(position);
                p.selecionado = !p.selecionado;
                adapter.notifyDataSetChanged();
                return true;
            }

            return false;
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Passeio p = lista.get(position);
            Intent i = new Intent(this, DetalhesActivity.class);
            i.putExtra("id_passeio", p.id);
            startActivity(i);
        });
    }
}