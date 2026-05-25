package com.strix.mshistorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class HistorialResponseDTO {
    private Long id;
    private Long ticketId;
    private String estadoAnterior;
    private String estadoNuevo;
    private Long usuarioId;
    private String justificacion;
    private LocalDateTime registradoEn;
}
