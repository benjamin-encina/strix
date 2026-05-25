package com.strix.mstickets.controller;

import com.strix.mstickets.dto.EtiquetaRequestDTO;
import com.strix.mstickets.dto.EtiquetaResponseDTO;
import com.strix.mstickets.service.EtiquetaTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/etiquetas")
@RequiredArgsConstructor
public class EtiquetaTicketController {

    private final EtiquetaTicketService service;

    @GetMapping
    public ResponseEntity<List<EtiquetaResponseDTO>> listar(@PathVariable Long ticketId) {
        return ResponseEntity.ok(service.listarPorTicket(ticketId));
    }

    @PostMapping
    public ResponseEntity<EtiquetaResponseDTO> agregar(
            @PathVariable Long ticketId,
            @Valid @RequestBody EtiquetaRequestDTO dto) {
        return ResponseEntity.status(201).body(service.agregar(ticketId, dto));
    }

    @DeleteMapping("/{etiquetaId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long ticketId,
            @PathVariable Long etiquetaId) {
        service.eliminar(etiquetaId);
        return ResponseEntity.noContent().build();
    }
}