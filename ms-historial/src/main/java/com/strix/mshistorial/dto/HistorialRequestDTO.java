package com.strix.mshistorial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class HistorialRequestDTO {
    @NotNull(message = "El ticketId es obligatorio")
    private Long ticketId;
    private String estadoAnterior;
    @NotBlank(message = "El estadoNuevo es obligatorio")
    private String estadoNuevo;
    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;
    private String justificacion;
}
