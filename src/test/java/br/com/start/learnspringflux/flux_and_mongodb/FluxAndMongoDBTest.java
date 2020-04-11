package br.com.start.learnspringflux.flux_and_mongodb;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMongoDBTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //.concatWith(Flux.error(new RuntimeException("Error Provoked!")))
                .concatWith(Flux.just("After Error"))
                .log();
        stringFlux.subscribe(System.out::println,
                (e) -> System.err.println(e));
    }

    @Test
    public void fluxTestElements() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                    .verifyComplete();
    }

    @Test
    public void fluxTestElementsError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectError(RuntimeException.class);
    }

    @Test
    public void monoTest() {
        Mono<String> strMono =  Mono.just("Spring").log();
        StepVerifier.create(strMono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTestError() {
        Mono<String> strMono =  Mono.just("Spring").log();
        StepVerifier.create(Mono.error(new RuntimeException("Error")))
                .expectNext("Spring")
                .expectError(RuntimeException.class);
    }
}
