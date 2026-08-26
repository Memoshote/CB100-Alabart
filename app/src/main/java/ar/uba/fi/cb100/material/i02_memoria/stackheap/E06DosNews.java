package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 6 — Dos news, dos maneras de "intercambiar": atributos
 * (cambia el CONTENIDO de los bloques) vs punteros (cambia QUIÉN apunta a
 * QUIÉN; los bloques ni se tocan). En main las dos funcionan — porque las
 * variables son NUESTRAS; el escenario 4 fallaba porque tocaba copias.
 * <p>
 * Debugger: dos breakpoints, uno después de cada intercambio. En el
 * primero: los @ de r1/r2 siguen iguales y las bases cambiaron. En el
 * segundo: los @ se cruzaron.
 */
public class E06DosNews {

    public static void main(String[] args) {
        Rectangulo r1 = new Rectangulo(3, 2);     // P1: @A
        Rectangulo r2 = new Rectangulo(7, 4);     // P1: @B

        // (a) intercambiar ATRIBUTOS: los bloques cambian por dentro
        int tmp = r1.base;                        // P2
        r1.base = r2.base;                        // P2: @A pasa a tener base 7
        r2.base = tmp;                            // P2: @B pasa a tener base 3
        System.out.println("tras swap de atributos: r1 = " + r1 + ", r2 = " + r2);
        // r1 = Rectangulo(7x2), r2 = Rectangulo(3x4)

        // (b) intercambiar PUNTEROS: los bloques quedan como están
        Rectangulo aux = r1;                      // P3
        r1 = r2;                                  // P3: r1 ahora apunta a @B
        r2 = aux;                                 // P3: r2 ahora apunta a @A
        System.out.println("tras swap de punteros:  r1 = " + r1 + ", r2 = " + r2);
        // r1 = Rectangulo(3x4), r2 = Rectangulo(7x2): las flechas se cruzaron
    }
}
