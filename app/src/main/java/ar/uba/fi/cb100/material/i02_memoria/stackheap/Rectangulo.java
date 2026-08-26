package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * La clase de todos los escenarios de stack y heap.
 * <p>
 * ATENCIÓN: los atributos son públicos A PROPÓSITO, para que los escenarios
 * puedan mostrar la memoria sin el ruido de getters y setters. En código real
 * van {@code private} con métodos (encapsulamiento, Unidad 3).
 */
public class Rectangulo {

    public int base;
    public int altura;

    public Rectangulo(int base, int altura) {
        this.base = base;
        this.altura = altura;
    }

    public int area() {
        return base * altura;
    }

    /** Muta el objeto sobre el que se llama: EL ejemplo de this (escenario 3). */
    public void crecer(int delta) {
        this.base = this.base + delta;
        this.altura = this.altura + delta;
    }

    /** Igualdad por CONTENIDO (escenario 9: equals vs ==). */
    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof Rectangulo r)) {
            return false;
        }
        return base == r.base && altura == r.altura;
    }

    @Override
    public int hashCode() {
        return 31 * base + altura;
    }

    @Override
    public String toString() {
        return "Rectangulo(" + base + "x" + altura + ")";
    }
}
