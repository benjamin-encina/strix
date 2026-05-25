package com.strix.msreportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class TicketResumenDTO {
    private Long id;
    private String estado;
    private Long categoriaId;
    private String categoriaNombre;
    private Long prioridadId;
    private String prioridadNombre;
    private Long tecnicoId;
    private LocalDateTime creadoEn;
    private LocalDateTime cerradoEn;
}
