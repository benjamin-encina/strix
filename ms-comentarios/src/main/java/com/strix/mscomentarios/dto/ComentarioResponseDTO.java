package com.strix.mscomentarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class ComentarioResponseDTO {
    private Long id;
    private Long ticketId;
    private Long usuarioId;
    private String contenido;
    private String tipo;
    private LocalDateTime creadoEn;
}
