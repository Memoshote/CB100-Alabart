package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class LectorDePrestamos {

    private static final int CANTIDAD_DE_CAMPOS = 6;

    private LectorDePrestamos() {
        // Clase de un unico metodo estatico: no tiene sentido instanciarla.
    }

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

    private static boolean esComentarioOBlanco(String linea) {
        String sinEspacios = linea.strip();
        return sinEspacios.isEmpty() || sinEspacios.startsWith("#");
    }

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

    private static LocalDate parsearFecha(String texto, int numeroDeLinea) {
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException error) {
            throw new LineaInvalidaException(numeroDeLinea, "fecha invalida: " + texto);
        }
    }

    private static int parsearPadron(String texto, int numeroDeLinea) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException error) {
            throw new LineaInvalidaException(numeroDeLinea, "padron no numerico: " + texto);
        }
    }
}