package com.strix.mstickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CancelarTicketDTO {
    @NotBlank(message = "El motivo de cancelación es obligatorio")
    private String motivo;

    @Size(max = 500)
    private String descripcionMotivo;
}
