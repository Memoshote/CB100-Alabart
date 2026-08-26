package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 5 — Swap de VALORES (atributos): se mantiene al salir.
 * <p>
 * La diferencia con el escenario 4: acá el método no toca sus copias de las
 * flechas — las USA para llegar a los bloques del heap y modifica los
 * atributos ADENTRO de los bloques. El heap es compartido: main lo ve.
 * <p>
 * Debugger: breakpoint en main después de la llamada; desplegá r1 y r2 en
 * Variables: las bases quedaron intercambiadas (los @ NO cambiaron).
 */
public class E05SwapDeValores {

    public static void main(String[] args) {
        Rectangulo r1 = new Rectangulo(3, 2);   // P1: @A
        Rectangulo r2 = new Rectangulo(7, 4);   // P1: @B
        intercambiarBases(r1, r2);              // P2: x -> @A, y -> @B (copias)
        System.out.println("r1 = " + r1);       // r1 = Rectangulo(7x2)  <- ¡cambió!
        System.out.println("r2 = " + r2);       // r2 = Rectangulo(3x4)  <- ¡cambió!
    }

    static void intercambiarBases(Rectangulo x, Rectangulo y) {
        int tmp = x.base;                       // P3: lee 3 SIGUIENDO la flecha
        x.base = y.base;                        // P4: escribe 7 DENTRO de @A
        y.base = tmp;                           // P4: escribe 3 DENTRO de @B
    }                                           // P5: el frame muere, PERO los bloques quedaron cambiados
}
