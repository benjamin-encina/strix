package com.strix.mstickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CerrarTicketDTO {
    @NotBlank(message = "La solución aplicada es obligatoria al cerrar un ticket")
    @Size(min = 10, max = 3000, message = "La solución debe tener entre 10 y 3000 caracteres")
    private String solucion;
}
