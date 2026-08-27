package ar.uba.fi.cb100.clases.a2026.c02.s02;

import ar.uba.fi.cb100.guia.i12_profesional.i02_facil.e09.BuscadorDeAlumnos;

public class Semana02 {

    public static void main1() {

        BuscadorDeAlumnos.Alumno alumno1 = new BuscadorDeAlumnos.Alumno("Juan", 8);
        BuscadorDeAlumnos.Alumno alumno2 = null; //0 0-> 0x00000000


        if (alumno2 == null) {
            alumno2 = new BuscadorDeAlumnos.Alumno("Juan", 8);
        }
        alumno2 = null;
        alumno2.nombre(); //Da error

        alumno2 = new BuscadorDeAlumnos.Alumno("Juan", 8);
        alumno2.nombre(); //Anda

        if (alumno1 == alumno2) { //FALSO
            System.out.println("Los alumnos son el mismo");
        }

        alumno1 = alumno2;
        if (alumno1 == alumno2) { //Verdadero
            System.out.println("Los alumnos son el mismo");
        }

        int a = 8;
        int b = 10;
        if (a==b) { //falso
            System.out.println("a y b son iguales");
        }
        b = 8;
        if (a==b) { //verdadero
            System.out.println("a y b son iguales");
        }

        Alumno alumno3 = new Alumno("juan Schmidt", "40152625", 8);
        Alumno alumno4 = new Alumno("juan Perez", "40152625", 9);

        if (alumno3.equals(alumno4)) {
            System.out.println("Los alumnos son el mismo");
        }

        System.out.println("Firma: " + alumno4.firmar("2815846"));

        alumno1.nombre();
    }

    public static void main() {
        try {
            Auto auto = new Auto(10);
            auto.setVelocidad(50);
            System.out.println("Tiempo transcurrido: " + auto.tiempoTranscurrido(100));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
