package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Para el escenario 8: un objeto cuyo ATRIBUTO es un puntero a OTRO objeto.
 * Los punteros no viven sólo en el stack: un bloque del heap puede apuntar
 * a otro bloque del heap.
 */
public class Cuadro {

    public Rectangulo marco;        // un puntero DENTRO de un bloque del heap

    public Cuadro(Rectangulo marco) {
        this.marco = marco;
    }
}
