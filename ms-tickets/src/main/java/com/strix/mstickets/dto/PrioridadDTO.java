package com.strix.mstickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PrioridadDTO {
    private Long id;
    private String nombre;
    private Integer nivel;
    private Integer slaHoras;
    private Boolean activo;
}
