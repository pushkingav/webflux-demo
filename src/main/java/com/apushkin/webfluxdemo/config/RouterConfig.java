package com.apushkin.webfluxdemo.config;

import com.apushkin.webfluxdemo.dto.InputFailedValidationResponse;
import com.apushkin.webfluxdemo.exception.CalculatorOperationValidationException;
import com.apushkin.webfluxdemo.exception.InputValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Predicate;

@Configuration
public class RouterConfig {
    private final RequestHandler requestHandler;
    private final CalculatorRequestHandler calculatorRequestHandler;

    public RouterConfig(RequestHandler requestHandler, CalculatorRequestHandler calculatorRequestHandler) {
        this.requestHandler = requestHandler;
        this.calculatorRequestHandler = calculatorRequestHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> highLevelRouter() {
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .path("calculator", this::calculatorRouterFunction)
                .build();
    }

    //@Bean
    private RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("square/{input}", RequestPredicates.path("*/1?")
                        .or(RequestPredicates.path("*/20")), requestHandler::squareHandler)
                .GET("square/{input}", request -> ServerResponse.badRequest().bodyValue("only 10-20 allowed"))
                .GET("table/{input}", requestHandler::multiplicationTableHandler)
                .GET("table/{input}/stream", requestHandler::streamingMultiplicationTableHandler)
                .POST("multiply", requestHandler::multiplyHandler)
                .GET("square/{input}/validation", requestHandler::squareHandleWithValidation)
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }

    private RouterFunction<ServerResponse> calculatorRouterFunction() {
        Predicate<ServerRequest.Headers> hasOperation = headers -> !headers.header("OP").isEmpty();
        return RouterFunctions.route()
                .GET("{first}/{second}", RequestPredicates.headers(hasOperation),
                        calculatorRequestHandler::calculateHandler)
                .GET("{first}/{second}", request ->
                        ServerResponse.badRequest().bodyValue("HMM... No operation found in request"))
                .onError(CalculatorOperationValidationException.class, calculatorExceptionHandler())
                .build();
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (throwable, serverRequest) -> {
            InputValidationException exception = (InputValidationException) throwable;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setInput(exception.getInput());
            response.setErrorCode(exception.getErrorCode());
            response.setMessage(exception.getMessage());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }

    record CalculatorValidationFailedResponse(String operation, int errorCode, String message) {
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> calculatorExceptionHandler() {
        return (throwable, serverRequest) -> {
            CalculatorOperationValidationException ex = (CalculatorOperationValidationException) throwable;
            CalculatorValidationFailedResponse response =
                    new CalculatorValidationFailedResponse(ex.getOperation(), ex.getErrorCode(), ex.getMessage());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }
}
