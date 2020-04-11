package br.com.start.learnspringflux.flux_and_mongodb;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    @Test
    public void filterTest() {
        Flux<String> fluxNames = Flux.fromIterable(listNames)
                .filter(str -> str.startsWith("J"))
                .log();
        StepVerifier.create(fluxNames)
                .expectNext("Jessica", "Jenny")
                .verifyComplete();
    }


    List<String> listNames = Arrays.asList("William", "Beatriz", "Jessica", "Jenny");

    @Test
    public void filterLengthTest() {
        Flux<String> fluxNames = Flux.fromIterable(listNames)
                .filter(str -> str.length() <= 5)
                .log();
        StepVerifier.create(fluxNames)
                .expectNext("Jenny")
                .verifyComplete();
    }
}
