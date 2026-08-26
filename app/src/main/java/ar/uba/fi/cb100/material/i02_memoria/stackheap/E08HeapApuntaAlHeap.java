package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 8 — Un new adentro de algo del heap: el atributo marco de
 * Cuadro es un puntero que vive EN EL HEAP y apunta a otro bloque del heap.
 * Los punteros no son sólo cosa del stack.
 * <p>
 * Debugger: breakpoint en el último println. Desplegá c en Variables:
 * adentro aparece marco = Rectangulo@... — una flecha DENTRO del objeto.
 * Compará ese @ con el de alias: es el mismo bloque.
 */
public class E08HeapApuntaAlHeap {

    public static void main(String[] args) {
        Cuadro c = new Cuadro(new Rectangulo(3, 2));   // P1: DOS news: @A (Cuadro)
                                                       //     cuyo campo marco -> @B
        Rectangulo alias = c.marco;                    // P2: el stack TAMBIÉN puede
                                                       //     apuntar a @B
        alias.base = 9;                                // P3: modifica @B...

        System.out.println(c.marco.base);              // 9  <- ...y c.marco lo VE:
        System.out.println(alias == c.marco);          // true: misma flecha, mismo bloque
    }
}
