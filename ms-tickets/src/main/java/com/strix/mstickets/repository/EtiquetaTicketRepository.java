package com.strix.mstickets.repository;

import com.strix.mstickets.model.EtiquetaTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtiquetaTicketRepository extends JpaRepository<EtiquetaTicket, Long> {
    List<EtiquetaTicket> findByTicketId(Long ticketId);
}