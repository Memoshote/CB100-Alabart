package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

/**
 * Contrato del TDA "registro de prestamos".
 *
 * Esta interfaz dice QUE se puede hacer con un registro de prestamos,
 * pero no dice COMO se guardan los datos por dentro. Eso es justamente
 * la idea de separar el contrato de la implementacion: el resto del
 * programa (Reporteador, Tp1, los tests) puede programar contra esta
 * interfaz sin saber ni importarle si adentro hay un arreglo, y si el
 * dia de manana cambiamos la implementacion, nada de lo que use
 * RegistroDePrestamos se entera.
 *
 * Todos los metodos de esta interfaz asumen (como precondicion general)
 * que la referencia sobre la que se invocan no es null.
 */
public interface RegistroDePrestamos {

    /**
     * Agrega un prestamo al registro.
     *
     * Precondicion: p no es null.
     * Postcondicion: cantidad() aumenta exactamente en 1 respecto de su
     * valor antes de la llamada, y obtener(cantidad() - 1) despues de
     * esta llamada devuelve exactamente p. El orden relativo de los
     * prestamos ya registrados no cambia.
     *
     * @param p el prestamo a agregar.
     */
    void registrar(Prestamo p);

    /**
     * Informa cuantos prestamos hay cargados.
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve un entero mayor o igual a 0, igual a la
     * cantidad de veces que se llamo exitosamente a registrar() desde
     * que el registro se creo. No modifica el estado del registro.
     *
     * @return la cantidad de prestamos registrados (no la capacidad
     *         interna que pueda tener la implementacion).
     */
    int cantidad();

    /**
     * Devuelve el prestamo almacenado en una posicion dada.
     *
     * Precondicion: 0 <= i < cantidad().
     * Postcondicion: devuelve el Prestamo que ocupa la posicion i, en el
     * mismo orden en que fueron registrados (la posicion 0 es el primero
     * que se registro). No modifica el estado del registro.
     *
     * @param i posicion a consultar, entre 0 y cantidad() - 1.
     * @return el prestamo en esa posicion.
     * @throws IndexOutOfBoundsException si i es invalido (negativo o
     *         mayor o igual a cantidad()).
     */
    Prestamo obtener(int i);

    /**
     * Lista los padrones de todos los socios con al menos un prestamo.
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve un arreglo sin elementos repetidos, con un
     * elemento por cada padron distinto que aparece en algun prestamo
     * registrado, en el orden en que cada padron aparecio por primera
     * vez. Si no hay ningun prestamo registrado, devuelve un arreglo
     * vacio (nunca null). No modifica el estado del registro.
     *
     * @return los padrones distintos, en orden de primera aparicion.
     */
    int[] padrones();

    /**
     * Busca todos los prestamos asociados a un padron.
     *
     * Precondicion: ninguna (padron puede no existir en el registro).
     * Postcondicion: devuelve un arreglo con todos los Prestamo cuyo
     * padron() es igual al parametro, en el mismo orden en que fueron
     * registrados. Si ningun prestamo coincide, devuelve un arreglo
     * vacio (nunca null). No modifica el estado del registro.
     *
     * @param padron el padron a buscar.
     * @return los prestamos de ese padron (posiblemente vacio).
     */
    Prestamo[] prestamosDe(int padron);

    /**
     * Calcula los titulos con mas prestamos registrados.
     *
     * Precondicion: n >= 0.
     * Postcondicion: devuelve un arreglo con, como maximo, n titulos
     * distintos (menos, si hay menos de n titulos distintos en total),
     * ordenados de mayor a menor cantidad de prestamos; si dos titulos
     * tienen la misma cantidad, el que va primero es el alfabeticamente
     * menor (segun String.compareTo). No modifica el estado del registro.
     *
     * @param n cantidad maxima de titulos a devolver.
     * @return hasta n titulos, ordenados segun el criterio de arriba.
     */
    String[] titulosMasPedidos(int n);
}