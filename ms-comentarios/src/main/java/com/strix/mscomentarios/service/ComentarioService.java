package com.strix.mscomentarios.service;

import com.strix.mscomentarios.dto.ComentarioRequestDTO;
import com.strix.mscomentarios.dto.ComentarioResponseDTO;
import com.strix.mscomentarios.model.Comentario;
import com.strix.mscomentarios.repository.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private static final Logger log = LoggerFactory.getLogger(ComentarioService.class);

    private final ComentarioRepository repository;

    private ComentarioResponseDTO mapear(Comentario c) {
        return new ComentarioResponseDTO(
                c.getId(), c.getTicketId(), c.getUsuarioId(),
                c.getContenido(), c.getTipo().name(), c.getCreadoEn()
        );
    }

    // Agregar comentario a un Ticket
    public ComentarioResponseDTO agregar(ComentarioRequestDTO dto, Long usuarioId) {
        log.info("Usuario {} agregando comentario al ticket {}", usuarioId, dto.getTicketId());
        Comentario c = new Comentario();
        c.setTicketId(dto.getTicketId());
        c.setUsuarioId(usuarioId);
        c.setContenido(dto.getContenido());
        c.setTipo(dto.getTipo() != null && dto.getTipo().equalsIgnoreCase("SISTEMA")
                ? Comentario.Tipo.SISTEMA : Comentario.Tipo.USUARIO);
        c.setCreadoEn(LocalDateTime.now());
        Comentario guardado = repository.save(c);
        log.info("Comentario {} creado en ticket {}", guardado.getId(), dto.getTicketId());
        return mapear(guardado);
    }

    // Ver hilo completo de un Ticket
    public List<ComentarioResponseDTO> listarPorTicket(Long ticketId) {
        log.info("Listando comentarios del ticket {}", ticketId);
        return repository.findByTicketIdOrderByCreadoEnAsc(ticketId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }
}
