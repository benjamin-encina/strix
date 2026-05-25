package com.strix.msadjuntos.service;

import com.strix.msadjuntos.dto.AdjuntoResponseDTO;
import com.strix.msadjuntos.exception.ReglaNegocioException;
import com.strix.msadjuntos.model.Adjunto;
import com.strix.msadjuntos.model.ComentarioRef;
import com.strix.msadjuntos.repository.AdjuntoRepository;
import com.strix.msadjuntos.repository.ComentarioRefRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdjuntoService {

    private static final Logger log = LoggerFactory.getLogger(AdjuntoService.class);

    private final AdjuntoRepository repository;
    private final ComentarioRefRepository comentarioRefRepository;

    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf", "text/plain"
    );
    private static final long MAX_TAMANO = 5 * 1024 * 1024L;
    private static final String STORAGE_DIR = "./uploads/strix/";

    private AdjuntoResponseDTO mapear(Adjunto a) {
        return new AdjuntoResponseDTO(
                a.getId(),
                a.getComentarioId(),
                a.getTicketId(),
                a.getNombreArchivo(),
                a.getTipoMime(),
                a.getTamanoBytes(),
                a.getRutaStorage(),
                a.getSubidoEn()
        );
    }

    /**
     * RF-18: Subir adjunto asociado a un comentario.
     * Se resuelve la entidad ComentarioRef (o se crea si no existe)
     * para mantener la integridad referencial a nivel ORM (@ManyToOne).
     */
    public AdjuntoResponseDTO subirAdjunto(Long comentarioId, Long ticketId,
                                           MultipartFile file) throws IOException {
        log.info("Subiendo adjunto para comentario {} (ticket {}): '{}', tipo '{}', {} bytes",
                comentarioId, ticketId, file.getOriginalFilename(), file.getContentType(), file.getSize());

        String mime = file.getContentType();
        if (!TIPOS_PERMITIDOS.contains(mime)) {
            log.warn("Tipo MIME no permitido: {}", mime);
            throw new ReglaNegocioException("Tipo de archivo no permitido: " + mime);
        }
        if (file.getSize() > MAX_TAMANO) {
            log.warn("Archivo demasiado grande: {} bytes (máximo {} bytes)", file.getSize(), MAX_TAMANO);
            throw new ReglaNegocioException("El archivo supera el límite de 5 MB");
        }

        // Resolver o registrar la referencia local al comentario (mantiene la relación @ManyToOne)
        ComentarioRef comentarioRef = comentarioRefRepository.findById(comentarioId)
                .orElseGet(() -> {
                    log.info("Registrando ComentarioRef para comentario {} (ticket {})", comentarioId, ticketId);
                    return comentarioRefRepository.save(new ComentarioRef(comentarioId, ticketId));
                });

        // Guardar archivo en disco local
        Path dir = Paths.get(STORAGE_DIR + ticketId);
        Files.createDirectories(dir);
        String nombreUnico = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path ruta = dir.resolve(nombreUnico);
        Files.copy(file.getInputStream(), ruta);

        Adjunto adjunto = new Adjunto();
        adjunto.setComentario(comentarioRef);   // ← relación @ManyToOne
        adjunto.setTicketId(ticketId);
        adjunto.setNombreArchivo(file.getOriginalFilename());
        adjunto.setTipoMime(mime);
        adjunto.setTamanoBytes(file.getSize());
        adjunto.setRutaStorage(ruta.toString());
        adjunto.setSubidoEn(LocalDateTime.now());

        Adjunto guardado = repository.save(adjunto);
        log.info("Adjunto {} guardado en {}", guardado.getId(), ruta);
        return mapear(guardado);
    }

    public List<AdjuntoResponseDTO> listarPorComentario(Long comentarioId) {
        log.info("Listando adjuntos del comentario {}", comentarioId);
        return repository.findByComentario_Id(comentarioId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }

    public List<AdjuntoResponseDTO> listarPorTicket(Long ticketId) {
        log.info("Listando adjuntos del ticket {}", ticketId);
        return repository.findByTicketId(ticketId)
                .stream().map(this::mapear).collect(Collectors.toList());
    }
}
