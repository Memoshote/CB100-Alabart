package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportadorCsvTest {

    @Test
    void escribeElEncabezadoYUnaFilaCorrectamente() throws IOException {
        FilaDeSocio[] filas = { new FilaDeSocio(41234, "Ana Gomez", 4, 23, 3450, "CON_DEUDA") };

        Path archivoTemporal = Files.createTempFile("reporte", ".csv");
        try {
            new ExportadorCsv().exportar(filas, archivoTemporal);
            List<String> lineas = Files.readAllLines(archivoTemporal);

            assertEquals("padron;socio;prestamos;dias_atraso;multa;estado", lineas.get(0));
            assertEquals("41234;Ana Gomez;4;23;3450;CON_DEUDA", lineas.get(1));
            assertEquals(2, lineas.size());
        } finally {
            Files.deleteIfExists(archivoTemporal);
        }
    }

    @Test
    void laExtensionDeclaradaEsCsv() {
        assertEquals("csv", new ExportadorCsv().extension());
    }
}