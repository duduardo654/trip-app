package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PasseioDAO {

    BancoHelper helper;

    public PasseioDAO(Context c) {
        helper = new BancoHelper(c);
    }

    public Cursor listar() {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, nome FROM passeio", null);
    }

    public long inserirPasseio(Passeio p) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("nome", p.nome);
        v.put("tipo", p.tipo);
        v.put("descricao", p.descricao);
        v.put("inicio", p.inicio);
        v.put("fim", p.fim);
        return db.insert("passeio", null, v);
    }

    public void inserirPonto(long idPasseio, Ponto p) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("id_passeio", idPasseio);
        v.put("lat", p.lat);
        v.put("lon", p.lon);
        db.insert("ponto", null, v);
    }

    public Cursor buscarPasseio(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM passeio WHERE id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public ArrayList<Ponto> listarPontos(long idPasseio) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Ponto> lista = new ArrayList<>();
        Cursor c = db.rawQuery(
                "SELECT lat, lon FROM ponto WHERE id_passeio = ?",
                new String[]{String.valueOf(idPasseio)}
        );
        while (c.moveToNext()) {
            lista.add(new Ponto(c.getDouble(0), c.getDouble(1)));
        }
        c.close();
        return lista;
    }
}