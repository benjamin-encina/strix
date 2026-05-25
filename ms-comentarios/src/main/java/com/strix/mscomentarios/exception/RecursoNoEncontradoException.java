package com.strix.mscomentarios.exception;

/**
 * Lanzada cuando un recurso no existe en la base de datos generando un 404 como resultado.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
