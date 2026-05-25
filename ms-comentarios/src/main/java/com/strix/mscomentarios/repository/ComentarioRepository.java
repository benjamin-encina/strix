package com.strix.mscomentarios.repository;

import com.strix.mscomentarios.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    // Hilo en orden cronológico ascendente.
    List<Comentario> findByTicketIdOrderByCreadoEnAsc(Long ticketId);
}
