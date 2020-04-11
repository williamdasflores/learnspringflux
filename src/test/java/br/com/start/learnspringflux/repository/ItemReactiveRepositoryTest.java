package br.com.start.learnspringflux.repository;

import br.com.start.learnspringflux.domain.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemRepository;

    List<Item> itemList = Arrays.asList(
            new Item(null, "Item 1 Test", new BigDecimal("10")),
            new Item(null, "Item 2 Test", new BigDecimal("4.5")),
            new Item("123", "Item 3 Test", new BigDecimal("1")));

    @Before
    public void setUp() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemRepository::save)
                .doOnNext( item -> {
                    System.out.println(item);
                })
                .blockLast();
    }

    @Test
    public void getAllItemsTest() {
        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(3L)
                .verifyComplete();
    }

    @Test
    public void getItemByIdTest() {
        StepVerifier.create(itemRepository.findById("123"))
                .expectSubscription()
                .expectNextMatches( item -> item.getDescription().equals("Item 3 Test") )
                .verifyComplete();
    }

    @Test
    public void findItemByDescriptionTest() {
        StepVerifier.create(itemRepository.findByDescription("Item 2 Test"))
                .expectSubscription()
                .expectNextMatches( item -> item.getPrice().equals(new BigDecimal("4.5")))
                .verifyComplete();
    }

    @Test
    public void saveItemTest() {
        Item itemSave = new Item(null, "Item Save", new BigDecimal("11"));
        Mono<Item> monoItem = itemRepository.save(itemSave);
        StepVerifier.create(monoItem.log(":: LOG SAVED ITEM :: "))
                .expectSubscription()
                .expectNextMatches( item -> (item.getId() != null && item.getDescription().equals("Item Save")))
                .verifyComplete();
    }

    @Test
    public void updateItemTest() {
        BigDecimal price = new BigDecimal("40");
        Mono<Item> updatedItem =  itemRepository.findByDescription("Item 1 Test")
                .map( item -> {
                    item.setPrice(price);
                    return item;
                })
                .flatMap( item -> itemRepository.save(item));
        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice().equals(new BigDecimal("40")))
                .verifyComplete();
    }

    @Test
    public void deleteByIdItemTest() {
        Mono<Void> deletedItem = itemRepository.findById("123")
                .map(Item::getId)
                .flatMap(id -> itemRepository.deleteById(id));
        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();
        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(2L)
                .verifyComplete();
    }

    @Test
    public void deleteItem() {
        Mono<Void> deletedItem = itemRepository.findByDescription("Item 2 Test")
                .flatMap((item) -> itemRepository.delete(item));
        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();
        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(2L)
                .verifyComplete();
    }

}
