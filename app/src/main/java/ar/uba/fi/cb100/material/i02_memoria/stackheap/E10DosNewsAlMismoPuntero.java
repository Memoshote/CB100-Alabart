package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 10 — Dos news al MISMO puntero, sin backup: el primer bloque
 * queda colgado (UNO solo), y el puntero sigue perfectamente válido…
 * apuntando al nuevo.
 * <p>
 * OJO: no es el mismo caso que el escenario 7. Allá había VARIOS punteros
 * y quedaban VARIOS bloques huérfanos; acá hay UN puntero que se reasigna
 * y queda UN huérfano. Lo que se pierde no es el puntero: es el BLOQUE
 * viejo, que ya no tiene quién lo apunte.
 * <p>
 * Debugger: breakpoint en el println y otro en la línea del segundo new.
 * Anotá el @ de r antes y después del segundo new: CAMBIA. El @ viejo no
 * aparece más en ninguna variable: ese bloque quedó huérfano.
 */
public class E10DosNewsAlMismoPuntero {

    public static void main(String[] args) {
        Rectangulo r = new Rectangulo(3, 2);    // P1: r -> @A
        r = new Rectangulo(7, 4);               // P2: r -> @B; @A queda sin flechas

        System.out.println(r);                  // Rectangulo(7x4): r es 100% usable
        // A @A nadie lo nombra ni lo alcanza: huérfano, futuro trabajo del
        // recolector de basura. No hay forma de "volver" a él: no guardamos backup.
    }
}
