package ar.uba.fi.cb100.clases.a2026.c02.s02.e01.v1;

import java.util.Objects;

public class Punto implements Comparable<Punto> {
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
//ATRIBUTOS -----------------------------------------------------------------------------------------------

    private double x = 0;
    private double y = 0;

//CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Crea un punto en el origen.
     */
    public Punto() {}

    /**
     * Crea un punto en la posicion x e y dadas.
     * @param x: coordenada x
     * @param y: coordenada y
     */
    public Punto(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

//METODOS ABSTRACTOS --------------------------------------------------------------------------------------
//METODOS DE CLASE ----------------------------------------------------------------------------------------
//METODOS GENERALES ---------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Punto{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null ||
            getClass() != o.getClass()) return false;
        Punto punto = (Punto) o;
        return Double.compare(x, punto.x) == 0 &&
               Double.compare(y, punto.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    /**
     * Compara dos puntos por orden de x, luego por orden de y.
     * @param o: otro punto
     * Devuelve -1 si el punto actual es menor que otro,
     *         0 si son iguales y
     *         1 si el punto actual es mayor que otro.
     */
    public int compareTo(Punto o) {
        int comparacion = Double.compare(this.x, o.x);
        if (comparacion == 0) {
            comparacion = Double.compare(this.y, o.y);
        }
        return comparacion;
    }

//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Suma el punto p al punto actual.
     * @param p: un punto
     */
    public void sumar(Punto p) {
        this.setX(this.getX() + p.getX());
        this.setY(this.getY() + p.getY());
    }

//GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Devuelve la coordenada x del punto.
     * @return coordenada x
     */
    public double getX() {
        return x;
    }

    /**
     * Devuelve la coordenada y del punto.
     * @return coordenada y
     */
    public double getY() {
        return y;
    }

//SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Establece la coordenada x del punto.
     * @param x: coordenada x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Establece la coordenada y del punto.
     * @param y: coordenada y
     */
    public void setY(double y) {
        this.y = y;
    }

}
