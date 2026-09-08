package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class LectorDePrestamosTest {

    private static final Path ARCHIVO_DE_DATOS = Path.of(
            "app", "src", "main", "java", "ar", "uba", "fi", "cb100",
            "clases", "a2026", "c02", "tps", "tp1", "datos", "prestamos.csv");

    @Test
    void elArchivoOficialTieneDieciochoLineasValidasYCuatroErrores() throws IOException {
        ResultadoDeCarga resultado = LectorDePrestamos.cargar(ARCHIVO_DE_DATOS);

        assertEquals(18, resultado.registro().cantidad());
        assertEquals(4, resultado.errores().length);
        assertEquals(22, resultado.lineasDeDatos());
    }

    @Test
    void losCuatroErroresSonUnoDeCadaTipo() throws IOException {
        ResultadoDeCarga resultado = LectorDePrestamos.cargar(ARCHIVO_DE_DATOS);
        String todosLosErrores = String.join(" | ", resultado.errores());

        assertTrue(todosLosErrores.contains("se esperaban 6 campos"));
        assertTrue(todosLosErrores.contains("fecha invalida"));
        assertTrue(todosLosErrores.contains("padron no numerico"));
        assertTrue(todosLosErrores.contains("es anterior al retiro"));
    }
}