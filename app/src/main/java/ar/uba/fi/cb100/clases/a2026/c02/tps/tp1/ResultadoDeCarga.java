package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

/**
 * Resultado de leer el archivo de prestamos: el registro con todos los
 * prestamos validos, el detalle de los errores encontrados (uno por
 * cada linea invalida) y la cantidad total de lineas de datos leidas
 * (validas + invalidas, sin contar comentarios ni lineas en blanco).
 *
 * Es un record porque es solo un "paquete" de tres datos relacionados:
 * no necesita comportamiento propio, solo agruparlos para que
 * LectorDePrestamos.cargar(...) pueda devolver los tres juntos.
 *
 * Invariante de clase: registro y errores nunca son null (aunque
 * errores puede tener longitud 0 si no hubo errores), y
 * lineasDeDatos es siempre mayor o igual a la suma de
 * registro.cantidad() + errores.length (puede ser mayor si en el
 * futuro se agregaran otros motivos de descarte, pero con el lector
 * actual siempre coincide exactamente con esa suma).
 *
 * Al ser un record generado por el compilador, no hace falta comentar
 * el constructor canonico ni los accesores registro(), errores() y
 * lineasDeDatos(): simplemente devuelven, sin modificarlos, los
 * valores con los que se construyo la instancia (no tienen
 * precondiciones propias mas alla de las de este record en su
 * conjunto, y su postcondicion es siempre "devuelve el campo tal cual
 * fue guardado").
 */
public record ResultadoDeCarga(
        RegistroDePrestamos registro,
        String[] errores,
        int lineasDeDatos
) {
}