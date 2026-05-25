package com.strix.mscomentarios.controller;

import com.strix.mscomentarios.dto.ComentarioRequestDTO;
import com.strix.mscomentarios.dto.ComentarioResponseDTO;
import com.strix.mscomentarios.service.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService service;

    // Se agrega el comentario
    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> agregar(
            @Valid @RequestBody ComentarioRequestDTO dto,
            @RequestHeader("X-Usuario-Id") Long usuarioId) {
        return ResponseEntity.status(201).body(service.agregar(dto, usuarioId));
    }

    // Revisar los comentarios de un ticket específico
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(service.listarPorTicket(ticketId));
    }
}
