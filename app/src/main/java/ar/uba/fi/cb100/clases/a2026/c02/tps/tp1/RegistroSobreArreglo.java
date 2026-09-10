package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.util.Arrays;

/**
 * Implementacion de RegistroDePrestamos sobre un arreglo de Prestamo
 * que nosotros mismos hacemos crecer cuando se llena.
 *
 * El arreglo "datos" arranca con capacidad 8 y, cada vez que se llena, 
 * se cambia por uno del doble de tamano, copiando los elementos que ya 
 * estaban con Arrays.copyOf.
 *
 * Invariante de clase:
 * - datos nunca es null y datos.length siempre es una potencia de 2
 *   mayor o igual a CAPACIDAD_INICIAL.
 * - 0 <= cantidad <= datos.length.
 * - las posiciones datos[0..cantidad-1] contienen los prestamos
 *   registrados, en el orden en que se registraron, y ninguna es null.
 * - las posiciones datos[cantidad..datos.length-1] (si existen) no se
 *   usan (pueden ser null o contener basura de una copia anterior, pero
 *   ningun metodo publico las expone).
 */
public class RegistroSobreArreglo implements RegistroDePrestamos {
    
    private static final int CAPACIDAD_INICIAL = 8;

    private Prestamo[] datos;
    private int cantidad;

