package com.strix.mshistorial.repository;

import com.strix.mshistorial.model.HistorialTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialTicketRepository extends JpaRepository<HistorialTicket, Long> {
    // RF-23: historial por ticket ordenado ascendente
    List<HistorialTicket> findByTicketIdOrderByRegistradoEnAsc(Long ticketId);
}
