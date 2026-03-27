package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Passeio passeio = new Passeio();
    PasseioDAO dao;
    private GoogleMap mMap;

    ArrayList<Ponto> listaPontos = new ArrayList<>();

    Button btnFinalizar;
    boolean gravando = true;

    private FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;

    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dao = new PasseioDAO(this);

        btnFinalizar = findViewById(R.id.btnFinalizar);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            passeio.nome      = b.getString("nome");
            passeio.tipo      = b.getString("tipo");
            passeio.descricao = b.getString("desc");
            passeio.inicio    = b.getString("inicio");
            passeio.fim       = b.getString("fim");
        }

        btnFinalizar.setOnClickListener(v -> {
            gravando = false;
            fusedClient.removeLocationUpdates(locationCallback);

            if (passeio.fim == null || passeio.fim.trim().isEmpty()) {
                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
                passeio.fim = sdf.format(new java.util.Date());
            }

            long id = dao.inserirPasseio(passeio);
            for (Ponto p : listaPontos) {
                dao.inserirPonto(id, p);
            }

            desenharPasseio();
            btnFinalizar.setEnabled(false);
            btnFinalizar.setText("Passeio salvo!");

            new android.os.Handler().postDelayed(() -> finish(), 2000);
        });

        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        verificarPermissao();
    }

    private void verificarPermissao() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION
            );
        } else {
            ativarLocalizacao();
        }
    }

    private void ativarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        mMap.setMyLocationEnabled(true);

        LocationRequest request = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                if (!gravando) return;

                android.location.Location location = result.getLastLocation();
                if (location == null) return;

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                listaPontos.add(new Ponto(lat, lon));

                LatLng pos = new LatLng(lat, lon);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));
            }
        };

        fusedClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
    }

    private void desenharPasseio() {
        if (listaPontos.isEmpty()) return;

        PolylineOptions linha = new PolylineOptions()
                .color(0xFF2196F3)
                .width(8f);

        for (Ponto p : listaPontos) {
            linha.add(new LatLng(p.lat, p.lon));
        }

        mMap.addPolyline(linha);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(listaPontos.get(0).lat, listaPontos.get(0).lon))
                .title("Início"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        listaPontos.get(listaPontos.size() - 1).lat,
                        listaPontos.get(listaPontos.size() - 1).lon))
                .title("Fim"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ativarLocalizacao();
        }
    }
}