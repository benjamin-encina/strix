package com.strix.msadjuntos.repository;

import com.strix.msadjuntos.model.Adjunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdjuntoRepository extends JpaRepository<Adjunto, Long> {
    List<Adjunto> findByComentario_Id(Long comentarioId);
    List<Adjunto> findByTicketId(Long ticketId);
}
