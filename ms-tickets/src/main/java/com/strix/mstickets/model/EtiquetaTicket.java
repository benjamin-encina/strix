package com.strix.mstickets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "etiquetas_ticket")
public class EtiquetaTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_etiqueta_ticket"))
    private Ticket ticket;

    @NotBlank(message = "La etiqueta no puede estar vacía")
    @Size(max = 50, message = "La etiqueta no puede superar 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;
}