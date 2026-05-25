package com.strix.msreportes.controller;

import com.strix.msreportes.dto.ReporteResponseDTO;
import com.strix.msreportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService service;

    /**
     * Estadisticas Generales de Tickets
     * Se filtra por rangos de fechas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ReporteResponseDTO> estadisticas(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long prioridadId,
            @RequestParam(required = false) Long tecnicoId) {

        return ResponseEntity.ok(
                service.generarReporte(desde, hasta, categoriaId, prioridadId, tecnicoId)
        );
    }
}
