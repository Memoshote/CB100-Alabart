package ar.uba.fi.cb100.clases.a2026.c02.s02.e01.v1;

public class Geometria {

    /**
     * Suma los puntos p y q.
     * @param p: un punto
     * @param q: un punto
     * @return la suma
     */
    public static Punto sumar(Punto p, Punto q) {
        return new Punto(p.getX() + q.getX(), p.getY() + q.getY());
    }
}
