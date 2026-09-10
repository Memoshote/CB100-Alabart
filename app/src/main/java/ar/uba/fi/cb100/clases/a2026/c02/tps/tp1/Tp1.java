package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

/**
 * Punto de entrada del programa. Lee el archivo de prestamos, muestra
 * por consola el resumen de la carga, y escribe en disco los dos
 * reportes (multas por socio en .txt y en .csv, y el ranking de
 * titulos mas pedidos, agregado al final del .txt).
 *
 * Usar SIEMPRE rutas relativas (nunca absolutas), tal como pide el
 * enunciado.
 */
public class Tp1 {

    private static final String ENTRADA_POR_DEFECTO =
            "app/src/main/java/ar/uba/fi/cb100/clases/a2026/c02/tps/tp1/datos/prestamos.csv";
    private static final LocalDate CORTE_POR_DEFECTO = LocalDate.parse("2026-05-04");
    private static final int TAMANO_DEL_RANKING = 3;

    /**
     * Punto de entrada del programa.
     *
     * Precondicion: si se pasa args[0], debe ser una ruta relativa
     * valida a un archivo legible con el formato esperado; si se pasa
     * args[1], debe ser una fecha en formato ISO (AAAA-MM-DD). Ambos
     * son opcionales: sin argumentos, se usan ENTRADA_POR_DEFECTO y
     * CORTE_POR_DEFECTO.
     * Postcondicion: se imprime por consola el resumen de la carga
     * (lineas de datos, validas, descartadas, y el detalle de cada
     * error); y quedan escritos en disco "salida/reporte.txt" (con la
     * tabla de multas y el ranking de titulos al final) y
     * "salida/reporte.csv" (solo con la tabla de multas). Se crea la
     * carpeta "salida/" si no existia.
     *
     * @param args args[0] = archivo de entrada (opcional); args[1] = fecha de corte ISO (opcional).
     * @throws IOException si el archivo de entrada no se puede leer, o los reportes no se pueden escribir.
     */
    public static void main(String[] args) throws IOException {
        Path archivoDeEntrada = args.length > 0 ? Path.of(args[0]) : Path.of(ENTRADA_POR_DEFECTO);
        LocalDate corte = args.length > 1 ? LocalDate.parse(args[1]) : CORTE_POR_DEFECTO;

        ResultadoDeCarga resultado = LectorDePrestamos.cargar(archivoDeEntrada);
        mostrarResumenDeCarga(resultado);

        FilaDeSocio[] filas = Reporteador.porSocio(resultado.registro(), corte);
        String[] ranking = Reporteador.ranking(resultado.registro(), TAMANO_DEL_RANKING);

        Path reporteTxt = Path.of("salida", "reporte.txt");
        Path reporteCsv = Path.of("salida", "reporte.csv");

        ExportadorDeReporte exportadorTxt = new ExportadorTxt(corte);
        exportadorTxt.exportar(filas, reporteTxt);
        agregarRankingAlTxt(reporteTxt, ranking);

        ExportadorDeReporte exportadorCsv = new ExportadorCsv();
        exportadorCsv.exportar(filas, reporteCsv);
    }

    /**
     * Muestra por consola cuantas lineas de datos se leyeron, cuantas
     * fueron validas, cuantas se descartaron, y el detalle de cada
     * error (con su numero de linea real dentro del archivo).
     *
     * Precondicion: resultado no es null.
     * Postcondicion: se imprimieron por System.out exactamente
     * 1 + resultado.errores().length lineas: la linea de resumen, y
     * una linea por cada error, indentada con dos espacios. No
     * modifica resultado.
     *
     * @param resultado el resultado de la carga a resumir.
     */
    private static void mostrarResumenDeCarga(ResultadoDeCarga resultado) {
        int validas = resultado.registro().cantidad();
        int descartadas = resultado.errores().length;

        System.out.println("Lineas de datos: " + resultado.lineasDeDatos()
                + " | validas: " + validas
                + " | descartadas: " + descartadas);

        for (String error : resultado.errores()) {
            System.out.println("  " + error);
        }
    }

    /**
     * Agrega, al final del reporte.txt ya escrito por ExportadorTxt, la
     * seccion con el ranking de titulos mas pedidos. Se hace aparte
     * porque ExportadorDeReporte solo conoce las filas de multas, no el
     * ranking de titulos (ese calculo lo hace Reporteador.ranking).
     *
     * Precondicion: reporteTxt ya existe en disco (fue creado antes por
     * un ExportadorTxt.exportar exitoso); ranking no es null.
     * Postcondicion: al archivo reporteTxt se le agrega, al final, una
     * linea en blanco, el titulo "TITULOS MAS PEDIDOS", y una linea por
     * cada elemento de ranking (en el mismo orden). El contenido previo
     * del archivo no se modifica.
     *
     * @param reporteTxt ruta del reporte de texto ya generado.
     * @param ranking    lineas del ranking, ya formateadas por Reporteador.ranking.
     * @throws IOException si el archivo no se puede escribir.
     */
    private static void agregarRankingAlTxt(Path reporteTxt, String[] ranking) throws IOException {
        StringBuilder texto = new StringBuilder();
        texto.append(System.lineSeparator());
        texto.append("TITULOS MAS PEDIDOS").append(System.lineSeparator());
        for (String linea : ranking) {
            texto.append(linea).append(System.lineSeparator());
        }
        Files.writeString(reporteTxt, texto.toString(), StandardOpenOption.APPEND);
    }
}