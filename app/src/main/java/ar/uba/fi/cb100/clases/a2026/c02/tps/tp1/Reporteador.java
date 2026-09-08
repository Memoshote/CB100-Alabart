package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.time.LocalDate;

public class Reporteador {

    private Reporteador() {
        // Clase de solo metodos estaticos: no tiene sentido instanciarla.
    }

    public static FilaDeSocio[] porSocio(RegistroDePrestamos r, LocalDate corte) {
        int[] padrones = r.padrones();
        FilaDeSocio[] filas = new FilaDeSocio[padrones.length];

        for (int i = 0; i < padrones.length; i++) {
            filas[i] = calcularFilaDe(r, padrones[i], corte);
        }

        ordenarPorMultaYSocio(filas);
        return filas;
    }

    private static FilaDeSocio calcularFilaDe(RegistroDePrestamos r, int padron, LocalDate corte) {
        Prestamo[] prestamosDelSocio = r.prestamosDe(padron);
        String nombreSocio = prestamosDelSocio[0].socio();

        int totalDiasDeAtraso = 0;
        int totalMulta = 0;
        for (Prestamo p : prestamosDelSocio) {
            totalDiasDeAtraso += p.diasDeAtraso(corte);
            totalMulta += p.multa(corte);
        }

        String estado = totalMulta > 0 ? FilaDeSocio.CON_DEUDA : FilaDeSocio.AL_DIA;
        return new FilaDeSocio(padron, nombreSocio, prestamosDelSocio.length, totalDiasDeAtraso, totalMulta, estado);
    }

    private static void ordenarPorMultaYSocio(FilaDeSocio[] filas) {
        for (int i = 0; i < filas.length - 1; i++) {
            int mejor = i;
            for (int j = i + 1; j < filas.length; j++) {
                if (vaAntes(filas[j], filas[mejor])) {
                    mejor = j;
                }
            }
            if (mejor != i) {
                FilaDeSocio auxiliar = filas[i];
                filas[i] = filas[mejor];
                filas[mejor] = auxiliar;
            }
        }
    }

    private static boolean vaAntes(FilaDeSocio a, FilaDeSocio b) {
        if (a.multa() != b.multa()) {
            return a.multa() > b.multa();
        }
        return a.socio().compareTo(b.socio()) < 0;
    }

    public static String[] ranking(RegistroDePrestamos r, int n) {
        String[] titulos = r.titulosMasPedidos(n);
        String[] lineas = new String[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            int cantidadDePrestamos = contarPrestamosDelTitulo(r, titulos[i]);
            lineas[i] = String.format("%3d. %-30s%4d", i + 1, titulos[i], cantidadDePrestamos);
        }
        return lineas;
    }

    private static int contarPrestamosDelTitulo(RegistroDePrestamos r, String titulo) {
        int contador = 0;
        for (int i = 0; i < r.cantidad(); i++) {
            if (r.obtener(i).titulo().equals(titulo)) {
                contador++;
            }
        }
        return contador;
    }
}