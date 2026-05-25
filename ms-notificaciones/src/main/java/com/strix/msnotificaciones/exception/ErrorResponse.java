package com.strix.msnotificaciones.exception;

import java.time.LocalDateTime;

/**
 * Cuerpo JSON estándar devuelto en cualquier respuesta de error.
 */
public class ErrorResponse {
    private int estado;
    private String error;
    private String mensaje;
    private LocalDateTime timestamp;

    public ErrorResponse(int estado, String error, String mensaje) {
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

    public int getEstado()       { return estado; }
    public String getError()     { return error; }
    public String getMensaje()   { return mensaje; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
