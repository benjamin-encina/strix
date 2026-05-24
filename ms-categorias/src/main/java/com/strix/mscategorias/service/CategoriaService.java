package com.strix.mscategorias.service;

import com.strix.mscategorias.dto.CategoriaRequestDTO;
import com.strix.mscategorias.dto.CategoriaResponseDTO;
import com.strix.mscategorias.exception.RecursoNoEncontradoException;
import com.strix.mscategorias.exception.ReglaNegocioException;
import com.strix.mscategorias.model.Categoria;
import com.strix.mscategorias.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaService.class);

    private final CategoriaRepository repository;

    private CategoriaResponseDTO mapear(Categoria c) {
        return new CategoriaResponseDTO(c.getId(), c.getNombre(), c.getDescripcion(), c.getActivo());
    }

    public List<CategoriaResponseDTO> listarTodas() {
        log.info("Listando todas las categorías");
        return repository.findAll().stream().map(this::mapear).collect(Collectors.toList());
    }

    public Optional<CategoriaResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando categoría con ID {}", id);
        return repository.findById(id).map(this::mapear);
    }

    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {
        log.info("Creando categoría: {}", dto.getNombre());
        if (repository.existsByNombreIgnoreCase(dto.getNombre())) {
            log.warn("Categoría duplicada: {}", dto.getNombre());
            throw new ReglaNegocioException("Ya existe una categoría con ese nombre: " + dto.getNombre());
        }
        Categoria c = new Categoria(null, dto.getNombre(), dto.getDescripcion(), true, null);
        c.setCreadoEn(LocalDateTime.now());
        Categoria guardada = repository.save(c);
        log.info("Categoría creada con ID {}", guardada.getId());
        return mapear(guardada);
    }

    public CategoriaResponseDTO editar(Long id, CategoriaRequestDTO dto) {
        log.info("Editando categoría con ID {}", id);
        Categoria c = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + id));
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
        log.info("Categoría {} actualizada", id);
        return mapear(repository.save(c));
    }

    public void eliminar(Long id) {
        log.info("Desactivando categoría con ID {}", id);
        Categoria c = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + id));
        c.setActivo(false);
        repository.save(c);
        log.info("Categoría {} desactivada", id);
    }
}
