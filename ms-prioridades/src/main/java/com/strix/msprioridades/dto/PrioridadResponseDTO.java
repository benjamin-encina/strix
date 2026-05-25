package com.strix.msprioridades.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PrioridadResponseDTO {
    private Long id;
    private String nombre;
    private Integer nivel;
    private Integer slaHoras;
    private Boolean activo;
}
