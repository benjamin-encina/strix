package com.strix.msreportes.client;

import com.strix.msreportes.dto.TicketResumenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketClient {

    @Qualifier("ticketsClient")
    private final WebClient ticketsClient;

    public List<TicketResumenDTO> obtenerTodos(String estado, Long categoriaId,
                                               Long prioridadId, Long tecnicoId) {
        return ticketsClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/tickets")
                        .queryParamIfPresent("estado",
                                java.util.Optional.ofNullable(estado))
                        .queryParamIfPresent("categoriaId",
                                java.util.Optional.ofNullable(categoriaId))
                        .queryParamIfPresent("prioridadId",
                                java.util.Optional.ofNullable(prioridadId))
                        .queryParamIfPresent("tecnicoId",
                                java.util.Optional.ofNullable(tecnicoId))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TicketResumenDTO>>() {})
                .block();
    }
}
