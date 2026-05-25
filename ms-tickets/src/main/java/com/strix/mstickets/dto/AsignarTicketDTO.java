package com.strix.mstickets.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AsignarTicketDTO {
    @NotNull(message = "El id del técnico es obligatorio")
    private Long tecnicoId;
}
