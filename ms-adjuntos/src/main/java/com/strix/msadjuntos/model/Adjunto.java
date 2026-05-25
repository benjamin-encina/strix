package com.strix.msadjuntos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un archivo adjunto subido por un usuario
 * dentro del contexto de un comentario en un ticket.
 *
 * Relación JPA: Adjunto @ManyToOne ComentarioRef
 * (muchos adjuntos pueden pertenecer a un mismo comentario)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adjuntos")
public class Adjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación @ManyToOne con ComentarioRef.
     * Muchos adjuntos pertenecen a un mismo comentario.
     * La FK se almacena como comentario_id en la tabla 'adjuntos'.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comentario_id", nullable = false)
    private ComentarioRef comentario;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "tipo_mime", nullable = false, length = 100)
    private String tipoMime;

    @Column(name = "tamano_bytes", nullable = false)
    private Long tamanoBytes;

    @Column(name = "ruta_storage", nullable = false, length = 500)
    private String rutaStorage;

    @Column(name = "subido_en", nullable = false)
    private LocalDateTime subidoEn = LocalDateTime.now();

    // ── Getters de conveniencia ──
    public Long getComentarioId() {
        return comentario != null ? comentario.getId() : null;
    }
}
