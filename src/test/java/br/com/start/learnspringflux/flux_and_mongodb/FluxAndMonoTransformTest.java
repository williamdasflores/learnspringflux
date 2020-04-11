package br.com.start.learnspringflux.flux_and_mongodb;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {

    List<String> listNames = Arrays.asList("William", "Beatriz", "Jessica");

    @Test
    public void transformMap() {
        Flux<String> namesFlux = Flux.fromIterable(listNames)
                .map(name -> name.toUpperCase()).log();
        StepVerifier.create(namesFlux)
                .expectNext("WILLIAM", "BEATRIZ", "JESSICA")
                .verifyComplete();
    }

    @Test
    public void transformMapLength() {
        Flux<Integer> lengthNamesFlux = Flux.fromIterable(listNames)
                .map(name -> name.length())
                .log();
        StepVerifier.create(lengthNamesFlux)
                .expectNext(7, 7, 7)
                .verifyComplete();
    }

    @Test
    public void transformFlatMap() {
        Flux<String> names = Flux.fromIterable(Arrays.asList("A", "B", "C", "E"))
                .flatMap(str -> {

                    return Flux.fromIterable(convertToList(str));
                })
                .log();
        StepVerifier.create(names)
                .expectNextCount(8)
                .verifyComplete();

    }

    private List<String> convertToList(String str) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(str, "New Value");
    }
}
