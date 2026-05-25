package com.strix.mshistorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "historial_tickets")
public class HistorialTicket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "estado_anterior", length = 15)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable = false, length = 15)
    private String estadoNuevo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(columnDefinition = "TEXT")
    private String justificacion;

    @Column(name = "registrado_en", nullable = false)
    private LocalDateTime registradoEn = LocalDateTime.now();
}
