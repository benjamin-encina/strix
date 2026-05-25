package com.strix.mstickets.client;

import com.strix.mstickets.dto.CategoriaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CategoriaClient {

    @Qualifier("webClientCategorias")
    private final WebClient webClientCategorias;

    public CategoriaDTO obtenerCategoria(Long id) {
        return webClientCategorias.get()
                .uri("/api/categorias/{id}", id)
                .retrieve()
                .bodyToMono(CategoriaDTO.class)
                .block();
    }
}