package com.example.lab2_20216352;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.lab2_20216352.databinding.ItemEquipoBinding;

import java.util.List;

public class EquipoAdapter extends ArrayAdapter<Equipo> {

    public EquipoAdapter(@NonNull Context context, @NonNull List<Equipo> equipos) {
        super(context, 0, equipos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemEquipoBinding binding;
        if (convertView == null) {
            binding = ItemEquipoBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemEquipoBinding) convertView.getTag();
        }

        Equipo equipo = getItem(position);
        if (equipo != null) {
            binding.tvCodigo.setText(getContext().getString(R.string.label_codigo, equipo.getCodigo()));
            binding.tvNombre.setText(getContext().getString(R.string.label_nombre, equipo.getNombre()));
            binding.tvTipo.setText(getContext().getString(R.string.label_tipo, equipo.getTipo()));
            binding.tvEstado.setText(getContext().getString(R.string.label_estado, equipo.getEstado()));

            int colorEstado = getEstadoColor(equipo.getEstado());
            binding.tvEstado.setTextColor(colorEstado);
        }

        return convertView;
    }

    private int getEstadoColor(String estado) {
        String operativo = getContext().getString(R.string.operativo);
        String mantenimiento = getContext().getString(R.string.mantenimiento);
        String fueraServicio = getContext().getString(R.string.fuera_servicio);

        if (operativo.equalsIgnoreCase(estado)) {
            return ContextCompat.getColor(getContext(), R.color.status_operativo);
        }
        if (mantenimiento.equalsIgnoreCase(estado)) {
            return ContextCompat.getColor(getContext(), R.color.status_mantenimiento);
        }
        if (fueraServicio.equalsIgnoreCase(estado)) {
            return ContextCompat.getColor(getContext(), R.color.status_fuera_servicio);
        }
        return Color.WHITE;
    }
}
