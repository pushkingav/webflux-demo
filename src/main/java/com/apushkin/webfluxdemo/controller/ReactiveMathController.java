package com.apushkin.webfluxdemo.controller;

import com.apushkin.webfluxdemo.dto.MultiplyRequestDto;
import com.apushkin.webfluxdemo.dto.Response;
import com.apushkin.webfluxdemo.service.ReactiveMathService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathController {

    private final ReactiveMathService mathService;

    public ReactiveMathController(ReactiveMathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("square/{input}")
    public Mono<Response> findSquare(@PathVariable int input) {
        return mathService.findSquare(input)
                .defaultIfEmpty(new Response(-1));
    }

    @GetMapping(value = "table/{input}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Response> multiplicationTable(@PathVariable int input) {
        return mathService.multiplicationTable(input);
    }

    @GetMapping(value = "table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> multiplicationTableStream(@PathVariable int input) {
        return mathService.multiplicationTable(input);
    }

    @PostMapping("multiply")
    public Mono<Response> multiply(@RequestBody Mono<MultiplyRequestDto> requestDtoMono,
                                   @RequestHeader Map<String, String> headers) {
        System.out.println(headers);
        return mathService.multiply(requestDtoMono);
    }
}
