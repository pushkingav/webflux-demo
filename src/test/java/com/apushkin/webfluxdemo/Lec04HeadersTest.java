package com.apushkin.webfluxdemo;

import com.apushkin.webfluxdemo.dto.MultiplyRequestDto;
import com.apushkin.webfluxdemo.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

@Slf4j
public class Lec04HeadersTest extends BaseTest {
    @Autowired
    WebClient webClient;

    @Test
    public void headersTest() {
        Mono<Response> responseMono = webClient
                .get()
                .uri("calculator/{first}/{second}", 5, 2)
                .headers(httpHeaders -> httpHeaders.put("OP", Collections.singletonList("*")))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(next -> log.info(next.toString()));

        StepVerifier
                .create(responseMono)
                .expectNextMatches(response -> response.getOutput() == 15)
                .verifyComplete();
    }

    private MultiplyRequestDto buildRequestDto(int a, int b) {
        MultiplyRequestDto dto = new MultiplyRequestDto();
        dto.setFirst(a);
        dto.setSecond(b);
        return dto;
    }
}
