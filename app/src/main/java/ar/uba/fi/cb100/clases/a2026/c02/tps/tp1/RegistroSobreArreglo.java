package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.util.Arrays;

public class RegistroSobreArreglo implements RegistroDePrestamos {
    
    private static final int CAPACIDAD_INICIAL = 8;

    private Prestamo[] datos;
    private int cantidad;

    public RegistroSobreArreglo() {
        this.datos = new Prestamo[CAPACIDAD_INICIAL];
        this.cantidad = 0;
    }

    @Override
    public void registrar(Prestamo p) {
        if (p == null) {
            throw new IllegalArgumentException("no se puede registrar un prestamo null");
        }
        if (cantidad == datos.length) {
            datos = Arrays.copyOf(datos, datos.length * 2);
        }
        datos[cantidad] = p;
        cantidad++;
    }

    @Override
    public int cantidad() {
        return cantidad;
    }

    @Override
    public Prestamo obtener(int i) {
        if (i < 0 || i >= cantidad) {
            throw new IndexOutOfBoundsException("indice invalido: " + i);
        }
        return datos[i];
    }
}
