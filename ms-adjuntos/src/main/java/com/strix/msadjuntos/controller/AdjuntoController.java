package com.strix.msadjuntos.controller;

import com.strix.msadjuntos.dto.AdjuntoResponseDTO;
import com.strix.msadjuntos.service.AdjuntoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/adjuntos")
@RequiredArgsConstructor
public class AdjuntoController {

    private final AdjuntoService service;

    // RF-18: Subir archivo adjunto a un comentario
    @PostMapping("/upload")
    public ResponseEntity<AdjuntoResponseDTO> subir(
            @RequestParam("comentarioId") Long comentarioId,
            @RequestParam("ticketId") Long ticketId,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(201).body(service.subirAdjunto(comentarioId, ticketId, file));
    }

    // Listar adjuntos de un comentario específico
    @GetMapping("/comentario/{comentarioId}")
    public ResponseEntity<List<AdjuntoResponseDTO>> porComentario(@PathVariable Long comentarioId) {
        return ResponseEntity.ok(service.listarPorComentario(comentarioId));
    }

    // Listar todos los adjuntos de un ticket
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<AdjuntoResponseDTO>> porTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(service.listarPorTicket(ticketId));
    }
}
