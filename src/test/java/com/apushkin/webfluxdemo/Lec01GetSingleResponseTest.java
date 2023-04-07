package com.apushkin.webfluxdemo;

import com.apushkin.webfluxdemo.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class Lec01GetSingleResponseTest extends BaseTest {
    private final WebClient webClient;

    @Autowired
    public Lec01GetSingleResponseTest(WebClient webClient) {
        this.webClient = webClient;
    }

    @Test
    public void blockTest() {
        Response response = webClient
                .get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .block();
        log.info("Response: {}", response);
    }

    @Test
    public void stepVerifierTest() {
        Mono<Response> responseMono = webClient
                .get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getOutput() == 25)
                .verifyComplete();
    }
}
