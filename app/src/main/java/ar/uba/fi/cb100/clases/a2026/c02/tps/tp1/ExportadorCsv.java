package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportadorCsv implements ExportadorDeReporte {

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

    @Override
    public String extension() {
        return "csv";
    }
}
