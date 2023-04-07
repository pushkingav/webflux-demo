package com.apushkin.webfluxdemo.service;

import com.apushkin.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactiveCalculatorService {
    public Mono<Response> multiply(int first, int second) {
        return Mono.fromSupplier(() -> first * second).map(Response::new);
    }

    public Mono<Response> add(int first, int second) {
        return Mono.fromSupplier(() -> first + second).map(Response::new);
    }

    public Mono<Response> subtract(int first, int second) {
        return Mono.fromSupplier(() -> first - second).map(Response::new);
    }

    public Mono<Response> divide(int first, int second) {
        if (second == 0) {
            return Mono.error(new IllegalArgumentException("Division by zero is not allowed"));
        }
        return Mono.fromSupplier(() -> first / second).map(Response::new);
    }

}
