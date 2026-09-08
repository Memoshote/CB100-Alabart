package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

public record ResultadoDeCarga(
        RegistroDePrestamos registro,
        String[] errores,
        int lineasDeDatos
) {
}