package com.strix.mstickets.service;

import com.strix.mstickets.dto.EtiquetaRequestDTO;
import com.strix.mstickets.dto.EtiquetaResponseDTO;
import com.strix.mstickets.model.EtiquetaTicket;
import com.strix.mstickets.model.Ticket;
import com.strix.mstickets.repository.EtiquetaTicketRepository;
import com.strix.mstickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EtiquetaTicketService {

    private final EtiquetaTicketRepository etiquetaRepository;
    private final TicketRepository ticketRepository;

    private EtiquetaResponseDTO mapear(EtiquetaTicket e) {
        return new EtiquetaResponseDTO(
                e.getId(),
                e.getTicket().getId(),
                e.getNombre());
    }

    public List<EtiquetaResponseDTO> listarPorTicket(Long ticketId) {
        log.info("[EtiquetaService] Listando etiquetas para ticketId={}", ticketId);
        return etiquetaRepository.findByTicketId(ticketId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }

    public EtiquetaResponseDTO agregar(Long ticketId, EtiquetaRequestDTO dto) {
        log.info("[EtiquetaService] Agregando etiqueta '{}' a ticketId={}", dto.getNombre(), ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado: " + ticketId));
        EtiquetaTicket etiqueta = new EtiquetaTicket(null, ticket, dto.getNombre());
        EtiquetaResponseDTO guardada = mapear(etiquetaRepository.save(etiqueta));
        log.info("[EtiquetaService] Etiqueta ID={} agregada a ticketId={}", guardada.getId(), ticketId);
        return guardada;
    }

    public void eliminar(Long etiquetaId) {
        log.info("[EtiquetaService] Eliminando etiqueta ID={}", etiquetaId);
        if (!etiquetaRepository.existsById(etiquetaId)) {
            throw new RuntimeException("Etiqueta no encontrada: " + etiquetaId);
        }
        etiquetaRepository.deleteById(etiquetaId);
        log.info("[EtiquetaService] Etiqueta ID={} eliminada", etiquetaId);
    }
}