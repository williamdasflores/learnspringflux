package br.com.start.learnspringflux.controller.v1;

import br.com.start.learnspringflux.domain.Item;
import br.com.start.learnspringflux.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static br.com.start.learnspringflux.constants.ItemConstants.*;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository repository;

    List<Item> items = Arrays.asList(
            new Item("123", "LG TV", new BigDecimal("899.99")),
            new Item(null, "MackBook Air", new BigDecimal("7000.00")),
            new Item(null, "Microwave", new BigDecimal("159.90")));

    @Before
    public void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(repository::save)
                .blockLast();
    }

    @Test
    public void getAllItemsTest() {
        webTestClient.get().uri(ITEM_V1_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(3)
                .consumeWith( response -> {
                    List<Item> itemList = response.getResponseBody();
                    items.forEach( item -> {
                        assertTrue( (item.getId() != null && !"".equals(item.getId())) );
                    });
                });
    }

    @Test
    public void getAllItemsTest_1() {
        Flux<Item> itemsFlux = webTestClient.get().uri(ITEM_V1_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Item.class)
                .getResponseBody();
        StepVerifier.create(itemsFlux)
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getItemByIdTest() {
        webTestClient.get().uri(ITEM_V1_ENDPOINT+ "/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("123", "LG TV");
    }

    @Test
    public void saveItemTest() {
        Item item = new Item("123", "Item Test", new BigDecimal("99"));
        webTestClient.post().uri(ITEM_V1_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Item Test")
                .jsonPath("$.price").isEqualTo(new BigDecimal("99"));
    }

    @Test
    public void deleteItemTest() {
        webTestClient.delete().uri(ITEM_V1_ENDPOINT+ "/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItemTest() {
        BigDecimal price = new BigDecimal("10");
        Item item = new Item(null, "Testing Update", price);

        webTestClient.put().uri(ITEM_V1_ENDPOINT+ "/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", price);
    }

    @Test
    public void updateItemNotFoundTest() {
        BigDecimal price = new BigDecimal("10");
        Item item = new Item(null, "Testing Update", price);

        webTestClient.put().uri(ITEM_V1_ENDPOINT + "/{id}", "554654")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}
