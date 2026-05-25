package com.strix.msnotificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class NotificacionRequestDTO {
    @NotNull(message = "El Id de Destinatario es obligatorio")
    private Long destinatarioId;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}
