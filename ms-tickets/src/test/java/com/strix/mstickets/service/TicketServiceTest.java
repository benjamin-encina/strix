package com.strix.mstickets.service;

import com.strix.mstickets.client.CategoriaClient;
import com.strix.mstickets.client.HistorialClient;
import com.strix.mstickets.client.PrioridadClient;
import com.strix.mstickets.dto.*;
import com.strix.mstickets.exception.RecursoNoEncontradoException;
import com.strix.mstickets.exception.ReglaNegocioException;
import com.strix.mstickets.model.Ticket;
import com.strix.mstickets.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de tickets.
 */

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private CategoriaClient  categoriaClient;
    @Mock private PrioridadClient  prioridadClient;
    @Mock private HistorialClient  historialClient;

    @InjectMocks
    private TicketService ticketService;

    private CategoriaDTO categoriaActiva;
    private PrioridadDTO prioridadActiva;

    @BeforeEach
    void setUp() {
        categoriaActiva = new CategoriaDTO(1L, "Hardware", "Descripción", true);
        prioridadActiva = new PrioridadDTO(1L, "Alta", 2, 8, true);
    }

    // PRUEBA 1 — Crear ticket exitosamente
    @Test
    @DisplayName("P1 - Crear ticket con categoria y prioridad validas debe retornar estado ABIERTO")
    void crear_conDatosValidos_retornaTicketAbierto() {
        TicketRequestDTO request = new TicketRequestDTO(
                "Monitor no enciende",
                "El monitor del puesto 5 no enciende desde esta mañana",
                1L, 1L, "monitor,hardware", null
        );

        // Ticket simulado que devuelve el repositorio al guardar
        Ticket ticketGuardado = new Ticket();
        ticketGuardado.setId(1L);
        ticketGuardado.setTitulo(request.getTitulo());
        ticketGuardado.setDescripcion(request.getDescripcion());
        ticketGuardado.setEstado(Ticket.Estado.ABIERTO);
        ticketGuardado.setCategoriaId(1L);
        ticketGuardado.setPrioridadId(1L);
        ticketGuardado.setUsuarioId(1L);
        ticketGuardado.setCreadoEn(LocalDateTime.now());
        ticketGuardado.setActualizadoEn(LocalDateTime.now());

        when(categoriaClient.obtenerCategoria(1L)).thenReturn(categoriaActiva);
        when(prioridadClient.obtenerPrioridad(1L)).thenReturn(prioridadActiva);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketGuardado);
        // historialClient.registrarCambio usa .subscribe() así que no necesita mock

        TicketResponseDTO resultado = ticketService.crear(request, 1L);

        assertNotNull(resultado);
        assertEquals("ABIERTO", resultado.getEstado());
        assertEquals("Monitor no enciende", resultado.getTitulo());

        // Verificar que el repositorio fue llamado para guardar
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    // PRUEBA 2 — Crear ticket con categoría inactiva debe lanzar excepción
    @Test
    @DisplayName("P2 - Crear ticket con categoria inactiva debe lanzar ReglaNegocioException")
    void crear_conCategoriaInactiva_lanzaExcepcion() {
        // DADO: una categoría que existe pero está inactiva
        CategoriaDTO categoriaInactiva = new CategoriaDTO(2L, "Obsoleta", "desc", false);

        TicketRequestDTO request = new TicketRequestDTO(
                "Problema de red", "No tengo conexión a internet en mi equipo",
                2L, 1L, null, null
        );

        when(categoriaClient.obtenerCategoria(2L)).thenReturn(categoriaInactiva);

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> ticketService.crear(request, 1L));

        assertTrue(ex.getMessage().contains("Categoría no existe o está inactiva"));

        verify(ticketRepository, never()).save(any());
    }

    // PRUEBA 3 — Cerrar ticket en estado incorrecto debe lanzar excepción
    @Test
    @DisplayName("P3 - Cerrar ticket ABIERTO debe lanzar ReglaNegocioException")
    void cerrar_ticketEnEstadoAbierto_lanzaExcepcion() {
        // DADO: un ticket en estado ABIERTO (no EN_PROCESO)
        Ticket ticketAbierto = new Ticket();
        ticketAbierto.setId(1L);
        ticketAbierto.setEstado(Ticket.Estado.ABIERTO);
        ticketAbierto.setTecnicoId(2L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketAbierto));

        CerrarTicketDTO cerrarDTO = new CerrarTicketDTO("El cable estaba desconectado");

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> ticketService.cerrar(1L, cerrarDTO, 2L));

        assertTrue(ex.getMessage().contains("Solo se pueden cerrar tickets en estado EN_PROCESO"));
        verify(ticketRepository, never()).save(any());
    }

    // PRUEBA 4 — Solo el técnico asignado puede cerrar el ticket
    @Test
    @DisplayName("P4 - Cerrar ticket con tecnico no asignado debe lanzar ReglaNegocioException")
    void cerrar_conTecnicoNoAsignado_lanzaExcepcion() {
        // DADO: ticket EN_PROCESO asignado al técnico ID=2
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEstado(Ticket.Estado.EN_PROCESO);
        ticket.setTecnicoId(2L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        CerrarTicketDTO cerrarDTO = new CerrarTicketDTO("Solución aplicada correctamente");

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> ticketService.cerrar(1L, cerrarDTO, 99L));

        assertTrue(ex.getMessage().contains("Solo el técnico asignado puede cerrar"));
        verify(ticketRepository, never()).save(any());
    }


    // PRUEBA 5 — No se puede cancelar un ticket ya cerrado
    @Test
    @DisplayName("P5 - Cancelar ticket CERRADO debe lanzar ReglaNegocioException")
    void cancelar_ticketCerrado_lanzaExcepcion() {
        // DADO: ticket en estado CERRADO (estado final)
        Ticket ticketCerrado = new Ticket();
        ticketCerrado.setId(1L);
        ticketCerrado.setEstado(Ticket.Estado.CERRADO);
        ticketCerrado.setUsuarioId(1L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketCerrado));

        CancelarTicketDTO cancelarDTO = new CancelarTicketDTO("Duplicado", null);

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> ticketService.cancelar(1L, cancelarDTO, 1L, false));

        assertTrue(ex.getMessage().contains("No se puede cancelar un ticket CERRADO"));
    }

    // PRUEBA 6 — Obtener ticket por ID inexistente debe lanzar excepción
    @Test
    @DisplayName("P6 - Asignar ticket inexistente debe lanzar RecursoNoEncontradoException")
    void asignar_ticketInexistente_lanzaExcepcion() {
        // DADO: el repositorio no encuentra el ticket
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        AsignarTicketDTO asignarDTO = new AsignarTicketDTO(2L);

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class,
                () -> ticketService.asignar(999L, asignarDTO, 1L));

        assertTrue(ex.getMessage().contains("Ticket no encontrado: 999"));
    }

    // PRUEBA 7 — Asignar ticket exitosamente cambia estado a EN_PROCESO

    @Test
    @DisplayName("P7 - Asignar ticket ABIERTO debe cambiar estado a EN_PROCESO")
    void asignar_ticketAbierto_cambiaEstadoAEnProceso() {
        // DADO: ticket en estado ABIERTO
        Ticket ticketAbierto = new Ticket();
        ticketAbierto.setId(1L);
        ticketAbierto.setEstado(Ticket.Estado.ABIERTO);
        ticketAbierto.setCategoriaId(1L);
        ticketAbierto.setPrioridadId(1L);
        ticketAbierto.setUsuarioId(1L);
        ticketAbierto.setCreadoEn(LocalDateTime.now());
        ticketAbierto.setActualizadoEn(LocalDateTime.now());

        Ticket ticketGuardado = new Ticket();
        ticketGuardado.setId(1L);
        ticketGuardado.setEstado(Ticket.Estado.EN_PROCESO);
        ticketGuardado.setTecnicoId(2L);
        ticketGuardado.setCategoriaId(1L);
        ticketGuardado.setPrioridadId(1L);
        ticketGuardado.setUsuarioId(1L);
        ticketGuardado.setCreadoEn(LocalDateTime.now());
        ticketGuardado.setActualizadoEn(LocalDateTime.now());

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketAbierto));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketGuardado);
        when(categoriaClient.obtenerCategoria(any())).thenReturn(categoriaActiva);
        when(prioridadClient.obtenerPrioridad(any())).thenReturn(prioridadActiva);

        AsignarTicketDTO dto = new AsignarTicketDTO(2L);

        // CUANDO: se asigna el ticket
        TicketResponseDTO resultado = ticketService.asignar(1L, dto, 1L);

        assertEquals("EN_PROCESO", resultado.getEstado());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    // PRUEBA 8 — Reabrir ticket que no está EN_PROCESO debe lanzar excepción
    @Test
    @DisplayName("P8 - Reabrir ticket CERRADO debe lanzar ReglaNegocioException")
    void reabrir_ticketCerrado_lanzaExcepcion() {
        // DADO: ticket en estado CERRADO
        Ticket ticketCerrado = new Ticket();
        ticketCerrado.setId(1L);
        ticketCerrado.setEstado(Ticket.Estado.CERRADO);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketCerrado));

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> ticketService.reabrir(1L, 1L, "Error en el cierre"));

        assertTrue(ex.getMessage().contains("Solo se pueden reabrir tickets en estado EN_PROCESO"));
        verify(ticketRepository, never()).save(any());
    }
}
