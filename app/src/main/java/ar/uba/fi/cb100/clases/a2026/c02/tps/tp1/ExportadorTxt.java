package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class ExportadorTxt implements ExportadorDeReporte {

    private static final String LINEA_SEPARADORA =
            "--------------------------------------------------------------------";

    private final LocalDate corte;

    public ExportadorTxt(LocalDate corte) {
        this.corte = corte;
    }

    @Override
    public void exportar(FilaDeSocio[] filas, Path destino) throws IOException {
        StringBuilder texto = new StringBuilder();
        texto.append("BIBLIOTECA FIUBA - REPORTE DE MULTAS").append(System.lineSeparator());
        texto.append("Fecha de corte: ").append(corte).append(System.lineSeparator());
        texto.append(formatearEncabezado()).append(System.lineSeparator());
        texto.append(LINEA_SEPARADORA).append(System.lineSeparator());

        int[] totales = agregarFilasYSumarTotales(texto, filas);

        texto.append(LINEA_SEPARADORA).append(System.lineSeparator());
        texto.append(formatearTotales(totales[0], totales[1], totales[2])).append(System.lineSeparator());

        escribirEnDisco(destino, texto.toString());
    }

    private String formatearEncabezado() {
        return String.format("%-9s%-19s%9s%12s%10s  %s",
                "Padron", "Socio", "Prestamos", "DiasAtraso", "Multa", "Estado");
    }

    private int[] agregarFilasYSumarTotales(StringBuilder texto, FilaDeSocio[] filas) {
        int totalPrestamos = 0;
        int totalDiasDeAtraso = 0;
        int totalMulta = 0;

        for (FilaDeSocio fila : filas) {
            texto.append(formatearFila(fila)).append(System.lineSeparator());
            totalPrestamos += fila.prestamos();
            totalDiasDeAtraso += fila.diasDeAtraso();
            totalMulta += fila.multa();
        }
        return new int[]{totalPrestamos, totalDiasDeAtraso, totalMulta};
    }

    private String formatearFila(FilaDeSocio fila) {
        return String.format("%-9s%-19s%9d%12d%10d  %s",
                fila.padron(), fila.socio(), fila.prestamos(),
                fila.diasDeAtraso(), fila.multa(), fila.estado());
    }

    private String formatearTotales(int totalPrestamos, int totalDias, int totalMulta) {
        return String.format("%-28s%9d%12d%10d", "TOTALES", totalPrestamos, totalDias, totalMulta);
    }

    private void escribirEnDisco(Path destino, String texto) throws IOException {
        if (destino.getParent() != null) {
            Files.createDirectories(destino.getParent());
        }
        Files.writeString(destino, texto);
    }

    @Override
    public String extension() {
        return "txt";
    }
}