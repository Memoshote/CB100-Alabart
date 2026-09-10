package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Lee el archivo de texto con los prestamos, linea por linea, y arma un
 * ResultadoDeCarga: un RegistroDePrestamos con todas las lineas validas
 * ya convertidas a Prestamo, y un arreglo con el detalle de las lineas
 * invalidas (que se descartan, pero se informan, sin cortar la lectura
 * del resto del archivo).
 *
 * Es una clase de un unico metodo publico estatico (cargar): no
 * representa un objeto con estado propio, sino un conjunto de
 * funciones relacionadas, por eso no tiene sentido instanciarla.
 */
public class LectorDePrestamos {

    private static final int CANTIDAD_DE_CAMPOS = 6;

    /**
     * Lee el archivo indicado y devuelve el resultado de la carga.
     *
     * Precondicion: archivo no es null y apunta a un archivo de texto
     * existente y legible, con el formato descripto en el enunciado
     * (campos separados por ";", fechas en formato ISO, lineas en
     * blanco o que empiezan con "#" para comentarios).
     * Postcondicion: devuelve un ResultadoDeCarga cuyo registro()
     * contiene un Prestamo por cada linea de datos bien formada, en el
     * mismo orden en que aparecen en el archivo; cuyo errores() tiene
     * un mensaje ("linea N: motivo") por cada linea de datos mal
     * formada, en el orden en que aparecen; y cuyo lineasDeDatos() es
     * la cantidad total de lineas que no son blancas ni comentarios
     * (validas + invalidas). El archivo no se modifica (solo se lee).
     *
     * @param archivo ruta (relativa) al archivo de prestamos.
     * @return el resultado de la carga, con el registro y los errores.
     * @throws IOException si el archivo no existe o no se puede leer.
     */
    public static ResultadoDeCarga cargar(Path archivo) throws IOException {
        List<String> lineas = Files.readAllLines(archivo);

        RegistroDePrestamos registro = new RegistroSobreArreglo();
        String[] errores = new String[8]; // crece a mano, igual que RegistroSobreArreglo
        int cantidadDeErrores = 0;
        int lineasDeDatos = 0;

        for (int indice = 0; indice < lineas.size(); indice++) {
            String linea = lineas.get(indice);
            if (esComentarioOBlanco(linea)) {
                continue;
            }
            lineasDeDatos++;
            try {
                registro.registrar(parsearLinea(linea, indice + 1));
            } catch (LineaInvalidaException error) {
                if (cantidadDeErrores == errores.length) {
                    errores = Arrays.copyOf(errores, errores.length * 2);
                }
                errores[cantidadDeErrores++] = "linea " + error.numeroDeLinea() + ": " + error.getMessage();
            }
        }

        return new ResultadoDeCarga(registro, Arrays.copyOf(errores, cantidadDeErrores), lineasDeDatos);
    }

    /**
     * Determina si una linea del archivo debe ignorarse por completo.
     *
     * Precondicion: linea no es null.
     * Postcondicion: devuelve true si y solo si, sacando los espacios
     * de los bordes, la linea queda vacia o empieza con "#".
     *
     * @param linea la linea cruda, tal como viene del archivo.
     * @return true si la linea es un comentario o esta en blanco.
     */
    private static boolean esComentarioOBlanco(String linea) {
        String sinEspacios = linea.strip();
        return sinEspacios.isEmpty() || sinEspacios.startsWith("#");
    }

    /**
     * Convierte una linea de texto (ya sabida no blanca ni comentario)
     * en un Prestamo, o lanza LineaInvalidaException con el motivo si
     * algo esta mal.
     *
     * Se usa split(";", -1) y no split(";") porque el segundo, al
     * encontrarse con un ultimo campo vacio (fechaDevolucion sin
     * completar), directamente lo elimina del arreglo resultante en
     * lugar de dejarlo como un String vacio. Eso rompe el conteo de
     * campos: un prestamo pendiente pasaria de tener 6 campos a tener 5,
     * y se rechazaria por error aunque este bien escrito.
     *
     * Precondicion: linea no es null y numeroDeLinea es mayor a 0.
     * Postcondicion: si retorna normalmente, el Prestamo devuelto es
     * valido (cumple la invariante de Prestamo). Si la linea no tiene
     * exactamente 6 campos separados por ";", si alguna fecha esta mal
     * escrita, si el padron no es numerico, o si los datos violan
     * alguna regla de Prestamo, lanza LineaInvalidaException con
     * numeroDeLinea y un motivo describiendo el problema, sin dejar
     * ningun efecto secundario (no se registra nada en ningun registro).
     *
     * @param linea         la linea de datos a interpretar.
     * @param numeroDeLinea numero real de esa linea dentro del archivo (para el mensaje de error).
     * @return el Prestamo construido a partir de la linea.
     * @throws LineaInvalidaException si la linea no se puede interpretar como un Prestamo valido.
     */
    private static Prestamo parsearLinea(String linea, int numeroDeLinea) {
        String[] campos = linea.split(";", -1);
        if (campos.length != CANTIDAD_DE_CAMPOS) {
            throw new LineaInvalidaException(numeroDeLinea,
                    "se esperaban 6 campos y llegaron " + campos.length);
        }

        LocalDate retiro = parsearFecha(campos[0].strip(), numeroDeLinea);
        int padron = parsearPadron(campos[1].strip(), numeroDeLinea);
        String socio = campos[2].strip();
        String isbn = campos[3].strip();
        String titulo = campos[4].strip();
        String textoDevolucion = campos[5].strip();
        LocalDate devolucion = textoDevolucion.isEmpty() ? null : parsearFecha(textoDevolucion, numeroDeLinea);

        try {
            return new Prestamo(retiro, padron, socio, isbn, titulo, devolucion);
        } catch (IllegalArgumentException invariante) {
            throw new LineaInvalidaException(numeroDeLinea, invariante.getMessage());
        }
    }

    /**
     * Interpreta un texto como una fecha en formato ISO (AAAA-MM-DD).
     *
     * Precondicion: texto no es null (puede estar vacio, pero en ese
     * caso tambien fallara el parseo).
     * Postcondicion: si texto representa una fecha ISO valida, la
     * devuelve como LocalDate. Si no, lanza LineaInvalidaException con
     * numeroDeLinea y un motivo que incluye el texto recibido.
     *
     * @param texto         el texto a interpretar como fecha.
     * @param numeroDeLinea numero de linea del archivo (para el mensaje de error).
     * @return la fecha interpretada.
     * @throws LineaInvalidaException si texto no es una fecha ISO valida.
     */
    private static LocalDate parsearFecha(String texto, int numeroDeLinea) {
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException error) {
            throw new LineaInvalidaException(numeroDeLinea, "fecha invalida: " + texto);
        }
    }

    /**
     * Interpreta un texto como el numero de padron de un socio.
     *
     * Precondicion: texto no es null.
     * Postcondicion: si texto representa un numero entero valido (segun
     * Integer.parseInt), lo devuelve como int. Si no, lanza
     * LineaInvalidaException con numeroDeLinea y un motivo que incluye
     * el texto recibido. No valida aca si el numero es positivo (esa
     * regla la valida el constructor de Prestamo).
     *
     * @param texto         el texto a interpretar como padron.
     * @param numeroDeLinea numero de linea del archivo (para el mensaje de error).
     * @return el padron interpretado.
     * @throws LineaInvalidaException si texto no es un numero entero valido.
     */
    private static int parsearPadron(String texto, int numeroDeLinea) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException error) {
            throw new LineaInvalidaException(numeroDeLinea, "padron no numerico: " + texto);
        }
    }
}