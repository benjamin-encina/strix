package com.strix.msadjuntos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class AdjuntoResponseDTO {
    private Long id;
    private Long comentarioId;
    private Long ticketId;
    private String nombreArchivo;
    private String tipoMime;
    private Long tamanoBytes;
    private String rutaStorage;
    private LocalDateTime subidoEn;
}
