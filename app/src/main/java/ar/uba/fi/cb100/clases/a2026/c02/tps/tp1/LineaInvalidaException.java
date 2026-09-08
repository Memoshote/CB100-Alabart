package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

public class LineaInvalidaException extends RuntimeException {

    private final int numeroDeLinea;

    public LineaInvalidaException(int numeroDeLinea, String motivo) {
        super(motivo);
        this.numeroDeLinea = numeroDeLinea;
    }

    public int numeroDeLinea() {
        return numeroDeLinea;
    }
}