package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.time.LocalDate;

/**
 * Representa un prestamo de un libro de la biblioteca.
 *
 * Es una clase de VALOR: dos Prestamo con exactamente los mismos datos
 * se consideran el mismo prestamo. Por eso se usa un "record": Java genera
 * automaticamente equals(), hashCode() y toString() comparando todos los
 * campos, sin que nosotros tengamos que escribirlos a mano.
 *
 * Invariante de clase (vale siempre, para cualquier instancia que exista):
 * - socio, isbn y titulo nunca son null ni estan vacios/en blanco.
 * - padron es siempre mayor a 0.
 * - si devolucion no es null, nunca es anterior a retiro.
 * Esta invariante se garantiza en el constructor compacto: si algun dato
 * la viola, el objeto directamente no llega a crearse.
 */
public record Prestamo(
        LocalDate retiro,
        int padron,
        String socio,
        String isbn,
        String titulo,
        LocalDate devolucion
) {

    public static final int DIAS_DE_PLAZO = 14;
    public static final int MULTA_POR_DIA = 150;
    public static final int TOPE_DE_MULTA = 3000;

    /**
     * Constructor compacto: se ejecuta siempre que se crea un Prestamo
     * (tanto con "new Prestamo(...)" como al leerlo desde el archivo).
     * Aca se valida toda la invariante de la clase antes de dejar
     * terminar la construccion del objeto.
     *
     * Precondicion: ninguna sobre los parametros en si (pueden venir con
     * cualquier valor, incluso invalido); es responsabilidad de este
     * constructor detectarlo.
     * Postcondicion: si el metodo retorna normalmente, el Prestamo creado
     * cumple la invariante de clase descripta en el Javadoc de la clase.
     * Si algun dato es invalido, no se crea ningun objeto: se lanza
     * IllegalArgumentException y la construccion se aborta.
     *
     * @throws IllegalArgumentException si socio, isbn o titulo son null o
     *         estan en blanco, si padron no es positivo, o si devolucion
     *         no es null y es anterior a retiro.
     */
    public Prestamo {
        if (socio == null || socio.isBlank()) {
            throw new IllegalArgumentException("el socio no puede ser vacio");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("el isbn no puede ser vacio");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("el titulo no puede ser vacio");
        }
        if (padron <= 0) {
            throw new IllegalArgumentException("el padron debe ser positivo");
        }
        if (devolucion != null && devolucion.isBefore(retiro)) {
            throw new IllegalArgumentException(
                    "la devolucion (" + devolucion + ") es anterior al retiro (" + retiro + ")");
        }
    }

    /**
     * Indica si el prestamo todavia no fue devuelto.
     *
     * Precondicion: this cumple la invariante de clase (siempre es asi,
     * dado que un Prestamo invalido no puede existir).
     * Postcondicion: devuelve true si y solo si devolucion es null.
     * No modifica el estado de this (Prestamo es inmutable).
     *
     * @return true si el prestamo esta pendiente de devolucion.
     */
    public boolean estaPendiente() {
        return devolucion == null;
    }

    /**
     * Calcula la fecha limite para devolver el libro sin generar multa.
     *
     * Precondicion: retiro no es null (garantizado por ser un componente
     * no nulo de un record ya construido).
     * Postcondicion: devuelve exactamente retiro + DIAS_DE_PLAZO dias
     * corridos. No modifica el estado de this.
     *
     * @return la fecha de vencimiento del prestamo.
     */
    public LocalDate vencimiento() {
        long numeroDeDiaDelRetiro = retiro.toEpochDay();
        long numeroDeDiaDelVencimiento = numeroDeDiaDelRetiro + DIAS_DE_PLAZO;
        return LocalDate.ofEpochDay(numeroDeDiaDelVencimiento);
    }

    /**
     * Calcula la cantidad de dias de atraso de este prestamo.
     *
     * Precondicion: corte no es null. Si el prestamo esta pendiente,
     * corte deberia representar una fecha razonable de "hoy" (no se
     * valida aca; es responsabilidad de quien llama pasar una fecha
     * sensata, como la fecha de corte del programa).
     * Postcondicion: devuelve un entero mayor o igual a 0. Si el
     * prestamo ya fue devuelto, el calculo usa la fecha de devolucion;
     * si esta pendiente, usa corte. Nunca devuelve un numero negativo,
     * aunque la devolucion (o el corte) sea anterior al vencimiento.
     * No modifica el estado de this.
     *
     * @param corte fecha a usar como "hoy" cuando el prestamo esta pendiente.
     * @return dias de atraso, siempre >= 0.
     */
    public int diasDeAtraso(LocalDate corte) {
        LocalDate hasta = estaPendiente() ? corte : devolucion;

        long numeroDeDiaDeVencimiento = vencimiento().toEpochDay();
        long numeroDeDiaDeHasta = hasta.toEpochDay();
        long dias = numeroDeDiaDeHasta - numeroDeDiaDeVencimiento;

        if (dias < 0) {
            return 0;
        }
        return (int) dias;
    }

    /**
     * Calcula la multa en pesos de este prestamo, a una fecha dada.
     *
     * Precondicion: corte no es null.
     * Postcondicion: devuelve diasDeAtraso(corte) * MULTA_POR_DIA, salvo
     * que ese valor supere TOPE_DE_MULTA, en cuyo caso devuelve
     * exactamente TOPE_DE_MULTA. El resultado esta siempre en el rango
     * [0, TOPE_DE_MULTA]. No modifica el estado de this.
     *
     * @param corte fecha a usar como "hoy" cuando el prestamo esta pendiente.
     * @return la multa del prestamo, entre 0 y TOPE_DE_MULTA inclusive.
     */
    public int multa(LocalDate corte) {
        int multaSinTope = diasDeAtraso(corte) * MULTA_POR_DIA;

        if (multaSinTope > TOPE_DE_MULTA) {
            return TOPE_DE_MULTA;
        }
        return multaSinTope;
    }
}