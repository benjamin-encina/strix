package com.strix.msreportes.service;

import com.strix.msreportes.client.TicketClient;
import com.strix.msreportes.dto.ReporteResponseDTO;
import com.strix.msreportes.dto.TicketResumenDTO;
import com.strix.msreportes.exception.ReglaNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de reportes.
 * ms-reportes depende completamente de ms-tickets via WebClient —
 * se testea que la lógica de filtrado y agrupamiento funcione
 * correctamente con los datos que ms-tickets devuelve.
 */
@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock private TicketClient ticketClient;

    @InjectMocks
    private ReporteService reporteService;

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 15 — Reporte sin filtros devuelve el total correcto
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P15 - Reporte sin filtros debe contar correctamente todos los tickets")
    void generarReporte_sinFiltros_contaTodosLosTickets() {
        // DADO: ms-tickets devuelve 3 tickets con distintos estados
        TicketResumenDTO t1 = new TicketResumenDTO(1L, "ABIERTO",    1L, "Hardware", 1L, "Alta",  null, LocalDateTime.now(), null);
        TicketResumenDTO t2 = new TicketResumenDTO(2L, "EN_PROCESO", 1L, "Hardware", 2L, "Media", 2L,   LocalDateTime.now(), null);
        TicketResumenDTO t3 = new TicketResumenDTO(3L, "CERRADO",    2L, "Software", 3L, "Baja",  2L,   LocalDateTime.now(), LocalDateTime.now());

        when(ticketClient.obtenerTodos(null, null, null, null))
                .thenReturn(List.of(t1, t2, t3));

        // CUANDO: se genera el reporte sin filtros
        ReporteResponseDTO resultado = reporteService.generarReporte(null, null, null, null, null);

        // ENTONCES: el total debe ser 3 y los agrupamientos correctos
        assertEquals(3L, resultado.getTotalTickets());
        assertEquals(1L, resultado.getPorEstado().get("ABIERTO"));
        assertEquals(1L, resultado.getPorEstado().get("EN_PROCESO"));
        assertEquals(1L, resultado.getPorEstado().get("CERRADO"));
        assertEquals(2L, resultado.getPorCategoria().get("Hardware"));
        assertEquals(1L, resultado.getPorCategoria().get("Software"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 16 — Reporte con lista vacía devuelve totales en cero
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P16 - Reporte sin tickets debe retornar total cero y mapas vacios")
    void generarReporte_sinTickets_retornaCero() {
        // DADO: ms-tickets devuelve lista vacía
        when(ticketClient.obtenerTodos(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        // CUANDO: se genera el reporte
        ReporteResponseDTO resultado = reporteService.generarReporte(null, null, null, null, null);

        // ENTONCES: el total es 0 y todos los mapas están vacíos
        assertEquals(0L, resultado.getTotalTickets());
        assertTrue(resultado.getPorEstado().isEmpty());
        assertTrue(resultado.getPorTecnico().isEmpty());
        assertTrue(resultado.getPorCategoria().isEmpty());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 17 — Filtro de fecha inválida debe lanzar excepción
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P17 - Reporte con fecha en formato incorrecto debe lanzar ReglaNegocioException")
    void generarReporte_conFechaInvalida_lanzaExcepcion() {
        // DADO: ms-tickets devuelve tickets normalmente
        TicketResumenDTO t1 = new TicketResumenDTO(1L, "ABIERTO", 1L, "Hardware", 1L, "Alta", null, LocalDateTime.now(), null);
        when(ticketClient.obtenerTodos(any(), any(), any(), any())).thenReturn(List.of(t1));

        // CUANDO: se envía una fecha con formato incorrecto (dd/MM/yyyy en vez de yyyy-MM-dd)
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> reporteService.generarReporte("25/05/2026", null, null, null, null));

        // ENTONCES: el service rechaza el formato con mensaje claro
        assertTrue(ex.getMessage().contains("Formato de fecha inválido para 'desde'"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 18 — Agrupamiento por técnico cuenta solo tickets asignados
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P18 - Reporte debe agrupar por tecnico solo tickets con tecnico asignado")
    void generarReporte_agrupaPorTecnico_ignoraTicketsSinTecnico() {
        // DADO: 2 tickets con técnico ID=2 y 1 sin técnico (recién creado)
        TicketResumenDTO conTecnico1  = new TicketResumenDTO(1L, "EN_PROCESO", 1L, "Hardware", 1L, "Alta", 2L, LocalDateTime.now(), null);
        TicketResumenDTO conTecnico2  = new TicketResumenDTO(2L, "CERRADO",    1L, "Hardware", 1L, "Alta", 2L, LocalDateTime.now(), LocalDateTime.now());
        TicketResumenDTO sinTecnico   = new TicketResumenDTO(3L, "ABIERTO",    2L, "Software", 2L, "Baja", null, LocalDateTime.now(), null);

        when(ticketClient.obtenerTodos(any(), any(), any(), any()))
                .thenReturn(List.of(conTecnico1, conTecnico2, sinTecnico));

        // CUANDO: se genera el reporte
        ReporteResponseDTO resultado = reporteService.generarReporte(null, null, null, null, null);

        // ENTONCES: solo aparece el técnico con tickets asignados
        assertEquals(3L, resultado.getTotalTickets());
        assertEquals(2L, resultado.getPorTecnico().get("Tecnico_2"));
        // El ticket sin técnico no debe generar entrada en porTecnico
        assertEquals(1, resultado.getPorTecnico().size());
    }
}
