package br.com.start.learnspringflux.flux_and_mongodb;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws Exception {
        Flux<String> strFlux = Flux.just("A", "B", "C", "D")
                .delayElements(Duration.ofSeconds(1));
        strFlux.subscribe( str -> System.out.println("Subscriber 1 " + str));
        Thread.sleep(2000);
        strFlux.subscribe( str -> System.out.println("Subscriber 2 " + str));
        Thread.sleep(4000);
    }

    @Test
    public void hotPublisherTest() throws Exception {
        Flux<String> strFlux = Flux.just("A", "B", "C", "D")
                .delayElements(Duration.ofSeconds(1));
        ConnectableFlux<String> connectableFlux = strFlux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe( str -> System.out.println("Subscriber 1 " + str));
        Thread.sleep(2000);
        connectableFlux.subscribe( str -> System.out.println("Subscriber 2 " + str));
        Thread.sleep(4000);

    }
}
