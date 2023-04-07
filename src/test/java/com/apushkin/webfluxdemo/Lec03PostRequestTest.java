package com.apushkin.webfluxdemo;

import com.apushkin.webfluxdemo.dto.MultiplyRequestDto;
import com.apushkin.webfluxdemo.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class Lec03PostRequestTest extends BaseTest {
    @Autowired
    WebClient webClient;

    @Test
    public void postTest() {
        Mono<Response> responseMono = webClient
                .post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(next -> log.info(next.toString()));

        StepVerifier
                .create(responseMono)
                .expectNextMatches(response -> response.getOutput() == 10)
                .verifyComplete();
    }

    private MultiplyRequestDto buildRequestDto(int a, int b) {
        MultiplyRequestDto dto = new MultiplyRequestDto();
        dto.setFirst(a);
        dto.setSecond(b);
        return dto;
    }
}
