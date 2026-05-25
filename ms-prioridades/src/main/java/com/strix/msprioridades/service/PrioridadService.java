package com.strix.msprioridades.service;

import com.strix.msprioridades.dto.PrioridadRequestDTO;
import com.strix.msprioridades.dto.PrioridadResponseDTO;
import com.strix.msprioridades.exception.RecursoNoEncontradoException;
import com.strix.msprioridades.model.Prioridad;
import com.strix.msprioridades.repository.PrioridadRepository;
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
public class PrioridadService {

    private static final Logger log = LoggerFactory.getLogger(PrioridadService.class);

    private final PrioridadRepository repository;

    private PrioridadResponseDTO mapear(Prioridad p) {
        return new PrioridadResponseDTO(p.getId(), p.getNombre(), p.getNivel(), p.getSlaHoras(), p.getActivo());
    }

    public List<PrioridadResponseDTO> listarTodas() {
        log.info("Listando todas las prioridades");
        return repository.findAll().stream().map(this::mapear).collect(Collectors.toList());
    }

    public Optional<PrioridadResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando prioridad con ID {}", id);
        return repository.findById(id).map(this::mapear);
    }

    public PrioridadResponseDTO crear(PrioridadRequestDTO dto) {
        log.info("Creando prioridad: {} (nivel {}, SLA {} h)", dto.getNombre(), dto.getNivel(), dto.getSlaHoras());
        Prioridad p = new Prioridad(null, dto.getNombre(), dto.getNivel(), dto.getSlaHoras(), true, LocalDateTime.now());
        Prioridad guardada = repository.save(p);
        log.info("Prioridad creada con ID {}", guardada.getId());
        return mapear(guardada);
    }

    public PrioridadResponseDTO editar(Long id, PrioridadRequestDTO dto) {
        log.info("Editando prioridad con ID {}", id);
        Prioridad p = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prioridad no encontrada: " + id));
        p.setNombre(dto.getNombre());
        p.setNivel(dto.getNivel());
        p.setSlaHoras(dto.getSlaHoras());
        log.info("Prioridad {} actualizada", id);
        return mapear(repository.save(p));
    }

    public void eliminar(Long id) {
        log.info("Desactivando prioridad con ID {}", id);
        Prioridad p = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prioridad no encontrada: " + id));
        p.setActivo(false);
        repository.save(p);
        log.info("Prioridad {} desactivada", id);
    }
}
