package com.strix.mstickets.controller;

import com.strix.mstickets.dto.*;
import com.strix.mstickets.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // Creación de Ticket con adición de JWT
    @PostMapping
    public ResponseEntity<TicketResponseDTO> crear(
            @Valid @RequestBody TicketRequestDTO dto,
            @RequestHeader("X-Usuario-Id") Long usuarioId) {
        return ResponseEntity.status(201).body(ticketService.crear(dto, usuarioId));
    }

    // Listar Tickets Propios
    @GetMapping("/mis-tickets")
    public ResponseEntity<List<TicketResponseDTO>> misTickets(
            @RequestHeader("X-Usuario-Id") Long usuarioId) {
        return ResponseEntity.ok(ticketService.listarPorUsuario(usuarioId));
    }

    // Listar con filtros opcionales
    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> listarTodos(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long prioridadId,
            @RequestParam(required = false) Long tecnicoId) {
        return ResponseEntity.ok(ticketService.listarTodos(estado, categoriaId, prioridadId, tecnicoId));
    }

    // Ver detalle del ticket
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> obtener(@PathVariable Long id) {
        return ticketService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Reasignación de Ticket a Técnico
    @PatchMapping("/{id}/asignar")
    public ResponseEntity<TicketResponseDTO> asignar(
            @PathVariable Long id,
            @Valid @RequestBody AsignarTicketDTO dto,
            @RequestHeader("X-Usuario-Id") Long adminId) {
        return ResponseEntity.ok(ticketService.asignar(id, dto, adminId));
    }

    // Cerrar Ticket con Solución
    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<TicketResponseDTO> cerrar(
            @PathVariable Long id,
            @Valid @RequestBody CerrarTicketDTO dto,
            @RequestHeader("X-Usuario-Id") Long tecnicoId) {
        return ResponseEntity.ok(ticketService.cerrar(id, dto, tecnicoId));
    }

    // Cancelar Ticket
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<TicketResponseDTO> cancelar(
            @PathVariable Long id,
            @Valid @RequestBody CancelarTicketDTO dto,
            @RequestHeader("X-Usuario-Id") Long actorId,
            @RequestHeader(value = "X-Rol", defaultValue = "USUARIO") String rol) {
        boolean esAdmin = "ADMIN".equalsIgnoreCase(rol);
        return ResponseEntity.ok(ticketService.cancelar(id, dto, actorId, esAdmin));
    }

    // Reapertura de Ticket
    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<TicketResponseDTO> reabrir(
            @PathVariable Long id,
            @RequestHeader("X-Usuario-Id") Long adminId,
            @RequestBody(required = false) Map<String, String> body) {
        String justificacion = body != null ? body.get("justificacion") : null;
        return ResponseEntity.ok(ticketService.reabrir(id, adminId, justificacion));
    }
}
