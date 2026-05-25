package com.strix.msreportes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${strix.ms-tickets.url}")
    private String ticketsUrl;

    @Bean("ticketsClient")
    public WebClient ticketsClient() {
        return WebClient.builder().baseUrl(ticketsUrl).build();
    }
}
