package com.strix.msprioridades.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PrioridadRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El nivel es obligatorio")
    @Min(value = 1, message = "El nivel debe ser mayor a 0")
    private Integer nivel;

    @NotNull(message = "El SLA en horas es obligatorio")
    @Min(value = 1, message = "El SLA debe ser al menos 1 hora")
    private Integer slaHoras;
}
