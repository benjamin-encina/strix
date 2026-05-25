package com.strix.msnotificaciones.controller;

import com.strix.msnotificaciones.dto.NotificacionRequestDTO;
import com.strix.msnotificaciones.dto.NotificacionResponseDTO;
import com.strix.msnotificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService service;

    // Es nuestro Endpoint interno, recibe las notificaciones desde los otros microservicios
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crear(@Valid @RequestBody NotificacionRequestDTO dto) {
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    // Listar todas las notificaciones de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    // Listar solo las no leídas
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<NotificacionResponseDTO>> noLeidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarNoLeidas(usuarioId));
    }

    // Marcar como leída
    @PatchMapping("/{id}/leer")
    public ResponseEntity<NotificacionResponseDTO> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarLeida(id));
    }
}
