package com.strix.mstickets.repository;

import com.strix.mstickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Ticket propio del usuario
    List<Ticket> findByUsuarioId(Long usuarioId);

    // Ticket por técnico asignado
    List<Ticket> findByTecnicoId(Long tecnicoId);

    // Filtro para Admin
    List<Ticket> findByEstado(Ticket.Estado estado);

    List<Ticket> findByCategoriaId(Long categoriaId);

    List<Ticket> findByPrioridadId(Long prioridadId);

    @Query("SELECT t FROM Ticket t WHERE " +
           "(:estado IS NULL OR t.estado = :estado) AND " +
           "(:categoriaId IS NULL OR t.categoriaId = :categoriaId) AND " +
           "(:prioridadId IS NULL OR t.prioridadId = :prioridadId) AND " +
           "(:tecnicoId IS NULL OR t.tecnicoId = :tecnicoId)")
    List<Ticket> filtrar(@Param("estado") Ticket.Estado estado,
                         @Param("categoriaId") Long categoriaId,
                         @Param("prioridadId") Long prioridadId,
                         @Param("tecnicoId") Long tecnicoId);
}
