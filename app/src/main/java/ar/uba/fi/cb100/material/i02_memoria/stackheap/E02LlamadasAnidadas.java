package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 2 — Varias llamadas anidadas: el stack crece un frame por
 * llamada, y el PUNTERO viaja por parámetro (se copia la flecha, no el
 * objeto). Tres frames vivos, un solo Rectangulo.
 * <p>
 * Debugger: breakpoint en la primera línea de area(). El panel FRAMES
 * muestra la pila entera: area &lt;- describir &lt;- main. Hacé clic en cada
 * frame y mirá Variables: r (acá), figura (en describir) y r1 (en main)
 * muestran el MISMO @: tres nombres, una sola flecha al mismo bloque.
 * Step Out (Shift+F8) desapila un frame por vez.
 */
public class E02LlamadasAnidadas {

    public static void main(String[] args) {
        Rectangulo r1 = new Rectangulo(3, 2);   // P1: un solo objeto en el heap
        describir(r1);                          // P2: se apila describir; figura = copia de la flecha
        System.out.println("sigue main");       // P5: la pila quedó como al principio
    }

    static void describir(Rectangulo figura) {
        int a = area(figura);                   // P3: se apila area; r = otra copia de la flecha
        System.out.println("área = " + a);      // P4: area ya fue desapilado
    }

    static int area(Rectangulo r) {
        return r.base * r.altura;               // las tres variables apuntan al MISMO bloque
    }
}
