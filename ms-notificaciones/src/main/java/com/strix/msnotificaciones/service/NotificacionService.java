package com.strix.msnotificaciones.service;

import com.strix.msnotificaciones.dto.NotificacionRequestDTO;
import com.strix.msnotificaciones.dto.NotificacionResponseDTO;
import com.strix.msnotificaciones.exception.RecursoNoEncontradoException;
import com.strix.msnotificaciones.model.Notificacion;
import com.strix.msnotificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository repository;

    private NotificacionResponseDTO mapear(Notificacion n) {
        return new NotificacionResponseDTO(
                n.getId(), n.getDestinatarioId(), n.getTipo(),
                n.getMensaje(), n.getLeida(), n.getCreadaEn()
        );
    }

    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {
        log.info("Creando notificación tipo '{}' para usuario {}", dto.getTipo(), dto.getDestinatarioId());
        Notificacion n = new Notificacion(
                null, dto.getDestinatarioId(), dto.getTipo(),
                dto.getMensaje(), false, LocalDateTime.now()
        );
        Notificacion guardada = repository.save(n);
        log.info("Notificación {} enviada a usuario {}", guardada.getId(), dto.getDestinatarioId());
        return mapear(guardada);
    }

    public List<NotificacionResponseDTO> listarPorUsuario(Long usuarioId) {
        log.info("Listando notificaciones del usuario {}", usuarioId);
        return repository.findByDestinatarioIdOrderByCreadaEnDesc(usuarioId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }

    public List<NotificacionResponseDTO> listarNoLeidas(Long usuarioId) {
        log.info("Listando notificaciones no leídas del usuario {}", usuarioId);
        return repository.findByDestinatarioIdAndLeida(usuarioId, false)
                .stream().map(this::mapear).collect(Collectors.toList());
    }

    public NotificacionResponseDTO marcarLeida(Long id) {
        log.info("Marcando notificación {} como leída", id);
        Notificacion n = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificación no encontrada: " + id));
        n.setLeida(true);
        log.info("Notificación {} marcada como leída", id);
        return mapear(repository.save(n));
    }
}
