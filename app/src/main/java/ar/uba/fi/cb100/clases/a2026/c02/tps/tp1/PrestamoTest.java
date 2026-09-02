package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PrestamoTest {

    @Test
    void elConstructorRechazaUnSocioVacio() {
        assertThrows(IllegalArgumentException.class, () ->
                new Prestamo(LocalDate.of(2026, 3, 1), 41234, "", "isbn", "titulo", null));
    }

    @Test
    void elConstructorRechazaUnPadronNoPositivo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Prestamo(LocalDate.of(2026, 3, 1), 0, "Ana Gomez", "isbn", "titulo", null));
    }

    @Test
    void elConstructorRechazaUnaDevolucionAnteriorAlRetiro() {
        LocalDate retiro = LocalDate.of(2026, 4, 15);
        LocalDate devolucion = LocalDate.of(2026, 4, 1);
        assertThrows(IllegalArgumentException.class, () ->
                new Prestamo(retiro, 43310, "Elena Sosa", "isbn", "titulo", devolucion));
    }

    @Test
    void elVencimientoEsElRetiroMasCatorceDias() {
        Prestamo p = new Prestamo(LocalDate.of(2026, 3, 2), 41234, "Ana Gomez",
                "isbn", "Estructuras de Datos", null);
        assertEquals(LocalDate.of(2026, 3, 16), p.vencimiento());
    }

    @Test
    void noHayAtrasoCuandoSeDevuelveEnFecha() {
        Prestamo p = new Prestamo(LocalDate.of(2026, 3, 2), 41234, "Ana Gomez",
                "isbn", "Estructuras de Datos", LocalDate.of(2026, 3, 16));
        assertEquals(0, p.diasDeAtraso(LocalDate.of(2026, 5, 4)));
    }

    @Test
    void elAtrasoDePendienteSeCuentaHastaElCorte() {
        Prestamo p = new Prestamo(LocalDate.of(2026, 3, 11), 40555, "Diego Ruiz",
                "isbn", "El Lenguaje de Programacion C", null);
        assertEquals(40, p.diasDeAtraso(LocalDate.of(2026, 5, 4)));
    }

    @Test
    void laMultaNuncaSuperaElTope() {
        Prestamo p = new Prestamo(LocalDate.of(2026, 1, 1), 41234, "Ana Gomez",
                "isbn", "titulo", null);
        assertEquals(3000, p.multa(LocalDate.of(2026, 5, 4)));
    }
}