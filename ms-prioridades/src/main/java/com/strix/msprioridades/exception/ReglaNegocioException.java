package com.strix.msprioridades.exception;

/**
 * Lanzada al violar una regla de negocio del dominio → HTTP 422.
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
