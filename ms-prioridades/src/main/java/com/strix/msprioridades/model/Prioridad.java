package com.strix.msprioridades.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "prioridades")
public class Prioridad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
    @Column(nullable = false)
    private Integer nivel;
    @Column(name = "sla_horas", nullable = false)
    private Integer slaHoras;
    @Column(nullable = false)
    private Boolean activo = true;
    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();
}
