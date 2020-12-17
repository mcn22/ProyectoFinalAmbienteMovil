package com.example.proyectofinalambienteweb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalambienteweb.R;

import java.util.List;

import model.Cita;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    @Override
    public void onClick(View v) {
        if (listener!=null) {
            listener.onClick(v);
        }
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private List<Cita> lista;

    public CitaAdapter(List<Cita> lista) {
        this.lista = lista;
    }

    public void actualiza(List<Cita> lista) {
        this.lista= lista;
    }

    @NonNull
    @Override
    public CitaAdapter.CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_cita,parent,false);
        vista.setOnClickListener(this);

        return new CitaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaAdapter.CitaViewHolder holder, int position) {
        holder.tvEstablecimiento.setText(lista.get(position).getEstablecimiento());
        holder.tvArea.setText(lista.get(position).getArea());
        holder.tvFecha.setText(lista.get(position).getFecha());
        holder.tvHora.setText(lista.get(position).getHora());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class CitaViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEstablecimiento;
        public TextView tvArea;
        public TextView tvFecha;
        public TextView tvHora;
        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvEstablecimiento = itemView.findViewById(R.id.tvLugar);
            this.tvArea = itemView.findViewById(R.id.tvArea);
            this.tvFecha = itemView.findViewById(R.id.tvFecha);
            this.tvHora = itemView.findViewById(R.id.tvHora);
        }
    }
}
