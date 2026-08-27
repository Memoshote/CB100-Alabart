package ar.uba.fi.cb100.clases.a2026.c02.s02;

import java.util.Objects;

public class Alumno {

    String nombre;
    int nota;
    String dni;

    public Alumno(String nombre, String dni, int nota) {
        this.nombre = nombre;
        this.dni = dni;
        this.nota = nota;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNota() {
        return nota;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return Objects.equals(dni, alumno.dni);
    }

    public String firmar(String dni) {
        String nombre = "HOla";
        return "Firma del alumno " + this.getNombre() + " con DNI " + this.dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
