package com.strix.msadjuntos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de referencia local al ms-adjuntos.
 * Representa al Comentario al que pertenece un Adjunto dentro de este microservicio.
 * La tabla 'comentarios_ref' es un reflejo local de la clave primaria de ms-comentarios,
 * lo que permite establecer la relación @ManyToOne en el ORM sin cruzar microservicios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comentarios_ref")
public class ComentarioRef {

    @Id
    private Long id; // mismo ID que en ms-comentarios

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;
}
