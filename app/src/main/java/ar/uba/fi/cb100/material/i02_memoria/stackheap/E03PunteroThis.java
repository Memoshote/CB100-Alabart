package ar.uba.fi.cb100.material.i02_memoria.stackheap;

/**
 * Escenario 3 — El puntero this: cuando llamás r1.crecer(2), el frame de
 * crecer recibe un puntero OCULTO llamado this, que apunta al objeto sobre
 * el que se hizo la llamada. "this.base" es: seguí la flecha this y tocá
 * su atributo base.
 * <p>
 * Debugger: breakpoint dentro de crecer (en Rectangulo.java). En Variables
 * aparece this = Rectangulo@... — el mismo @ que r1 en el frame de main
 * (verificalo con clic en cada frame del panel Frames).
 */
public class E03PunteroThis {

    public static void main(String[] args) {
        Rectangulo r1 = new Rectangulo(3, 2);   // P1
        r1.crecer(2);                           // P2: se apila crecer con this -> @A
                                                // P3: this.base 3->5, this.altura 2->4
        System.out.println(r1);                 // P4: Rectangulo(5x4): r1 VE el cambio,
    }                                           //     porque this era su mismo bloque
}
