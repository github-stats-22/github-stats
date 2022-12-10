package io.pakland.mdas.githubstats.infrastructure.rest.repository;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@NoArgsConstructor
public class WebClientConfiguration {

    private WebClient webClient;

    public WebClientConfiguration(String baseUrl, String apiKey) {
        this.webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
                })
                .build();
    }

    public WebClient getWebClient() {
        return this.webClient;
    }
}
