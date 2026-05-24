package com.strix.msusuarios.service;

import com.strix.msusuarios.dto.LoginRequestDTO;
import com.strix.msusuarios.dto.LoginResponseDTO;
import com.strix.msusuarios.exception.ReglaNegocioException;
import com.strix.msusuarios.model.JwtBlacklist;
import com.strix.msusuarios.model.Usuario;
import com.strix.msusuarios.repository.JwtBlacklistRepository;
import com.strix.msusuarios.repository.UsuarioRepository;
import com.strix.msusuarios.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final JwtBlacklistRepository blacklistRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        log.info("Intento de login para email: {}", dto.getEmail());
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login fallido: email no encontrado {}", dto.getEmail());
                    return new ReglaNegocioException("Credenciales inválidas.");
                });

        if (!usuario.getActivo()) {
            log.warn("Login fallido: usuario {} inactivo", dto.getEmail());
            throw new ReglaNegocioException("Credenciales inválidas.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            log.warn("Login fallido: contraseña incorrecta para {}", dto.getEmail());
            throw new ReglaNegocioException("Credenciales inválidas.");
        }

        String token = jwtUtil.generarToken(usuario.getId(), usuario.getEmail(), usuario.getRol().name());
        log.info("Login exitoso para usuario {} con rol {}", usuario.getId(), usuario.getRol());

        return new LoginResponseDTO(
                token, "Bearer",
                usuario.getId(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }

    public void logout(String token) {
        log.info("Procesando logout");
        if (!jwtUtil.esValido(token)) {
            log.warn("Logout: token ya expirado.");
            return;
        }
        LocalDateTime expira = jwtUtil.obtenerExpiracion(token)
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        JwtBlacklist entrada = new JwtBlacklist(null, token.hashCode() + "_" + System.currentTimeMillis(),
                expira, LocalDateTime.now());
        blacklistRepository.save(entrada);
        log.info("Token añadido a lista negra, expira en {}", expira);
    }

    public boolean estaEnBlacklist(String token) {
        return blacklistRepository.existsByTokenHash(token.hashCode() + "_" + System.currentTimeMillis());
    }
}
