package com.example.lab2_20216352;

import java.util.ArrayList;
import java.util.List;

public final class EquipoRepository {

    private static final List<Equipo> EQUIPOS = new ArrayList<>();

    private EquipoRepository() {
    }

    public static synchronized List<Equipo> getAll() {
        return new ArrayList<>(EQUIPOS);
    }

    public static synchronized boolean add(Equipo equipo) {
        if (findByCodigo(equipo.getCodigo()) != null) {
            return false;
        }
        EQUIPOS.add(equipo);
        return true;
    }

    public static synchronized boolean update(String codigoOriginal, Equipo actualizado) {
        for (int i = 0; i < EQUIPOS.size(); i++) {
            if (EQUIPOS.get(i).getCodigo().equalsIgnoreCase(codigoOriginal)) {
                EQUIPOS.set(i, actualizado);
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean delete(String codigo) {
        for (int i = 0; i < EQUIPOS.size(); i++) {
            if (EQUIPOS.get(i).getCodigo().equalsIgnoreCase(codigo)) {
                EQUIPOS.remove(i);
                return true;
            }
        }
        return false;
    }

    public static synchronized Equipo findByCodigo(String codigo) {
        for (Equipo equipo : EQUIPOS) {
            if (equipo.getCodigo().equalsIgnoreCase(codigo)) {
                return equipo;
            }
        }
        return null;
    }
}
