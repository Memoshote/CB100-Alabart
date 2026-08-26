package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 9 — == compara FLECHAS (¿es el mismo bloque?); equals compara
 * CONTENIDO (si la clase lo define, como Rectangulo). Y el caso String,
 * donde Java mete la cola con el "pool" de literales.
 * <p>
 * Debugger: breakpoint en el primer println. Mirá los @ de a, b y c en
 * Variables: a y c comparten @; b tiene otro. Con los String: s1 y s2
 * ¡muestran el MISMO @! (pool de literales); s3 tiene el suyo.
 */
public class E09EqualsVsIgualIgual {

    public static void main(String[] args) {
        Rectangulo a = new Rectangulo(3, 2);    // P1: @A
        Rectangulo b = new Rectangulo(3, 2);    // P1: @B (mismo contenido, OTRO bloque)
        Rectangulo c = a;                       // P1: copia de la flecha: c -> @A

        System.out.println(a == b);             // false: bloques distintos
        System.out.println(a == c);             // true:  la MISMA flecha
        System.out.println(a.equals(b));        // true:  mismo CONTENIDO (3x2)

        // --- el caso String -------------------------------------------------
        String s1 = "hola";                     // P2: literal -> va al POOL: @P
        String s2 = "hola";                     // P2: MISMO literal -> reusa @P
        String s3 = new String("hola");         // P2: new OBLIGA un bloque nuevo: @C

        System.out.println(s1 == s2);           // true:  ¡mismo bloque del pool!
        System.out.println(s1 == s3);           // false: bloques distintos
        System.out.println(s1.equals(s3));      // true:  mismo contenido
    }
}
