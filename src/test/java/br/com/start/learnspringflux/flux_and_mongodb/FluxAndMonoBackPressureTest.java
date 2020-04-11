package br.com.start.learnspringflux.flux_and_mongodb;

import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> finiteFlux = Flux.range(1, 5)
                .log();
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> finiteFlux = Flux.range(1,5);

        finiteFlux.subscribe( (element) -> System.out.println("Element is " + element),
                (e) -> System.err.println("Error " +e ),
                () -> System.out.println("Done"),
                (subscription -> subscription.request(3)));

    }

    @Test
    public void backPressureCancel() {
        Flux<Integer> finiteFlux = Flux.range(1,5);

        finiteFlux.subscribe( (element) -> System.out.println("Element is " + element),
                (e) -> System.err.println("Error " +e ),
                () -> System.out.println("Done"),
                (subscription -> subscription.cancel()));
    }

    @Test
    public void backPressureCustomized() {
        Flux<Integer> finiteFlux = Flux.range(1,5).log();

        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Value is " + value);
                if (value == 4)
                    cancel();
            }
        });
    }
}
