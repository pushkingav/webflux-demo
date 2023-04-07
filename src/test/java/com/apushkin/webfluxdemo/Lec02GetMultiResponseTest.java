package com.apushkin.webfluxdemo;

import com.apushkin.webfluxdemo.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class Lec02GetMultiResponseTest extends BaseTest {
    @Autowired
    WebClient webClient;

    @Test
    public void fluxTest() {
        Flux<Response> responseFlux = webClient
                .get()
                .uri("reactive-math/table/{input}", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(next -> log.info(next.toString()));

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void fluxStreamTest() {
        Flux<Response> responseFlux = webClient
                .get()
                .uri("reactive-math/table/{input}/stream", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(next -> log.info(next.toString()));

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }
}
