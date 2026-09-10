package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Exporta el reporte de multas como texto plano, con columnas de ancho
 * fijo (usando String.format).
 *
 * La interfaz ExportadorDeReporte solo recibe las filas y el destino,
 * pero el encabezado del reporte de texto necesita mostrar la fecha de
 * corte. Por eso ExportadorTxt la recibe por constructor: la interfaz
 * (el contrato) no cambia, pero esta implementacion concreta guarda un
 * dato extra que le hace falta a ella y a ninguna otra.
 *
 * Invariante de clase: corte nunca es null despues de construida la
 * instancia (se fija una unica vez en el constructor y no cambia).
 */
public class ExportadorTxt implements ExportadorDeReporte {

    private static final String LINEA_SEPARADORA =
            "--------------------------------------------------------------------";

    private final LocalDate corte;

    /**
     * Crea un exportador de texto plano para una fecha de corte dada.
     *
     * Precondicion: corte no es null.
     * Postcondicion: el exportador queda listo para usarse; toda
     * llamada posterior a exportar() va a mostrar esta fecha de corte
     * en el encabezado del reporte.
     *
     * @param corte fecha de corte a mostrar en el encabezado del reporte.
     */
    public ExportadorTxt(LocalDate corte) {
        this.corte = corte;
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: filas no es null; destino no es null.
     * Postcondicion: el archivo en destino queda con el encabezado
     * (titulo, fecha de corte, nombres de columna), una linea por cada
     * elemento de filas (en el mismo orden en que vienen), una fila de
     * totales, y todo separado por lineas de guiones. Se crean las
     * carpetas intermedias de destino si no existian.
     *
     * @throws IOException si el archivo no se puede escribir.
     */
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

    /**
     * Arma la linea de encabezado con los nombres de las columnas.
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve una linea con los seis nombres de columna
     * (Padron, Socio, Prestamos, DiasAtraso, Multa, Estado) alineados
     * con los mismos anchos que usan las filas de datos.
     *
     * @return la linea de encabezado formateada.
     */
    private String formatearEncabezado() {
        return String.format("%-9s%-19s%9s%12s%10s  %s",
                "Padron", "Socio", "Prestamos", "DiasAtraso", "Multa", "Estado");
    }

    /**
     * Agrega al texto una linea por cada fila, y calcula los totales.
     *
     * Precondicion: texto no es null; filas no es null.
     * Postcondicion: se agrega al final de texto una linea formateada
     * por cada elemento de filas, en el mismo orden, cada una seguida
     * de un salto de linea. Devuelve un arreglo de 3 posiciones con la
     * suma de prestamos, dias de atraso y multa de todas las filas, en
     * ese orden.
     *
     * @param texto StringBuilder al que se le agregan las lineas (se modifica).
     * @param filas filas a agregar.
     * @return {totalPrestamos, totalDiasDeAtraso, totalMulta}.
     */
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

    /**
     * Formatea una fila individual del reporte con columnas de ancho fijo.
     *
     * Precondicion: fila no es null.
     * Postcondicion: devuelve una linea de texto con los seis campos de
     * fila alineados en las mismas columnas que formatearEncabezado().
     *
     * @param fila la fila a formatear.
     * @return la linea de texto correspondiente a esa fila.
     */
    private String formatearFila(FilaDeSocio fila) {
        return String.format("%-9s%-19s%9d%12d%10d  %s",
                fila.padron(), fila.socio(), fila.prestamos(),
                fila.diasDeAtraso(), fila.multa(), fila.estado());
    }

    /**
     * Formatea la linea final de totales del reporte.
     *
     * Precondicion: totalPrestamos, totalDias y totalMulta son mayores
     * o iguales a 0.
     * Postcondicion: devuelve una linea con la palabra "TOTALES" seguida
     * de los tres totales, alineados en las mismas columnas numericas
     * que usan las filas de datos (sin columna de estado).
     *
     * @param totalPrestamos suma de prestamos de todas las filas.
     * @param totalDias      suma de dias de atraso de todas las filas.
     * @param totalMulta     suma de multas de todas las filas.
     * @return la linea de totales formateada.
     */
    private String formatearTotales(int totalPrestamos, int totalDias, int totalMulta) {
        return String.format("%-28s%9d%12d%10d", "TOTALES", totalPrestamos, totalDias, totalMulta);
    }

    /**
     * Escribe un texto completo en un archivo, creando las carpetas intermedias si hace falta.
     *
     * Precondicion: destino no es null; texto no es null.
     * Postcondicion: el archivo en destino existe y su contenido es
     * exactamente texto (se sobrescribe si ya existia). Si destino
     * tiene una carpeta contenedora que no existia, se crea.
     *
     * @param destino ruta del archivo a escribir.
     * @param texto   contenido completo a escribir.
     * @throws IOException si el archivo o las carpetas no se pueden crear/escribir.
     */
    private void escribirEnDisco(Path destino, String texto) throws IOException {
        if (destino.getParent() != null) {
            Files.createDirectories(destino.getParent());
        }
        Files.writeString(destino, texto);
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve siempre el String "txt".
     */
    @Override
    public String extension() {
        return "txt";
    }
}