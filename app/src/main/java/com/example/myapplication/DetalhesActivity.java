package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class DetalhesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private long idPasseio;
    PasseioDAO dao;
    ArrayList<Ponto> pontos;

    TextView txtNome, txtTipo, txtDescricao, txtInicio, txtFim, txtPontos;
    Button btnGoogleMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        dao = new PasseioDAO(this);
        idPasseio = getIntent().getLongExtra("id_passeio", -1);

        txtNome      = findViewById(R.id.txtNome);
        txtTipo      = findViewById(R.id.txtTipo);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtInicio    = findViewById(R.id.txtInicio);
        txtFim       = findViewById(R.id.txtFim);
        txtPontos    = findViewById(R.id.txtPontos);
        btnGoogleMaps = findViewById(R.id.btnGoogleMaps);

        carregarDados();

        btnGoogleMaps.setOnClickListener(v -> abrirGoogleMaps());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapDetalhes);

        mapFragment.getMapAsync(this);
    }

    private void carregarDados() {
        if (idPasseio == -1) return;

        Cursor c = dao.buscarPasseio(idPasseio);
        if (c != null && c.moveToFirst()) {
            txtNome.setText(c.getString(c.getColumnIndexOrThrow("nome")));
            txtTipo.setText("Tipo: " + c.getString(c.getColumnIndexOrThrow("tipo")));
            txtDescricao.setText("Descrição: " + c.getString(c.getColumnIndexOrThrow("descricao")));
            txtInicio.setText("Início: " + c.getString(c.getColumnIndexOrThrow("inicio")));
            txtFim.setText("Fim: " + c.getString(c.getColumnIndexOrThrow("fim")));
            c.close();
        }

        pontos = dao.listarPontos(idPasseio);
        txtPontos.setText("Pontos registrados: " + pontos.size());
    }

    private void abrirGoogleMaps() {
        if (pontos == null || pontos.isEmpty()) {
            Toast.makeText(this, "Nenhum ponto registrado neste passeio.", Toast.LENGTH_SHORT).show();
            return;
        }

        Ponto inicio = pontos.get(0);
        Ponto fim    = pontos.get(pontos.size() - 1);

        StringBuilder waypoints = new StringBuilder();
        int total = pontos.size();
        int step  = Math.max(1, total / 10);

        for (int i = step; i < total - 1; i += step) {
            if (waypoints.length() > 0) waypoints.append("|");
            waypoints.append(pontos.get(i).lat).append(",").append(pontos.get(i).lon);
        }

        String url;
        if (waypoints.length() > 0) {
            url = "https://www.google.com/maps/dir/?api=1"
                    + "&origin=" + inicio.lat + "," + inicio.lon
                    + "&destination=" + fim.lat + "," + fim.lon
                    + "&waypoints=" + waypoints
                    + "&travelmode=walking";
        } else {
            url = "https://www.google.com/maps/dir/?api=1"
                    + "&origin=" + inicio.lat + "," + inicio.lon
                    + "&destination=" + fim.lat + "," + fim.lon
                    + "&travelmode=walking";
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (pontos == null || pontos.isEmpty()) return;

        PolylineOptions linha = new PolylineOptions()
                .color(0xFF2196F3)
                .width(8f);

        for (Ponto p : pontos) {
            linha.add(new LatLng(p.lat, p.lon));
        }

        mMap.addPolyline(linha);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(pontos.get(0).lat, pontos.get(0).lon))
                .title("Início"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        pontos.get(pontos.size() - 1).lat,
                        pontos.get(pontos.size() - 1).lon))
                .title("Fim"));

        LatLng inicio = new LatLng(pontos.get(0).lat, pontos.get(0).lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio, 15));
    }
}