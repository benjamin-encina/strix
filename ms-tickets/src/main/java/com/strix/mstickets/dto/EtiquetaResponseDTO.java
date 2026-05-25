package com.strix.mstickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EtiquetaResponseDTO {
    private Long id;
    private Long ticketId;
    private String nombre;
}