package com.strix.msusuarios.service;

import com.strix.msusuarios.dto.LoginRequestDTO;
import com.strix.msusuarios.dto.LoginResponseDTO;
import com.strix.msusuarios.exception.ReglaNegocioException;
import com.strix.msusuarios.model.Usuario;
import com.strix.msusuarios.repository.JwtBlacklistRepository;
import com.strix.msusuarios.repository.UsuarioRepository;
import com.strix.msusuarios.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de autenticación.
 * Verifica el flujo de login con las tres variantes posibles:
 * usuario válido, usuario inactivo y contraseña incorrecta.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UsuarioRepository      usuarioRepository;
    @Mock private JwtBlacklistRepository blacklistRepository;
    @Mock private JwtUtil                jwtUtil;
    @Mock private BCryptPasswordEncoder  passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Usuario usuarioActivo;

    @BeforeEach
    void setUp() {
        // Usuario base reutilizado en las pruebas
        usuarioActivo = new Usuario(
                1L, "Admin", "Strix",
                "admin@strix.com",
                "$2a$10$hash_simulado",
                Usuario.Rol.ADMIN,
                true, null
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 9 — Login exitoso con credenciales correctas
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P9 - Login con email y password correctos debe retornar token JWT")
    void login_conCredencialesValidas_retornaToken() {
        // DADO: el usuario existe, está activo y la contraseña coincide
        when(usuarioRepository.findByEmail("admin@strix.com"))
                .thenReturn(Optional.of(usuarioActivo));
        when(passwordEncoder.matches("Admin123!", usuarioActivo.getPassword()))
                .thenReturn(true);
        when(jwtUtil.generarToken(1L, "admin@strix.com", "ADMIN"))
                .thenReturn("token.jwt.simulado");

        LoginRequestDTO request = new LoginRequestDTO("admin@strix.com", "Admin123!");

        // CUANDO: se hace login
        LoginResponseDTO resultado = authService.login(request);

        // ENTONCES: debe retornar el token y los datos del usuario
        assertNotNull(resultado);
        assertEquals("token.jwt.simulado", resultado.getToken());
        assertEquals("Bearer", resultado.getTipo());
        assertEquals("ADMIN", resultado.getRol());
        verify(jwtUtil, times(1)).generarToken(anyLong(), anyString(), anyString());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 10 — Login con usuario inactivo debe lanzar excepción
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P10 - Login con usuario inactivo debe lanzar ReglaNegocioException")
    void login_conUsuarioInactivo_lanzaExcepcion() {
        // DADO: el usuario existe pero está inactivo (soft delete)
        usuarioActivo.setActivo(false);
        when(usuarioRepository.findByEmail("admin@strix.com"))
                .thenReturn(Optional.of(usuarioActivo));

        LoginRequestDTO request = new LoginRequestDTO("admin@strix.com", "Admin123!");

        // CUANDO / ENTONCES: el sistema rechaza con "Credenciales inválidas"
        // (mismo mensaje que email no encontrado, por seguridad)
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> authService.login(request));

        assertEquals("Credenciales inválidas.", ex.getMessage());
        // El token nunca debe generarse
        verify(jwtUtil, never()).generarToken(any(), any(), any());
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRUEBA 11 — Login con contraseña incorrecta debe lanzar excepción
    // ════════════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("P11 - Login con password incorrecta debe lanzar ReglaNegocioException")
    void login_conPasswordIncorrecta_lanzaExcepcion() {
        // DADO: el usuario existe y está activo, pero la contraseña no coincide
        when(usuarioRepository.findByEmail("admin@strix.com"))
                .thenReturn(Optional.of(usuarioActivo));
        when(passwordEncoder.matches("WrongPass!", usuarioActivo.getPassword()))
                .thenReturn(false);

        LoginRequestDTO request = new LoginRequestDTO("admin@strix.com", "WrongPass!");

        // CUANDO / ENTONCES: el mensaje es igual al de email no encontrado
        // (no se revela cuál de los dos campos es incorrecto)
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> authService.login(request));

        assertEquals("Credenciales inválidas.", ex.getMessage());
        verify(jwtUtil, never()).generarToken(any(), any(), any());
    }
}
