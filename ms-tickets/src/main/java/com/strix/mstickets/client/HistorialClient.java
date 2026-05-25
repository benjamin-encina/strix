package com.strix.mstickets.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistorialClient {

    @Qualifier("webClientHistorial")
    private final WebClient webClientHistorial;

    public void registrarCambio(Long ticketId, String estadoAnterior, String estadoNuevo,
                                Long usuarioId, String justificacion) {
        Map<String, Object> body = Map.of(
                "ticketId",       ticketId,
                "estadoAnterior", estadoAnterior != null ? estadoAnterior : "",
                "estadoNuevo",    estadoNuevo,
                "usuarioId",      usuarioId,
                "justificacion",  justificacion != null ? justificacion : ""
        );

        webClientHistorial.post()
                .uri("/api/historial")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}