package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 1 — Un new simple: del stack al heap y al atributo.
 * <p>
 * Debugger: breakpoint en la línea del new, Step Over (F8). En Variables,
 * r1 aparece como {@code Rectangulo@519} (¡la flecha!) y se despliega con
 * el triangulito para ver base y altura: eso ES el bloque del heap.
 */
public class E01NewSimple {

    public static void main(String[] args) {
        Rectangulo r1 = new Rectangulo(3, 2);   // P1: new reserva el bloque en el heap
                                                //     y guarda EN r1 la flecha hacia él
        int a = r1.base;                        // P2: seguir la flecha y leer el atributo

        System.out.println("r1.base = " + r1.base);      // 3
        System.out.println("r1.area() = " + r1.area());  // 6
        System.out.println("a = " + a);                  // 3
    }
}
