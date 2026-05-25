package com.strix.mstickets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${strix.ms-categorias.url}")
    private String categoriasUrl;

    @Value("${strix.ms-prioridades.url}")
    private String prioridadesUrl;

    @Value("${strix.ms-historial.url}")
    private String historialUrl;

    @Value("${strix.ms-notificaciones.url}")
    private String notificacionesUrl;

    @Bean("webClientCategorias")
    public WebClient webClientCategorias() {
        return WebClient.builder().baseUrl(categoriasUrl).build();
    }

    @Bean("webClientPrioridades")
    public WebClient webClientPrioridades() {
        return WebClient.builder().baseUrl(prioridadesUrl).build();
    }

    @Bean("webClientHistorial")
    public WebClient webClientHistorial() {
        return WebClient.builder().baseUrl(historialUrl).build();
    }

    @Bean("webClientNotificaciones")
    public WebClient webClientNotificaciones() {
        return WebClient.builder().baseUrl(notificacionesUrl).build();
    }
}