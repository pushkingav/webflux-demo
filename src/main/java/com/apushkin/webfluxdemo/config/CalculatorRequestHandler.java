package com.apushkin.webfluxdemo.config;

import com.apushkin.webfluxdemo.dto.Response;
import com.apushkin.webfluxdemo.exception.CalculatorOperationValidationException;
import com.apushkin.webfluxdemo.service.ReactiveCalculatorService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CalculatorRequestHandler {
    private final ReactiveCalculatorService calculatorService;

    public CalculatorRequestHandler(ReactiveCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public Mono<ServerResponse> calculateHandler(ServerRequest serverRequest) {
        int first = Integer.parseInt(serverRequest.pathVariable("first"));
        int second = Integer.parseInt(serverRequest.pathVariable("second"));
        String operation = serverRequest.headers().header("OP").get(0);
        Mono<Response> responseMono;
        switch (operation) {
            case "+" -> responseMono = calculatorService.add(first, second);
            case "-" -> responseMono = calculatorService.subtract(first, second);
            case "*" -> responseMono = calculatorService.multiply(first, second);
            case "/" -> responseMono = calculatorService.divide(first, second);
            default -> {
                return Mono.error(() -> new CalculatorOperationValidationException(operation));
            }
        }
        return ServerResponse.ok().body(responseMono, Response.class);
    }
}
