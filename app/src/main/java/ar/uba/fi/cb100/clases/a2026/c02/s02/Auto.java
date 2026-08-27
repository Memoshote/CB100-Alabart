package ar.uba.fi.cb100.clases.a2026.c02.s02;

public class Auto {
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
//ATRIBUTOS -----------------------------------------------------------------------------------------------

    //Debe ser mayor que cero. Invariante.
    private double velocidad;

//CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Crea un auto con la velocidad dada.
     * @param velocidad: la velocidad del auto en km/h.
     */
    public Auto(double velocidad) {
        this.setVelocidad(velocidad);
    }

//METODOS ABSTRACTOS --------------------------------------------------------------------------------------
//METODOS DE CLASE ----------------------------------------------------------------------------------------
//METODOS GENERALES ---------------------------------------------------------------------------------------
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Calcula el tiempo transcurrido en h para recorrer una distancia dada.
     *
     * @param distancia: la distancia recorrida en km. Debe ser mayor que cero.
     * @return el tiempo transcurrido en horas.
     */
    public double tiempoTranscurrido(double distancia) {
        if (distancia <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor que cero.");
        }
        return distancia / velocidad;
    }

//GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * @return Devuelve la velocidad del auto.
     */
    public double getVelocidad() {
        return velocidad;
    }

//SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Establece la velocidad del auto.
     * @param velocidad: la nueva velocidad del auto en km/h.
     */
    public void setVelocidad(double velocidad) {
        if (velocidad <= 0) {
            throw new IllegalArgumentException("La velocidad debe ser mayor que cero.");
        }
        this.velocidad = velocidad;
    }
}
