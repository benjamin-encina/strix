package com.strix.mshistorial.controller;

import com.strix.mshistorial.dto.HistorialRequestDTO;
import com.strix.mshistorial.dto.HistorialResponseDTO;
import com.strix.mshistorial.service.HistorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final HistorialService service;

    // RF-22: Recibe cambios de estado desde ms-tickets
    @PostMapping
    public ResponseEntity<HistorialResponseDTO> registrar(@Valid @RequestBody HistorialRequestDTO dto) {
        return ResponseEntity.status(201).body(service.registrar(dto));
    }

    // RF-23: Consultar historial de un ticket
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<HistorialResponseDTO>> obtenerPorTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(service.obtenerPorTicket(ticketId));
    }
}
