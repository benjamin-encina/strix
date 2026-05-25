package com.strix.msprioridades.controller;

import com.strix.msprioridades.dto.PrioridadRequestDTO;
import com.strix.msprioridades.dto.PrioridadResponseDTO;
import com.strix.msprioridades.service.PrioridadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prioridades")
@RequiredArgsConstructor
public class PrioridadController {

    private final PrioridadService service;

    @GetMapping
    public ResponseEntity<List<PrioridadResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrioridadResponseDTO> obtener(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // RF-21: CRUD Prioridades (ADMIN)
    @PostMapping
    public ResponseEntity<PrioridadResponseDTO> crear(@Valid @RequestBody PrioridadRequestDTO dto) {
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrioridadResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody PrioridadRequestDTO dto) {
        return ResponseEntity.ok(service.editar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
