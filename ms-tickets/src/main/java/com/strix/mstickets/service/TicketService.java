package com.strix.mstickets.service;

import com.strix.mstickets.client.CategoriaClient;
import com.strix.mstickets.client.HistorialClient;
import com.strix.mstickets.client.PrioridadClient;
import com.strix.mstickets.dto.*;
import com.strix.mstickets.exception.RecursoNoEncontradoException;
import com.strix.mstickets.exception.ReglaNegocioException;
import com.strix.mstickets.model.Ticket;
import com.strix.mstickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final CategoriaClient categoriaClient;
    private final PrioridadClient prioridadClient;
    private final HistorialClient historialClient;

    // - Mapeo -

    private TicketResponseDTO mapear(Ticket t) {
        CategoriaDTO cat = null;
        PrioridadDTO pri = null;
        try { cat = categoriaClient.obtenerCategoria(t.getCategoriaId()); } catch (Exception e) {
            log.warn("No se pudo obtener categoria {} para ticket {}: {}", t.getCategoriaId(), t.getId(), e.getMessage());
        }
        try { pri = prioridadClient.obtenerPrioridad(t.getPrioridadId()); } catch (Exception e) {
            log.warn("No se pudo obtener prioridad {} para ticket {}: {}", t.getPrioridadId(), t.getId(), e.getMessage());
        }

        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(t.getId());
        dto.setTitulo(t.getTitulo());
        dto.setDescripcion(t.getDescripcion());
        dto.setEstado(t.getEstado().name());
        dto.setCategoriaId(t.getCategoriaId());
        dto.setCategoriaNombre(cat != null ? cat.getNombre() : "N/A");
        dto.setPrioridadId(t.getPrioridadId());
        dto.setPrioridadNombre(pri != null ? pri.getNombre() : "N/A");
        dto.setSlaHoras(pri != null ? pri.getSlaHoras() : null);
        dto.setUsuarioId(t.getUsuarioId());
        dto.setTecnicoId(t.getTecnicoId());
        dto.setSolucion(t.getSolucion());
        dto.setEtiquetas(t.getEtiquetas());
        dto.setCreadoEn(t.getCreadoEn());
        dto.setActualizadoEn(t.getActualizadoEn());
        dto.setCerradoEn(t.getCerradoEn());
        return dto;
    }

    // -Creación de Ticket-

    public TicketResponseDTO crear(TicketRequestDTO dto, Long usuarioId) {
        log.info("Creando ticket para usuario {} con categoría {} y prioridad {}",
                usuarioId, dto.getCategoriaId(), dto.getPrioridadId());

        // RN-T02: validar categoría existe
        CategoriaDTO cat = categoriaClient.obtenerCategoria(dto.getCategoriaId());
        if (cat == null || !cat.getActivo()) {
            log.warn("Categoría {} no existe o está inactiva", dto.getCategoriaId());
            throw new ReglaNegocioException("Categoría no existe o está inactiva: " + dto.getCategoriaId());
        }
        // RN-T03: validar prioridad existe
        PrioridadDTO pri = prioridadClient.obtenerPrioridad(dto.getPrioridadId());
        if (pri == null || !pri.getActivo()) {
            log.warn("Prioridad {} no existe o está inactiva", dto.getPrioridadId());
            throw new ReglaNegocioException("Prioridad no existe o está inactiva: " + dto.getPrioridadId());
        }

        Ticket t = new Ticket();
        t.setTitulo(dto.getTitulo());
        t.setDescripcion(dto.getDescripcion());
        t.setEstado(Ticket.Estado.ABIERTO); // RN-T01
        t.setCategoriaId(dto.getCategoriaId());
        t.setPrioridadId(dto.getPrioridadId());
        t.setUsuarioId(usuarioId);           // RN-T04
        t.setEtiquetas(dto.getEtiquetas());
        t.setCreadoEn(LocalDateTime.now());
        t.setActualizadoEn(LocalDateTime.now());

        Ticket guardado = ticketRepository.save(t);
        log.info("Ticket creado con ID {} en estado ABIERTO", guardado.getId());

        // Registrar creación en historial (RF-22)
        historialClient.registrarCambio(guardado.getId(), null, "ABIERTO", usuarioId, "Ticket creado");

        return mapear(guardado);
    }

    // ─── RF-09: Tickets propios ───────────────────────────────────────────────

    public List<TicketResponseDTO> listarPorUsuario(Long usuarioId) {
        log.info("Listando tickets del usuario {}", usuarioId);
        return ticketRepository.findByUsuarioId(usuarioId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }

    // ─── RF-10: Todos los tickets con filtros (ADMIN) ─────────────────────────

    public List<TicketResponseDTO> listarTodos(String estado, Long categoriaId,
                                               Long prioridadId, Long tecnicoId) {
        Ticket.Estado estadoEnum = null;
        if (estado != null && !estado.isBlank()) {
            try {
                estadoEnum = Ticket.Estado.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Estado de filtro inválido: {}", estado);
                throw new ReglaNegocioException("Estado inválido: " + estado +
                        ". Valores permitidos: ABIERTO, EN_PROCESO, CERRADO, CANCELADO");
            }
        }
        log.info("Listando tickets con filtros: estado={}, categoriaId={}, prioridadId={}, tecnicoId={}",
                estado, categoriaId, prioridadId, tecnicoId);
        return ticketRepository.filtrar(estadoEnum, categoriaId, prioridadId, tecnicoId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }

    // ─── RF-11: Ver detalle ───────────────────────────────────────────────────

    public Optional<TicketResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando ticket con ID {}", id);
        return ticketRepository.findById(id).map(this::mapear);
    }

    // ─── RF-13: Asignar ticket a técnico (ADMIN) ──────────────────────────────

    public TicketResponseDTO asignar(Long ticketId, AsignarTicketDTO dto, Long adminId) {
        log.info("Admin {} asignando ticket {} a técnico {}", adminId, ticketId, dto.getTecnicoId());
        Ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado: " + ticketId));

        if (t.getEstado() != Ticket.Estado.ABIERTO && t.getEstado() != Ticket.Estado.EN_PROCESO) {
            log.warn("Intento de asignar ticket {} en estado {}", ticketId, t.getEstado());
            throw new ReglaNegocioException("Solo se pueden asignar tickets en estado ABIERTO o EN_PROCESO");
        }

        String estadoAnterior = t.getEstado().name();
        t.setTecnicoId(dto.getTecnicoId());
        t.setEstado(Ticket.Estado.EN_PROCESO);
        t.setActualizadoEn(LocalDateTime.now());

        Ticket guardado = ticketRepository.save(t);
        log.info("Ticket {} asignado a técnico {}, nuevo estado: EN_PROCESO", ticketId, dto.getTecnicoId());
        historialClient.registrarCambio(ticketId, estadoAnterior, "EN_PROCESO", adminId,
                "Asignado a técnico ID: " + dto.getTecnicoId());

        return mapear(guardado);
    }

    // ─── RF-15: Cerrar ticket con solución (TÉCNICO) ──────────────────────────

    public TicketResponseDTO cerrar(Long ticketId, CerrarTicketDTO dto, Long tecnicoId) {
        log.info("Técnico {} cerrando ticket {}", tecnicoId, ticketId);
        Ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado: " + ticketId));

        if (t.getEstado() != Ticket.Estado.EN_PROCESO) {
            log.warn("Intento de cerrar ticket {} en estado {}", ticketId, t.getEstado());
            throw new ReglaNegocioException("Solo se pueden cerrar tickets en estado EN_PROCESO");
        }
        if (!tecnicoId.equals(t.getTecnicoId())) {
            log.warn("Técnico {} intentó cerrar ticket {} asignado al técnico {}", tecnicoId, ticketId, t.getTecnicoId());
            throw new ReglaNegocioException("Solo el técnico asignado puede cerrar este ticket");
        }
        // RN-U04: solución obligatoria
        if (dto.getSolucion() == null || dto.getSolucion().isBlank()) {
            log.warn("Intento de cerrar ticket {} sin solución", ticketId);
            throw new ReglaNegocioException("La solución aplicada es obligatoria al cerrar un ticket");
        }

        String estadoAnterior = t.getEstado().name();
        t.setEstado(Ticket.Estado.CERRADO);
        t.setSolucion(dto.getSolucion());
        t.setActualizadoEn(LocalDateTime.now());
        t.setCerradoEn(LocalDateTime.now());

        Ticket guardado = ticketRepository.save(t);
        log.info("Ticket {} cerrado correctamente por técnico {}", ticketId, tecnicoId);
        historialClient.registrarCambio(ticketId, estadoAnterior, "CERRADO", tecnicoId,
                "Ticket cerrado con solución registrada");

        return mapear(guardado);
    }

    // ─── RF-12: Cancelar ticket ───────────────────────────────────────────────

    public TicketResponseDTO cancelar(Long ticketId, CancelarTicketDTO dto, Long actorId, boolean esAdmin) {
        log.info("Actor {} (admin={}) cancelando ticket {}", actorId, esAdmin, ticketId);
        Ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado: " + ticketId));

        // RN-D02: CERRADO es final
        if (t.getEstado() == Ticket.Estado.CERRADO) {
            log.warn("Intento de cancelar ticket CERRADO: {}", ticketId);
            throw new ReglaNegocioException("No se puede cancelar un ticket CERRADO");
        }
        // RN-D01: usuario solo puede cancelar sus propios tickets en ABIERTO
        if (!esAdmin) {
            if (!actorId.equals(t.getUsuarioId())) {
                log.warn("Usuario {} intentó cancelar ticket {} de usuario {}", actorId, ticketId, t.getUsuarioId());
                throw new ReglaNegocioException("Solo puede cancelar sus propios tickets");
            }
            if (t.getEstado() != Ticket.Estado.ABIERTO) {
                log.warn("Usuario {} intentó cancelar ticket {} en estado {}", actorId, ticketId, t.getEstado());
                throw new ReglaNegocioException("Solo puede cancelar tickets en estado ABIERTO");
            }
        }
        if ("Otro".equalsIgnoreCase(dto.getMotivo()) &&
                (dto.getDescripcionMotivo() == null || dto.getDescripcionMotivo().isBlank())) {
            throw new ReglaNegocioException("La descripción del motivo es obligatoria cuando el motivo es 'Otro'");
        }

        String estadoAnterior = t.getEstado().name();
        t.setEstado(Ticket.Estado.CANCELADO);
        t.setActualizadoEn(LocalDateTime.now());

        Ticket guardado = ticketRepository.save(t);
        log.info("Ticket {} cancelado. Motivo: {}", ticketId, dto.getMotivo());
        historialClient.registrarCambio(ticketId, estadoAnterior, "CANCELADO", actorId,
                dto.getMotivo() + ": " + (dto.getDescripcionMotivo() != null ? dto.getDescripcionMotivo() : ""));

        return mapear(guardado);
    }

    // ─── RF-16: Reabrir ticket (ADMIN) ───────────────────────────────────────

    public TicketResponseDTO reabrir(Long ticketId, Long adminId, String justificacion) {
        log.info("Admin {} reabriendo ticket {}", adminId, ticketId);
        Ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado: " + ticketId));

        if (t.getEstado() != Ticket.Estado.EN_PROCESO) {
            log.warn("Intento de reabrir ticket {} en estado {}", ticketId, t.getEstado());
            throw new ReglaNegocioException("Solo se pueden reabrir tickets en estado EN_PROCESO");
        }

        String estadoAnterior = t.getEstado().name();
        t.setEstado(Ticket.Estado.ABIERTO);
        t.setTecnicoId(null);
        t.setActualizadoEn(LocalDateTime.now());

        Ticket guardado = ticketRepository.save(t);
        log.info("Ticket {} reabierto por admin {}, estado: ABIERTO", ticketId, adminId);
        historialClient.registrarCambio(ticketId, estadoAnterior, "ABIERTO", adminId,
                justificacion != null ? justificacion : "Ticket reabierto por administrador");

        return mapear(guardado);
    }
}
