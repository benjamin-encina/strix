package com.strix.msusuarios.service;

import com.strix.msusuarios.dto.UsuarioRequestDTO;
import com.strix.msusuarios.dto.UsuarioResponseDTO;
import com.strix.msusuarios.exception.RecursoNoEncontradoException;
import com.strix.msusuarios.exception.ReglaNegocioException;
import com.strix.msusuarios.model.Usuario;
import com.strix.msusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private UsuarioResponseDTO mapear(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(), u.getNombre(), u.getApellido(),
                u.getEmail(), u.getRol().name(), u.getActivo(), u.getCreadoEn()
        );
    }

    public List<UsuarioResponseDTO> listarTodos() {
        log.info("Listando todos los usuarios");
        return usuarioRepository.findAll().stream().map(this::mapear).collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando usuario con ID {}", id);
        return usuarioRepository.findById(id).map(this::mapear);
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        log.info("Creando usuario con email {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", dto.getEmail());
            throw new ReglaNegocioException("El email ya está registrado: " + dto.getEmail());
        }
        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setRol(Usuario.Rol.valueOf(dto.getRol().toUpperCase()));
        u.setActivo(true);
        Usuario guardado = usuarioRepository.save(u);
        log.info("Usuario creado con ID {}", guardado.getId());
        return mapear(guardado);
    }

    public UsuarioResponseDTO editar(Long id, UsuarioRequestDTO dto) {
        log.info("Editando usuario con ID {}", id);
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setRol(Usuario.Rol.valueOf(dto.getRol().toUpperCase()));
        log.info("Usuario {} actualizado", id);
        return mapear(usuarioRepository.save(u));
    }

    public void eliminar(Long id) {
        log.info("Desactivando usuario con ID {}", id);
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
        u.setActivo(false);
        usuarioRepository.save(u);
        log.info("Usuario {} desactivado (soft-delete)", id);
    }
}
