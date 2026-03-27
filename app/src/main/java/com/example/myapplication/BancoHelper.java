package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "passeios.db";
    public static final int DB_VERSION = 1;

    public BancoHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE passeio (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT," +
                        "tipo TEXT," +
                        "descricao TEXT," +
                        "inicio TEXT," +
                        "fim TEXT)"
        );

        db.execSQL(
                "CREATE TABLE ponto (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_passeio INTEGER," +
                        "lat REAL," +
                        "lon REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ponto");
        db.execSQL("DROP TABLE IF EXISTS passeio");
        onCreate(db);
    }
}