package com.strix.mscategorias.exception;

/**
 * Lanzada cuando un recurso no existe en la BD → HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
