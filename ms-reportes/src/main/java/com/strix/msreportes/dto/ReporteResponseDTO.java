package com.strix.msreportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReporteResponseDTO {
    private Long totalTickets;
    private Map<String, Long> porEstado;
    private Map<String, Long> porTecnico;
    private Map<String, Long> porCategoria;
    private Map<String, Long> porPrioridad;
    private String periodoDesde;
    private String periodoHasta;
}
