package com.strix.mscategorias.service;

import com.strix.mscategorias.dto.CategoriaRequestDTO;
import com.strix.mscategorias.dto.CategoriaResponseDTO;
import com.strix.mscategorias.exception.RecursoNoEncontradoException;
import com.strix.mscategorias.exception.ReglaNegocioException;
import com.strix.mscategorias.model.Categoria;
import com.strix.mscategorias.repository.CategoriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de categorías.
 * ms-categorias es dependencia directa de ms-tickets —
 * si falla, ms-tickets no puede crear tickets.
 */
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService categoriaService;

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 12 — Crear categoría con nombre único debe guardarse
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P12 - Crear categoria con nombre unico debe retornar la categoria guardada")
    void crear_conNombreNuevo_retornaCategoria() {
        // DADO: no existe ninguna categoría con ese nombre
        CategoriaRequestDTO request = new CategoriaRequestDTO("Seguridad", "Incidentes de seguridad informática");

        Categoria categoriaGuardada = new Categoria(
                1L, "Seguridad", "Incidentes de seguridad informática", true, LocalDateTime.now()
        );

        when(repository.existsByNombreIgnoreCase("Seguridad")).thenReturn(false);
        when(repository.save(any(Categoria.class))).thenReturn(categoriaGuardada);

        // CUANDO: se crea la categoría
        CategoriaResponseDTO resultado = categoriaService.crear(request);

        // ENTONCES: debe retornar la categoría activa con su ID
        assertNotNull(resultado);
        assertEquals("Seguridad", resultado.getNombre());
        assertTrue(resultado.getActivo());
        verify(repository, times(1)).save(any(Categoria.class));
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 13 — Crear categoría con nombre duplicado debe lanzar excepción
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P13 - Crear categoria con nombre duplicado debe lanzar ReglaNegocioException")
    void crear_conNombreDuplicado_lanzaExcepcion() {
        // DADO: ya existe una categoría llamada "Hardware"
        when(repository.existsByNombreIgnoreCase("Hardware")).thenReturn(true);

        CategoriaRequestDTO request = new CategoriaRequestDTO("Hardware", "Descripción");

        // CUANDO / ENTONCES: el sistema rechaza el duplicado
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> categoriaService.crear(request));

        assertTrue(ex.getMessage().contains("Ya existe una categoría con ese nombre"));
        // Confirmar que nunca se intentó guardar
        verify(repository, never()).save(any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 14 — Eliminar categoría aplica soft delete (activo = false)
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P14 - Eliminar categoria debe desactivarla no borrarla fisicamente")
    void eliminar_categoriaExistente_laDesactiva() {
        // DADO: existe la categoría con ID=1 y está activa
        Categoria categoria = new Categoria(1L, "Hardware", "desc", true, LocalDateTime.now());
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.save(any(Categoria.class))).thenReturn(categoria);

        // CUANDO: se elimina
        categoriaService.eliminar(1L);

        // ENTONCES: el campo activo debe ser false (soft delete)
        verify(repository, times(1)).save(argThat(c -> !c.getActivo()));
        // Y no debe llamarse deleteById nunca
        verify(repository, never()).deleteById(any());
    }
}
