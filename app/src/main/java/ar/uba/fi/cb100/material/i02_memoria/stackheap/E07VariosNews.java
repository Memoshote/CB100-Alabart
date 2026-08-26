package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 7 — Varios news, cada puntero con el suyo… y una reasignación
 * masiva: quedan colgados VARIOS bloques DISTINTOS.
 * <p>
 * Después de r1 = r3 y r2 = r3, los tres punteros miran a @C. Los bloques
 * @A y @B siguen existiendo un rato en el heap, pero ya no hay NINGUNA
 * flecha que llegue a ellos: son inalcanzables (dos bloques huérfanos
 * distintos) y el recolector de basura los va a levantar cuando quiera.
 * <p>
 * OJO: no confundir con el escenario 10 (un solo puntero reasignado:
 * UN huérfano). Acá hay varios punteros y quedan VARIOS huérfanos.
 * <p>
 * Debugger: breakpoint en el println. r1, r2 y r3 muestran el MISMO @.
 * A @A y @B ya no los podés encontrar desde ninguna variable.
 */
public class E07VariosNews {

    public static void main(String[] args) {
        Rectangulo r1 = new Rectangulo(1, 1);   // P1: @A
        Rectangulo r2 = new Rectangulo(2, 2);   // P1: @B
        Rectangulo r3 = new Rectangulo(3, 3);   // P1: @C

        r1 = r3;                                // P2: @A pierde su única flecha
        r2 = r3;                                // P3: @B pierde su única flecha

        System.out.println(r1 + " " + r2 + " " + r3);
        // Rectangulo(3x3) Rectangulo(3x3) Rectangulo(3x3)
        System.out.println(r1 == r2);           // true: LAS TRES flechas van a @C
    }
}
