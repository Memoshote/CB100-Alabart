package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Exporta el reporte de multas como CSV (valores separados por punto y
 * coma). 
 * 
 * No tiene estado propio: cada llamada a exportar() es independiente
 * de las anteriores.
 */
public class ExportadorCsv implements ExportadorDeReporte {

    /**
     * {@inheritDoc}
     *
     * Precondicion: filas no es null; destino no es null.
     * Postcondicion: el archivo en destino queda con una linea de
     * encabezado fija ("padron;socio;prestamos;dias_atraso;multa;estado")
     * y, a continuacion, una linea por cada elemento de filas (en el
     * mismo orden en que vienen), con sus seis campos separados por
     * ";". Se crean las carpetas intermedias de destino si no existian.
     *
     * @throws IOException si el archivo no se puede escribir.
     */
    @Override
    public void exportar(FilaDeSocio[] filas, Path destino) throws IOException {
        StringBuilder texto = new StringBuilder();
        texto.append("padron;socio;prestamos;dias_atraso;multa;estado").append(System.lineSeparator());

        for (FilaDeSocio fila : filas) {
            texto.append(fila.padron()).append(';')
                    .append(fila.socio()).append(';')
                    .append(fila.prestamos()).append(';')
                    .append(fila.diasDeAtraso()).append(';')
                    .append(fila.multa()).append(';')
                    .append(fila.estado())
                    .append(System.lineSeparator());
        }

        if (destino.getParent() != null) {
            Files.createDirectories(destino.getParent());
        }
        Files.writeString(destino, texto.toString());
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve siempre el String "csv".
     */
    @Override
    public String extension() {
        return "csv";
    }
}
