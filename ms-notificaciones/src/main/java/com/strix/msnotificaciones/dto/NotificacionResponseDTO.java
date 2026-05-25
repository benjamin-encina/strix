package com.strix.msnotificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class NotificacionResponseDTO {
    private Long id;
    private Long destinatarioId;
    private String tipo;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime creadaEn;
}
