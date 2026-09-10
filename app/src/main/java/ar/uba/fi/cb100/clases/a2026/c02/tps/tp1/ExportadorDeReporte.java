package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Contrato para escribir la tabla de multas por socio en disco, en
 * algun formato concreto (texto plano, csv, excel...).
 *
 * Gracias a esta interfaz, la clase Tp1 no necesita saber si esta
 * escribiendo un .txt o un .csv: recibe un ExportadorDeReporte y lo usa
 * sin preguntarle nada mas. Cambiar el formato de salida (o agregar uno
 * nuevo) no requiere tocar el resto del programa.
 */
public interface ExportadorDeReporte {

    /**
     * Escribe las filas del reporte en el archivo de destino.
     *
     * Precondicion: filas no es null (puede tener longitud 0); destino
     * no es null, y quien llama tiene permisos para crear/sobrescribir
     * ese archivo (y, si hace falta, la carpeta que lo contiene).
     * Postcondicion: al terminar, el archivo en destino existe y
     * contiene la representacion de filas en el formato propio de esta
     * implementacion (ver extension()). Si el archivo ya existia, su
     * contenido anterior se reemplaza por completo.
     *
     * @param filas    filas ya calculadas y ordenadas por Reporteador.porSocio.
     * @param destino  ruta (relativa) del archivo a generar.
     * @throws IOException si el archivo no se puede escribir.
     */
    void exportar(FilaDeSocio[] filas, Path destino) throws IOException;

    /**
     * Informa la extension propia de este formato de exportacion.
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve un String no nulo ni vacio, sin el punto
     * inicial (por ejemplo "txt" o "csv").
     *
     * @return la extension de archivo que genera esta implementacion.
     */
    String extension();
}