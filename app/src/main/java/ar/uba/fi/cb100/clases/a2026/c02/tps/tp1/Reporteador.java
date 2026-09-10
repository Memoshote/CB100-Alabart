package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.time.LocalDate;

/**
 * Toma un RegistroDePrestamos y arma, a partir de el, el contenido de
 * los dos reportes: la tabla de multas por socio (FilaDeSocio[]) y el
 * ranking de titulos mas pedidos, ya formateado como texto.
 *
 * Esta clase solo CALCULA. Quien escribe esos datos en disco son los
 * ExportadorDeReporte (para la tabla de multas) y la clase Tp1 (para el
 * ranking, que en el reporte.txt de ejemplo va agregado al final del
 * mismo archivo). Es una clase de solo metodos estaticos, sin estado
 * propio, por eso no tiene sentido instanciarla.
 */
public class Reporteador {

    /**
     * Arma una fila por cada socio con al menos un prestamo, con el
     * total de prestamos, dias de atraso y multa acumulados hasta la
     * fecha de corte. Las filas quedan ordenadas por multa descendente
     * y, a igual multa, por nombre de socio alfabetico.
     *
     * Precondicion: r no es null; corte no es null.
     * Postcondicion: devuelve un arreglo con exactamente
     * r.padrones().length filas, una por cada padron con prestamos en
     * r, ordenadas segun el criterio de arriba. No modifica r.
     *
     * @param r     el registro de donde tomar los prestamos.
     * @param corte fecha de corte para calcular atraso y multa.
     * @return las filas del reporte de multas, ya ordenadas.
     */
    public static FilaDeSocio[] porSocio(RegistroDePrestamos r, LocalDate corte) {
        int[] padrones = r.padrones();
        FilaDeSocio[] filas = new FilaDeSocio[padrones.length];

        for (int i = 0; i < padrones.length; i++) {
            filas[i] = calcularFilaDe(r, padrones[i], corte);
        }

        ordenarPorMultaYSocio(filas);
        return filas;
    }

    /**
     * Calcula la fila de un socio en particular, sumando todos sus prestamos.
     *
     * Precondicion: r no es null; corte no es null; padron tiene al
     * menos un prestamo en r (por ejemplo, por venir de r.padrones()).
     * Postcondicion: devuelve una FilaDeSocio cuyo prestamos() es la
     * cantidad de prestamos de ese padron, cuyo diasDeAtraso() y
     * multa() son la suma de los de cada uno de esos prestamos
     * (calculados a la fecha corte), y cuyo estado() es CON_DEUDA si
     * la multa total es mayor a 0, o AL_DIA en caso contrario. No
     * modifica r.
     *
     * @param r      el registro de donde tomar los prestamos del socio.
     * @param padron el padron del socio a calcular.
     * @param corte  fecha de corte para calcular atraso y multa.
     * @return la fila calculada para ese socio.
     */
    private static FilaDeSocio calcularFilaDe(RegistroDePrestamos r, int padron, LocalDate corte) {
        Prestamo[] prestamosDelSocio = r.prestamosDe(padron);
        String nombreSocio = prestamosDelSocio[0].socio();

        int totalDiasDeAtraso = 0;
        int totalMulta = 0;
        for (Prestamo p : prestamosDelSocio) {
            totalDiasDeAtraso += p.diasDeAtraso(corte);
            totalMulta += p.multa(corte);
        }

        String estado = totalMulta > 0 ? FilaDeSocio.CON_DEUDA : FilaDeSocio.AL_DIA;
        return new FilaDeSocio(padron, nombreSocio, prestamosDelSocio.length, totalDiasDeAtraso, totalMulta, estado);
    }

    /**
     * Ordena un arreglo de filas usando un ordenamiento por seleccion.
     *
     * Precondicion: filas no es null (sus elementos tampoco lo son).
     * Postcondicion: al terminar, para cada par de indices i < j se
     * cumple que filas[i] "va antes" que filas[j] segun vaAntes (mayor
     * multa primero, y a igual multa, socio alfabetico). El arreglo se
     * reordena in-place; no cambia su longitud ni los objetos que
     * contiene, solo sus posiciones.
     *
     * @param filas arreglo de filas a ordenar in-place.
     */
    private static void ordenarPorMultaYSocio(FilaDeSocio[] filas) {
        for (int i = 0; i < filas.length - 1; i++) {
            int mejor = i;
            for (int j = i + 1; j < filas.length; j++) {
                if (vaAntes(filas[j], filas[mejor])) {
                    mejor = j;
                }
            }
            if (mejor != i) {
                FilaDeSocio auxiliar = filas[i];
                filas[i] = filas[mejor];
                filas[mejor] = auxiliar;
            }
        }
    }

    /**
     * Compara dos filas para decidir su orden en el reporte.
     *
     * Precondicion: a y b no son null.
     * Postcondicion: devuelve true si y solo si a debe listarse antes
     * que b: esto ocurre cuando a.multa() es estrictamente mayor que
     * b.multa(), o cuando son iguales y a.socio() es alfabeticamente
     * menor que b.socio().
     *
     * @param a primera fila a comparar.
     * @param b segunda fila a comparar.
     * @return true si a va antes que b en el reporte.
     */
    private static boolean vaAntes(FilaDeSocio a, FilaDeSocio b) {
        if (a.multa() != b.multa()) {
            return a.multa() > b.multa();
        }
        return a.socio().compareTo(b.socio()) < 0;
    }

    /**
     * Arma las lineas de texto del ranking de titulos mas pedidos, ya
     * numeradas y con su cantidad de prestamos, listas para escribirse
     * en el reporte.
     *
     * Precondicion: r no es null; n >= 0.
     * Postcondicion: devuelve un arreglo de Strings, uno por cada
     * titulo de r.titulosMasPedidos(n), en el mismo orden, donde cada
     * linea tiene el formato "NNN. TITULO    CANTIDAD" con el numero
     * de posicion (empezando en 1), el titulo, y la cantidad de
     * prestamos de ese titulo en todo el registro. No modifica r.
     *
     * @param r registro de donde calcular el ranking.
     * @param n cantidad maxima de titulos a incluir.
     * @return las lineas de texto del ranking, listas para escribir.
     */
    public static String[] ranking(RegistroDePrestamos r, int n) {
        String[] titulos = r.titulosMasPedidos(n);
        String[] lineas = new String[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            int cantidadDePrestamos = contarPrestamosDelTitulo(r, titulos[i]);
            lineas[i] = String.format("%3d. %-30s%4d", i + 1, titulos[i], cantidadDePrestamos);
        }
        return lineas;
    }

    /**
     * Cuenta cuantos prestamos hay, en todo el registro, para un titulo dado.
     *
     * Precondicion: r no es null; titulo no es null.
     * Postcondicion: devuelve la cantidad de prestamos en r cuyo
     * titulo() es igual (segun equals) al parametro. No modifica r.
     *
     * @param r      registro donde contar.
     * @param titulo titulo a contar.
     * @return la cantidad de prestamos de ese titulo (puede ser 0).
     */
    private static int contarPrestamosDelTitulo(RegistroDePrestamos r, String titulo) {
        int contador = 0;
        for (int i = 0; i < r.cantidad(); i++) {
            if (r.obtener(i).titulo().equals(titulo)) {
                contador++;
            }
        }
        return contador;
    }
}