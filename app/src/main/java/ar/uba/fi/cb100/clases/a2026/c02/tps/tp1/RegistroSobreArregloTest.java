package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class RegistroSobreArregloTest {

    private Prestamo prestamo(int padron, String socio, String titulo) {
        return new Prestamo(LocalDate.of(2026, 3, 1), padron, socio, "isbn-" + titulo, titulo, null);
    }

    @Test
    void creceMasAllaDeLaCapacidadInicialDeOcho() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        for (int i = 1; i <= 20; i++) {
            registro.registrar(prestamo(40000 + i, "Socio " + i, "Titulo " + i));
        }
        assertEquals(20, registro.cantidad());
    }

    @Test
    void obtenerLanzaExcepcionConUnIndiceInvalido() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        registro.registrar(prestamo(41234, "Ana Gomez", "Estructuras de Datos"));
        assertThrows(IndexOutOfBoundsException.class, () -> registro.obtener(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> registro.obtener(1));
    }

    @Test
    void padronesSinRepetidosYEnOrdenDeAparicion() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        registro.registrar(prestamo(41234, "Ana Gomez", "Estructuras de Datos"));
        registro.registrar(prestamo(39876, "Bruno Ferrari", "Clean Code"));
        registro.registrar(prestamo(41234, "Ana Gomez", "Clean Code")); // padron repetido

        assertArrayEquals(new int[]{41234, 39876}, registro.padrones());
    }

    @Test
    void prestamosDeUnPadronInexistenteDevuelveArregloVacio() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        registro.registrar(prestamo(41234, "Ana Gomez", "Estructuras de Datos"));

        assertEquals(0, registro.prestamosDe(99999).length);
    }

    @Test
    void elRankingDeTitulosDesempataAlfabeticamente() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        registro.registrar(prestamo(41234, "Ana Gomez", "Introduction to Algorithms"));
        registro.registrar(prestamo(39876, "Bruno Ferrari", "Clean Code"));
        registro.registrar(prestamo(42001, "Carla Nunez", "Estructuras de Datos"));
        registro.registrar(prestamo(43310, "Elena Sosa", "Estructuras de Datos"));

        String[] ranking = registro.titulosMasPedidos(3);

        assertArrayEquals(
                new String[]{"Estructuras de Datos", "Clean Code", "Introduction to Algorithms"},
                ranking);
    }
}