    /**
     * Crea un registro vacio.
     *
     * Precondicion: ninguna.
     * Postcondicion: el registro queda con cantidad() == 0 y capacidad
     * interna CAPACIDAD_INICIAL (8), lista para recibir registrar().
     */
    public RegistroSobreArreglo() {
        this.datos = new Prestamo[CAPACIDAD_INICIAL];
        this.cantidad = 0;
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: p no es null.
     * Postcondicion: cantidad() queda incrementada en 1; si el arreglo
     * interno estaba lleno, su capacidad se duplica antes de agregar p
     * (crecimiento manual, sin ArrayList).
     *
     * @throws IllegalArgumentException si p es null.
     */
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

    /**
     * {@inheritDoc}
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve el valor actual del contador interno
     * "cantidad", sin modificar el estado del registro.
     */
    @Override
    public int cantidad() {
        return cantidad;
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: 0 <= i < cantidad().
     * Postcondicion: devuelve datos[i] sin modificar el estado del
     * registro.
     *
     * @throws IndexOutOfBoundsException si i < 0 o i >= cantidad().
     */
    @Override
    public Prestamo obtener(int i) {
        if (i < 0 || i >= cantidad) {
            throw new IndexOutOfBoundsException("indice invalido: " + i);
        }
        return datos[i];
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve un arreglo nuevo (no una vista de "datos")
     * con los padrones distintos, en orden de primera aparicion. No
     * modifica el estado del registro.
     */
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

    /**
     * Busca linealmente un valor dentro de las primeras posiciones de un arreglo.
     *
     * Precondicion: arreglo no es null; 0 <= hasta <= arreglo.length.
     * Postcondicion: devuelve true si y solo si existe una posicion i,
     * con 0 <= i < hasta, tal que arreglo[i] == valor. No modifica
     * arreglo.
     *
     * @param arreglo arreglo donde buscar.
     * @param hasta   cantidad de posiciones validas a considerar (desde el indice 0).
     * @param valor   valor a buscar.
     * @return true si valor aparece entre las primeras "hasta" posiciones.
     */
    private boolean estaEnArreglo(int[] arreglo, int hasta, int valor) {
        for (int i = 0; i < hasta; i++) {
            if (arreglo[i] == valor) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: ninguna (padron puede no tener ningun prestamo).
     * Postcondicion: devuelve un arreglo nuevo, de tamano exacto (sin
     * huecos), con los prestamos de ese padron en el orden en que
     * fueron registrados. Si no hay ninguno, devuelve un arreglo de
     * longitud 0. No modifica el estado del registro.
     */
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

    /**
     * Cuenta cuantos prestamos registrados pertenecen a un padron dado.
     *
     * Precondicion: ninguna.
     * Postcondicion: devuelve la cantidad de posiciones i, con
     * 0 <= i < cantidad, tales que datos[i].padron() == padron. No
     * modifica el estado del registro.
     *
     * @param padron el padron a contar.
     * @return la cantidad de prestamos de ese padron (puede ser 0).
     */
    private int contarCoincidencias(int padron) {
        int coincidencias = 0;
        for (int i = 0; i < cantidad; i++) {
            if (datos[i].padron() == padron) {
                coincidencias++;
            }
        }
        return coincidencias;
    }

    /**
     * {@inheritDoc}
     *
     * Precondicion: n >= 0.
     * Postcondicion: devuelve un arreglo con hasta n titulos distintos,
     * ordenados de mayor a menor cantidad de prestamos y, a igual
     * cantidad, alfabeticamente. No modifica el estado del registro.
     */
    @Override
    public String[] titulosMasPedidos(int n) {
        String[] titulosDistintos = new String[cantidad];
        int[] conteos = new int[cantidad];
        int totalDistintos = agruparPorTitulo(titulosDistintos, conteos);

        ordenarPorConteoYAlfabeto(titulosDistintos, conteos, totalDistintos);

        int cantidadAEntregar = Math.min(n, totalDistintos);
        return Arrays.copyOf(titulosDistintos, cantidadAEntregar);
    }

    /**
     * Recorre todos los prestamos y arma dos arreglos paralelos:
     * titulosDistintos[i] con su cantidad de prestamos en conteos[i].
     *
     * Precondicion: titulosDistintos y conteos no son null y tienen
     * ambos una longitud mayor o igual a cantidad.
     * Postcondicion: para cada titulo distinto presente entre los
     * prestamos registrados, queda una unica entrada en
     * titulosDistintos[0..totalDistintos-1] con su cantidad de
     * apariciones en la misma posicion de conteos. Devuelve
     * totalDistintos, la cantidad de titulos distintos encontrados.
     *
     * @param titulosDistintos arreglo (ya reservado) donde se escriben los titulos distintos.
     * @param conteos          arreglo (ya reservado) donde se escribe la cantidad de cada titulo.
     * @return cuantos titulos distintos se encontraron.
     */
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

    /**
     * Busca linealmente un titulo dentro de las primeras posiciones de un arreglo.
     *
     * Precondicion: arreglo no es null; 0 <= hasta <= arreglo.length;
     * titulo no es null.
     * Postcondicion: devuelve el menor indice i, con 0 <= i < hasta,
     * tal que arreglo[i].equals(titulo); si no existe tal indice,
     * devuelve -1. No modifica arreglo.
     *
     * @param arreglo arreglo donde buscar.
     * @param hasta   cantidad de posiciones validas a considerar.
     * @param titulo  titulo a buscar.
     * @return el indice encontrado, o -1 si no esta.
     */
    private int buscarTitulo(String[] arreglo, int hasta, String titulo) {
        for (int i = 0; i < hasta; i++) {
            if (arreglo[i].equals(titulo)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ordena, en simultaneo, dos arreglos paralelos de titulos y
     * conteos, usando un ordenamiento por seleccion.
     *
     * Precondicion: titulos y conteos no son null, ambos tienen al
     * menos "total" posiciones validas, y titulos[i] corresponde a
     * conteos[i] para cada i entre 0 y total-1.
     * Postcondicion: al terminar, para cada par de indices i < j (con
     * i, j < total) se cumple que la fila i "va antes" que la fila j
     * segun esMejorRanking (mayor conteo primero, y a igual conteo,
     * orden alfabetico de titulo). titulos y conteos quedan
     * permutados de la misma forma (la correspondencia titulo-conteo
     * se preserva). No cambia la longitud de ninguno de los dos arreglos.
     *
     * @param titulos arreglo de titulos a reordenar in-place.
     * @param conteos arreglo de conteos a reordenar in-place, en paralelo con titulos.
     * @param total   cantidad de posiciones validas a ordenar (desde el indice 0).
     */
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

    /**
     * Compara dos candidatos del ranking de titulos.
     *
     * Precondicion: tituloA y tituloB no son null.
     * Postcondicion: devuelve true si y solo si el par (conteoA, tituloA)
     * debe listarse antes que (conteoB, tituloB): esto ocurre cuando
     * conteoA es estrictamente mayor que conteoB, o cuando son iguales
     * y tituloA es alfabeticamente menor que tituloB. No tiene efectos
     * secundarios.
     *
     * @param conteoA cantidad de prestamos del primer candidato.
     * @param tituloA titulo del primer candidato.
     * @param conteoB cantidad de prestamos del segundo candidato.
     * @param tituloB titulo del segundo candidato.
     * @return true si el primer candidato va antes que el segundo.
     */
    private boolean esMejorRanking(int conteoA, String tituloA, int conteoB, String tituloB) {
        if (conteoA != conteoB) {
            return conteoA > conteoB;
        }
        return tituloA.compareTo(tituloB) < 0;
    }
}
