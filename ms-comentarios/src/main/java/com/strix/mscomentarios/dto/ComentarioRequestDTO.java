package com.strix.mscomentarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ComentarioRequestDTO {

    @NotNull(message = "El ID del Ticket es obligatorio")
    private Long ticketId;

    @NotBlank(message = "El contenido del comentario es obligatorio")
    private String contenido;

    // Tipo: USUARIO o SISTEMA (default USUARIO)
    private String tipo = "USUARIO";
}
