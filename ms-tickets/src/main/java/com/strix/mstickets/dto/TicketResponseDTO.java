package com.strix.mstickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private Long categoriaId;
    private String categoriaNombre;   // enriquecido via WebClient
    private Long prioridadId;
    private String prioridadNombre;   // enriquecido via WebClient
    private Integer slaHoras;
    private Long usuarioId;
    private Long tecnicoId;
    private String solucion;
    private String etiquetas;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
    private LocalDateTime cerradoEn;
}
