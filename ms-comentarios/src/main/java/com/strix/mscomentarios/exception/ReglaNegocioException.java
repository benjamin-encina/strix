package com.strix.mscomentarios.exception;

/**
 * Genera un 422 cuando se viola una regla de negocio.
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
