package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.util.Arrays;

public class RegistroSobreArreglo implements RegistroDePrestamos {
    
    private static final int CAPACIDAD_INICIAL = 8;

    private Prestamo[] datos;
    private int cantidad;

    public RegistroSobreArreglo() {
        this.datos = new Prestamo[CAPACIDAD_INICIAL];
        this.cantidad = 0;
    }

    @Override
    public void registrar(Prestamo p) {
        if (p == null) {
            throw new IllegalArgumentException("no se puede registrar un prestamo null");
        }
        if (cantidad == datos.length) {
            datos = Arrays.copyOf(datos, datos.length * 2);
        }
        datos[cantidad] = p;
        cantidad++;
    }

    @Override
    public int cantidad() {
        return cantidad;
    }

    @Override
    public Prestamo obtener(int i) {
        if (i < 0 || i >= cantidad) {
            throw new IndexOutOfBoundsException("indice invalido: " + i);
        }
        return datos[i];
    }

    @Override
    public int[] padrones() {
        // En el peor caso todos los padrones son distintos: "resultado"
        // puede tener como maximo "cantidad" lugares. Se recorta al
        // final con Arrays.copyOf.
        int[] resultado = new int[cantidad];
        int totalDistintos = 0;

        for (int i = 0; i < cantidad; i++) {
            int padron = datos[i].padron();
            if (!estaEnArreglo(resultado, totalDistintos, padron)) {
                resultado[totalDistintos] = padron;
                totalDistintos++;
            }
        }

        return Arrays.copyOf(resultado, totalDistintos);
    }

    private boolean estaEnArreglo(int[] arreglo, int hasta, int valor) {
        for (int i = 0; i < hasta; i++) {
            if (arreglo[i] == valor) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Prestamo[] prestamosDe(int padron) {
        int coincidencias = contarCoincidencias(padron);

        Prestamo[] resultado = new Prestamo[coincidencias];
        int siguiente = 0;
        for (int i = 0; i < cantidad; i++) {
            if (datos[i].padron() == padron) {
                resultado[siguiente] = datos[i];
                siguiente++;
            }
        }
        return resultado; // arreglo vacio si no hubo coincidencias, nunca null
    }

    private int contarCoincidencias(int padron) {
        int coincidencias = 0;
        for (int i = 0; i < cantidad; i++) {
            if (datos[i].padron() == padron) {
                coincidencias++;
            }
        }
        return coincidencias;
    }

    @Override
    public String[] titulosMasPedidos(int n) {
        String[] titulosDistintos = new String[cantidad];
        int[] conteos = new int[cantidad];
        int totalDistintos = agruparPorTitulo(titulosDistintos, conteos);

        ordenarPorConteoYAlfabeto(titulosDistintos, conteos, totalDistintos);

        int cantidadAEntregar = Math.min(n, totalDistintos);
        return Arrays.copyOf(titulosDistintos, cantidadAEntregar);
    }

    private int agruparPorTitulo(String[] titulosDistintos, int[] conteos) {
        int totalDistintos = 0;
        for (int i = 0; i < cantidad; i++) {
            String titulo = datos[i].titulo();
            int posicion = buscarTitulo(titulosDistintos, totalDistintos, titulo);
            if (posicion == -1) {
                titulosDistintos[totalDistintos] = titulo;
                conteos[totalDistintos] = 1;
                totalDistintos++;
            } else {
                conteos[posicion]++;
            }
        }
        return totalDistintos;
    }

    private int buscarTitulo(String[] arreglo, int hasta, String titulo) {
        for (int i = 0; i < hasta; i++) {
            if (arreglo[i].equals(titulo)) {
                return i;
            }
        }
        return -1;
    }

    private void ordenarPorConteoYAlfabeto(String[] titulos, int[] conteos, int total) {
        for (int i = 0; i < total - 1; i++) {
            int mejor = i;
            for (int j = i + 1; j < total; j++) {
                if (esMejorRanking(conteos[j], titulos[j], conteos[mejor], titulos[mejor])) {
                    mejor = j;
                }
            }
            if (mejor != i) {
                String tituloAux = titulos[i];
                titulos[i] = titulos[mejor];
                titulos[mejor] = tituloAux;

                int conteoAux = conteos[i];
                conteos[i] = conteos[mejor];
                conteos[mejor] = conteoAux;
            }
        }
    }

    private boolean esMejorRanking(int conteoA, String tituloA, int conteoB, String tituloB) {
        if (conteoA != conteoB) {
            return conteoA > conteoB;
        }
        return tituloA.compareTo(tituloB) < 0;
    }
}
