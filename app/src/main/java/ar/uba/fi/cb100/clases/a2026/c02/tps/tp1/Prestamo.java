package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.time.LocalDate;

public record Prestamo(
        LocalDate retiro,
        int padron,
        String socio,
        String isbn,
        String titulo,
        LocalDate devolucion
) {

    public static final int DIAS_DE_PLAZO = 14;
    public static final int MULTA_POR_DIA = 150;
    public static final int TOPE_DE_MULTA = 3000;

    public Prestamo {
        if (socio == null || socio.isBlank()) {
            throw new IllegalArgumentException("el socio no puede ser vacio");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("el isbn no puede ser vacio");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("el titulo no puede ser vacio");
        }
        if (padron <= 0) {
            throw new IllegalArgumentException("el padron debe ser positivo");
        }
        if (devolucion != null && devolucion.isBefore(retiro)) {
            throw new IllegalArgumentException(
                    "la devolucion (" + devolucion + ") es anterior al retiro (" + retiro + ")");
        }
    }

    public boolean estaPendiente() {
        return devolucion == null;
    }

    public LocalDate vencimiento() {
        long numeroDeDiaDelRetiro = retiro.toEpochDay();
        long numeroDeDiaDelVencimiento = numeroDeDiaDelRetiro + DIAS_DE_PLAZO;
        return LocalDate.ofEpochDay(numeroDeDiaDelVencimiento);
    }

    public int diasDeAtraso(LocalDate corte) {
        LocalDate hasta = estaPendiente() ? corte : devolucion;

        long numeroDeDiaDeVencimiento = vencimiento().toEpochDay();
        long numeroDeDiaDeHasta = hasta.toEpochDay();
        long dias = numeroDeDiaDeHasta - numeroDeDiaDeVencimiento;

        if (dias < 0) {
            return 0;
        }
        return (int) dias;
    }

    public int multa(LocalDate corte) {
        int multaSinTope = diasDeAtraso(corte) * MULTA_POR_DIA;

        if (multaSinTope > TOPE_DE_MULTA) {
            return TOPE_DE_MULTA;
        }
        return multaSinTope;
    }
}