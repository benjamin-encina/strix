package com.strix.msusuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String nombre;
    private String email;
    private String rol;
}
