package com.strix.mstickets.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "tickets")
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Estado estado = Estado.ABIERTO;

    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @Column(name = "prioridad_id", nullable = false)
    private Long prioridadId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tecnico_id")
    private Long tecnicoId;

    @Column(columnDefinition = "TEXT")
    private String solucion;

    @Column(length = 500)
    private String etiquetas;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn = LocalDateTime.now();

    @Column(name = "cerrado_en")
    private LocalDateTime cerradoEn;

    public enum Estado { ABIERTO, EN_PROCESO, CERRADO, CANCELADO }
}
