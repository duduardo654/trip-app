package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class PasseioAdapter extends ArrayAdapter<Passeio> {

    private final Context context;
    private final ArrayList<Passeio> lista;

    public PasseioAdapter(Context context, ArrayList<Passeio> lista) {
        super(context, R.layout.item_passeio, lista);
        this.context = context;
        this.lista   = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_passeio, parent, false);

            holder = new ViewHolder();
            holder.txtNome      = convertView.findViewById(R.id.txtItemNome);
            holder.txtDescricao = convertView.findViewById(R.id.txtItemDescricao);
            holder.chk          = convertView.findViewById(R.id.chkPasseio);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Passeio p = lista.get(position);

        holder.txtNome.setText(p.nome);
        holder.txtDescricao.setText(p.descricao);
        holder.chk.setChecked(p.selecionado);

        return convertView;
    }

    static class ViewHolder {
        TextView txtNome;
        TextView txtDescricao;
        CheckBox chk;
    }
}