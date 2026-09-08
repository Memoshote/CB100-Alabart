package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

public interface RegistroDePrestamos {

    void registrar(Prestamo p);

    int cantidad();

    Prestamo obtener(int i);

    int[] padrones();

    Prestamo[] prestamosDe(int padron);

    String[] titulosMasPedidos(int n);
}