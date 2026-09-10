package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

/**
 * Excepcion propia que representa una linea invalida del archivo de
 * prestamos: le falta algun campo, tiene una fecha mal escrita, un
 * padron no numerico, o una devolucion anterior al retiro.
 *
 * Es una RuntimeException (no "checked") porque LectorDePrestamos la
 * atrapa internamente linea por linea: quien use la clase desde afuera
 * no esta obligado a poner un try/catch para cada linea, el lector ya
 * se encarga de convertir estos errores en el arreglo de "errores" del
 * ResultadoDeCarga.
 *
 * Invariante de clase: numeroDeLinea es siempre mayor a 0 (los archivos
 * se numeran desde la linea 1), y el mensaje heredado de
 * RuntimeException (accesible con getMessage()) nunca es null.
 */
public class LineaInvalidaException extends RuntimeException {

    /** Numero de linea real dentro del archivo (contando comentarios y blancos). */
    private final int numeroDeLinea;

    /**
     * Crea una excepcion que identifica una linea invalida del archivo.
     *
     * Precondicion: numeroDeLinea deberia ser mayor a 0 y motivo no
     * deberia ser null (no se valida explicitamente, ya que esta clase
     * la instancia unicamente el propio LectorDePrestamos con datos que
     * el controla).
     * Postcondicion: numeroDeLinea() devuelve el valor recibido, y
     * getMessage() devuelve exactamente motivo.
     *
     * @param numeroDeLinea numero de linea real del archivo (empieza en 1).
     * @param motivo        texto explicando por que la linea es invalida.
     */
    public LineaInvalidaException(int numeroDeLinea, String motivo) {
        super(motivo);
        this.numeroDeLinea = numeroDeLinea;
    }

    /**
     * Devuelve el numero de linea del archivo que provoco esta excepcion.
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve el mismo valor que se paso al constructor,
     * sin modificaciones.
     *
     * @return el numero de linea (contado desde 1).
     */
    public int numeroDeLinea() {
        return numeroDeLinea;
    }
}