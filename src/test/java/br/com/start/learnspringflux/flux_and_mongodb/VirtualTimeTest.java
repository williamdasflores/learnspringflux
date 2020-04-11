package br.com.start.learnspringflux.flux_and_mongodb;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void withoutVirtualTimeTest() {
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3).log();
        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0l, 01l, 02l)
                .verifyComplete();
    }

    @Test
    public void virtualTimeTest() {
        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3);
        StepVerifier.withVirtualTime( () -> longFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(5))
                .expectNext(0l,1l,2l)
                .verifyComplete();
    }



}
