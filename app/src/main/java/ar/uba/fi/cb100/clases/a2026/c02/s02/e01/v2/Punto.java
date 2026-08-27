package ar.uba.fi.cb100.clases.a2026.c02.s02.e01.v2;

public class Punto {
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
//ATRIBUTOS -----------------------------------------------------------------------------------------------

    private double[] coordenadas = {0, 0};

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
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
//GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Devuelve la coordenada x del punto.
     * @return coordenada x
     */
    public double getX() {
        return coordenadas[0];
    }

    /**
     * Devuelve la coordenada y del punto.
     * @return coordenada y
     */
    public double getY() {
        return coordenadas[1];
    }

//SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Establece la coordenada x del punto.
     * @param x: coordenada x
     */
    public void setX(double x) {
        this.coordenadas[0] = x;
    }

    /**
     * Establece la coordenada y del punto.
     * @param y: coordenada y
     */
    public void setY(double y) {
        this.coordenadas[1] = y;
    }
}