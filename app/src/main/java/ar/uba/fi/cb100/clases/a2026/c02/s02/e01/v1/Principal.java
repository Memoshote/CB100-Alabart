package ar.uba.fi.cb100.clases.a2026.c02.s02.e01.v1;

//import ar.uba.fi.cb100.clases.a2026.c02.s02.e01.v1.Punto;

import ar.uba.fi.cb100.clases.a2026.c02.s02.e05.Formal;
import ar.uba.fi.cb100.clases.a2026.c02.s02.e05.Informal;
import ar.uba.fi.cb100.clases.a2026.c02.s02.e05.Saludador;

public class Principal {

    public static void main(String[] args) {
        {
            Punto p = new Punto();
            //100 lineas
            p.setX(1);
            p.setY(2);
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
        }
        {
            Punto p = new Punto(1, 2);
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
        }
        { //Simulamos una implementacion con el set
            Punto p = new Punto(1, 2);
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
            //100 lineas
            p.setX(2);
            p.setY(3);
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
        }
        { //Simulamos una implementacion sin el set
            Punto p = new Punto(1, 2);
            //100 lineas
            p = new Punto(2, 3);
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
        }
        { //Simulamos una implementacion sin el set (NO FUNCIONA)
            Punto p = new Punto(1, 2);
            Punto p2 = p;
            //100 lineas
            p = new Punto(2, 3);
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
        }
        { //Simulamos una implementacion con el set (FUNCIONA)
            Punto p = new Punto(1, 2);
            Punto p2 = p;
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
            //100 lineas
            p.setX(2);
            p.setY(3);
            System.out.println("Punto: (" + p.getX() + ", " + p.getY() + ")");
        }


        {
            Punto p1 = new Punto(1, 2);
            Punto p2 = new Punto(3, 4);

            p1.sumar(p2);
            System.out.println("Punto 1: (" + p1.getX() + ", " + p1.getY() + ")");

            p2.sumar(p1);
            System.out.println("Punto 2: (" + p2.getX() + ", " + p2.getY() + ")");

            Punto p3 = Geometria.sumar(p1, p2);
            System.out.println("Punto 3: (" + p3.getX() + ", " + p3.getY() + ")");


            System.out.println(p3);
            System.out.println(p3.toString());
        }
        {
            Punto p1 = new Punto(1, 2);
            Punto p2 = new Punto(1, 2);
            //No funciona
            if (p1 == p2) {
                System.out.println("Son iguales (por ==)");
            } else {
                System.out.println("No son iguales (por ==)");
            }
            //Correcto
            if (p1.equals(p2)) {
                System.out.println("Son iguales (por equals)");
            } else {
                System.out.println("No son iguales (por equals)");
            }

            //No funciona
            if (p1 != p2) {
                System.out.println("Son iguales (por ==)");
            } else {
                System.out.println("No son iguales (por ==)");
            }
            //Funciona
            if (!p1.equals(p2)) {
                System.out.println("Son iguales (por equals)");
            } else {
                System.out.println("No son iguales (por equals)");
            }

            if (p1.compareTo(p2) == 0) {
                System.out.println("Son iguales (por compareTo)");
            } else {
                System.out.println("No son iguales (por compareTo)");
            }

            int resultado = p1.compareTo(p2);

            if (resultado < 0) {
                System.out.println("El punto 1 es menor que el punto 2");
            } else if (resultado == 0) {
                System.out.println("Los puntos son iguales");
            } else {
                System.out.println("El punto 1 es mayor que el punto 2");
            }

        }
    }


    public static void saludador() {
        Formal formal = new Formal();
        System.out.println(formal.saludar());
        nombreDelMetodo(formal);
        formal.getUnValor();

        Informal informal = new Informal();
        System.out.println(informal.saludar());
        nombreDelMetodo(informal);
        //informal.getUnValor(); NO COMPILA

        Punto p1 = new Punto();
        //nombreDelMetodo(p1); NO COMPILA
    }

    public static void nombreDelMetodo(Saludador s) {
        System.out.println( s.saludar() );
        //s.getUnValor(); NO COMPILA
    }
}
