package br.com.start.learnspringflux.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HandlerFunction {

    public Mono<ServerResponse> flux(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body( Flux.just("Los Angeles ", "São Paulo ", "Fortaleza").log(), String.class );
    }


    public Mono<ServerResponse> mono(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body( Mono.just("Rio de Janeiro").log(), String.class );
    }
}
