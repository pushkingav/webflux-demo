package com.apushkin.webfluxdemo.config;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class CalculatorHandler {
    //The following to methods MUST be refactored in order to DRY, right?
    public Mono<ServerResponse> additionalHandler(ServerRequest request) {
        int a = getValue(request, "first");
        int b = getValue(request, "second");
        return ServerResponse.ok().bodyValue(a + b);
    }

    public Mono<ServerResponse> subtractHandler(ServerRequest request) {
        int a = getValue(request, "first");
        int b = getValue(request, "second");
        return ServerResponse.ok().bodyValue(a - b);
    }

    public Mono<ServerResponse> addingHandler(ServerRequest request) {
        return process(request, Integer::sum);
    }

    public Mono<ServerResponse> subtractingHandler(ServerRequest request) {
        return process(request, (a, b) -> a - b);
    }

    public Mono<ServerResponse> process(ServerRequest request,
                                        BiFunction<Integer, Integer, Integer> transform) {
        int a = getValue(request, "first");
        int b = getValue(request, "second");
        return ServerResponse.ok().bodyValue(transform.apply(a, b));
    }


    private int getValue(ServerRequest request, String key) {
        return Integer.parseInt(request.pathVariable(key));
    }
}
