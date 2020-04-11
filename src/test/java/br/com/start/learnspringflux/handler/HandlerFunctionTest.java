package br.com.start.learnspringflux.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
public class HandlerFunctionTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxTest() {
        Flux<String> stringFluxTest = webTestClient.get().uri("/flux/functional")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(stringFluxTest)
                .expectSubscription()
                .expectNext("Los Angeles São Paulo Fortaleza")
                .verifyComplete();
    }

    @Test
    public void monoTest() {
        String expected = "Rio de Janeiro";

        webTestClient.get().uri("/mono/functional")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(String.class)
               .consumeWith( response -> {
                   assertEquals(expected, response.getResponseBody());
               });


    }
}
