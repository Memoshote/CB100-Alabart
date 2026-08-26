package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 4 — Swap de PUNTEROS dentro de un método: al salir, se pierde.
 * <p>
 * x e y son COPIAS de las flechas r1 y r2. El método cruza las copias con
 * prolijidad… y al desapilarse el frame, las copias mueren con él: r1 y r2
 * nunca se enteraron. Éste es EL clásico de parcial.
 * <p>
 * Debugger: breakpoint en la primera línea de intercambiar. Mirá x e y
 * cruzarse con Step Over (F8), y al Step Out (Shift+F8) mirá r1 y r2 en
 * main: intactos.
 */
public class E04SwapDePunteros {

    public static void main(String[] args) {
        Rectangulo r1 = new Rectangulo(3, 2);   // P1: @A
        Rectangulo r2 = new Rectangulo(7, 4);   // P1: @B
        intercambiar(r1, r2);                   // P2: x copia r1, y copia r2
        System.out.println("r1 = " + r1);       // r1 = Rectangulo(3x2)  <- SIN cambios
        System.out.println("r2 = " + r2);       // r2 = Rectangulo(7x4)  <- SIN cambios
    }

    static void intercambiar(Rectangulo x, Rectangulo y) {
        Rectangulo tmp = x;                     // P3: tmp guarda la flecha de x
        x = y;                                  // P4: x pasa a apuntar a @B
        y = tmp;                                // P4: y pasa a apuntar a @A
    }                                           // P5: el frame se desapila: x, y, tmp mueren
}
