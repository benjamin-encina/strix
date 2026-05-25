package com.strix.mstickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EtiquetaRequestDTO {

    @NotBlank(message = "El nombre de la etiqueta es obligatorio")
    @Size(min = 1, max = 50, message = "La etiqueta debe tener entre 1 y 50 caracteres")
    private String nombre;
}