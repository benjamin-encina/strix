package com.strix.mstickets.client;

import com.strix.mstickets.dto.PrioridadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PrioridadClient {

    @Qualifier("webClientPrioridades")
    private final WebClient webClientPrioridades;

    public PrioridadDTO obtenerPrioridad(Long id) {
        return webClientPrioridades.get()
                .uri("/api/prioridades/{id}", id)
                .retrieve()
                .bodyToMono(PrioridadDTO.class)
                .block();
    }
}