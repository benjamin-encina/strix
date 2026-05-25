package com.strix.mshistorial.service;

import com.strix.mshistorial.dto.HistorialRequestDTO;
import com.strix.mshistorial.dto.HistorialResponseDTO;
import com.strix.mshistorial.model.HistorialTicket;
import com.strix.mshistorial.repository.HistorialTicketRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistorialService {

    private static final Logger log = LoggerFactory.getLogger(HistorialService.class);

    private final HistorialTicketRepository repository;

    private HistorialResponseDTO mapear(HistorialTicket h) {
        return new HistorialResponseDTO(
                h.getId(), h.getTicketId(), h.getEstadoAnterior(),
                h.getEstadoNuevo(), h.getUsuarioId(),
                h.getJustificacion(), h.getRegistradoEn()
        );
    }

    // RF-22: Registrar cambio de estado (llamado internamente desde ms-tickets)
    public HistorialResponseDTO registrar(HistorialRequestDTO dto) {
        log.info("Registrando cambio en ticket {}: {} -> {} (usuario {})",
                dto.getTicketId(), dto.getEstadoAnterior(), dto.getEstadoNuevo(), dto.getUsuarioId());
        HistorialTicket h = new HistorialTicket(
                null, dto.getTicketId(), dto.getEstadoAnterior(),
                dto.getEstadoNuevo(), dto.getUsuarioId(),
                dto.getJustificacion(), LocalDateTime.now()
        );
        HistorialTicket guardado = repository.save(h);
        log.info("Entrada historial {} guardada para ticket {}", guardado.getId(), dto.getTicketId());
        return mapear(guardado);
    }

    // RF-23: Consultar historial completo de un ticket
    public List<HistorialResponseDTO> obtenerPorTicket(Long ticketId) {
        log.info("Consultando historial del ticket {}", ticketId);
        return repository.findByTicketIdOrderByRegistradoEnAsc(ticketId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }
}
