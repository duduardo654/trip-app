package com.example.myapplication;

import java.util.ArrayList;

public class Passeio {

    public long id;
    public String nome;
    public String tipo;
    public String descricao;
    public String inicio;
    public String fim;
    public boolean selecionado = false;

    public ArrayList<Ponto> pontos = new ArrayList<>();
}