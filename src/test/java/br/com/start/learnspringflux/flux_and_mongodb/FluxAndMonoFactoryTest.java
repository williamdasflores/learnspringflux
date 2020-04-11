package br.com.start.learnspringflux.flux_and_mongodb;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> listNames = Arrays.asList("William", "Beatriz", "Jessica");

    @Test
    public void fluxUsingIterable() {
        Flux<String> namesFlux = Flux.fromIterable(listNames);
        StepVerifier.create(namesFlux)
                .expectNext("William", "Beatriz", "Jessica")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] names = new String[] {"William", "Beatriz", "Jessica"};
        Flux<String> fluxNames = Flux.fromArray(names);

        StepVerifier.create(fluxNames)
                .expectNext("William", "Beatriz", "Jessica")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(listNames.stream());
        StepVerifier.create(namesFlux)
                .expectNext("William", "Beatriz", "Jessica")
                .verifyComplete();
    }

    @Test
    public void monoEmptyTest() {
        Mono<String> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoSupplier() {
        Supplier<String> strSupplier = () -> "William";
        Mono<String> strMono = Mono.fromSupplier(strSupplier);
        StepVerifier.create(strMono)
                .expectNext("William")
                .verifyComplete();
    }

    @Test
    public void fluxRange() {
        Flux<Integer> fluxInt = Flux.range(1, 5);

        StepVerifier.create(fluxInt)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }
}
