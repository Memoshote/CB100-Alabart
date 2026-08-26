package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 0 — Primitivos puros: TODO vive en el stack, el heap ni aparece.
 * <p>
 * Debugger: breakpoint en la línea del primer println, panel Variables:
 * a y b muestran sus VALORES directamente (no hay flechitas ni "@").
 */
public class E00PrimitivosPuros {

    public static void main(String[] args) {
        int a = 5;                         // P1: a nace en el frame de main, vale 5
        int b = a;                         // P2: b COPIA el valor. Son dos casilleros
        b = 7;                             // P3: cambia b. a ni se entera

        System.out.println("a = " + a);    // a = 5
        System.out.println("b = " + b);    // b = 7
    }
}
