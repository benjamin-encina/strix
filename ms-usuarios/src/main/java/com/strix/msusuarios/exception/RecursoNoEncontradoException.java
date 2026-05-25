package com.strix.msusuarios.exception;

/**
 * HTTP 404 obtenido cuando un recurso no existe en la base de datos.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
