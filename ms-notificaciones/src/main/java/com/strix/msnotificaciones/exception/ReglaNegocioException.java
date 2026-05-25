package com.strix.msnotificaciones.exception;

/**
 * HTTP 422 obtenido por violar las reglas de negocio.
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
