package com.strix.msreportes.service;

import com.strix.msreportes.client.TicketClient;
import com.strix.msreportes.dto.ReporteResponseDTO;
import com.strix.msreportes.dto.TicketResumenDTO;
import com.strix.msreportes.exception.ReglaNegocioException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final TicketClient ticketClient;

    // Estadisticas con Filtro Opcional
    public ReporteResponseDTO generarReporte(String desde, String hasta,
                                             Long categoriaId, Long prioridadId,
                                             Long tecnicoId) {
        log.info("Generando reporte: desde={}, hasta={}, categoriaId={}, prioridadId={}, tecnicoId={}",
                desde, hasta, categoriaId, prioridadId, tecnicoId);

        List<TicketResumenDTO> tickets = ticketClient.obtenerTodos(null, categoriaId, prioridadId, tecnicoId);
        log.info("Tickets obtenidos de ms-tickets: {}", tickets.size());

        // Filtro por rango de fechas
        if (desde != null && !desde.isBlank()) {
            try {
                LocalDateTime desdeDate = LocalDate.parse(desde).atStartOfDay();
                tickets = tickets.stream()
                        .filter(t -> t.getCreadoEn() != null && !t.getCreadoEn().isBefore(desdeDate))
                        .collect(Collectors.toList());
                log.info("Filtro 'desde' aplicado: {}", desde);
            } catch (DateTimeParseException e) {
                log.warn("Formato de fecha inválido para 'desde': {}", desde);
                throw new ReglaNegocioException("Formato de fecha inválido para 'desde'. Use yyyy-MM-dd.");
            }
        }
        if (hasta != null && !hasta.isBlank()) {
            try {
                LocalDateTime hastaDate = LocalDate.parse(hasta).atTime(23, 59, 59);
                tickets = tickets.stream()
                        .filter(t -> t.getCreadoEn() != null && !t.getCreadoEn().isAfter(hastaDate))
                        .collect(Collectors.toList());
                log.info("Filtro 'hasta' aplicado: {}", hasta);
            } catch (DateTimeParseException e) {
                log.warn("Formato de fecha inválido para 'hasta': {}", hasta);
                throw new ReglaNegocioException("Formato de fecha inválido para 'hasta'. Use yyyy-MM-dd.");
            }
        }

        Map<String, Long> porEstado    = tickets.stream().collect(Collectors.groupingBy(TicketResumenDTO::getEstado, Collectors.counting()));
        Map<String, Long> porTecnico   = tickets.stream().filter(t -> t.getTecnicoId() != null)
                .collect(Collectors.groupingBy(t -> "Tecnico_" + t.getTecnicoId(), Collectors.counting()));
        Map<String, Long> porCategoria = tickets.stream().filter(t -> t.getCategoriaNombre() != null)
                .collect(Collectors.groupingBy(TicketResumenDTO::getCategoriaNombre, Collectors.counting()));
        Map<String, Long> porPrioridad = tickets.stream().filter(t -> t.getPrioridadNombre() != null)
                .collect(Collectors.groupingBy(TicketResumenDTO::getPrioridadNombre, Collectors.counting()));

        log.info("Reporte generado: {} tickets totales", tickets.size());
        return new ReporteResponseDTO((long) tickets.size(), porEstado, porTecnico, porCategoria, porPrioridad, desde, hasta);
    }
}
