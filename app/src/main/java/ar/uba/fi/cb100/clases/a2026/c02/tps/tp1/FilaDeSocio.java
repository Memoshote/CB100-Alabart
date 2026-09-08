package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

/**
 * Una fila del reporte de multas: el resumen de un socio con la
 * cantidad de prestamos, el total de dias de atraso, la multa total,
 * y su estado (AL_DIA o CON_DEUDA).
 */
public record FilaDeSocio(
        int padron,
        String socio,
        int prestamos,
        int diasDeAtraso,
        int multa,
        String estado
) {
    public static final String AL_DIA = "AL_DIA";
    public static final String CON_DEUDA = "CON_DEUDA";
}