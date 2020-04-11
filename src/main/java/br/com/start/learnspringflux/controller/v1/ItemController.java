package br.com.start.learnspringflux.controller.v1;

import br.com.start.learnspringflux.domain.Item;
import br.com.start.learnspringflux.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static br.com.start.learnspringflux.constants.ItemConstants.*;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemReactiveRepository itemRepository;

    @GetMapping(ITEM_V1_ENDPOINT)
    public Flux<Item> getAllItems() {
        Flux<Item> items = itemRepository.findAll();
        items.subscribe(item -> log.info("{}", item));

        return items;
    }

    @GetMapping(ITEM_V1_ENDPOINT+ "/{id}")
    public Mono<ResponseEntity<Item>> getItemById(@PathVariable String id) {
        log.info("ID {}", id);
        Mono<Item> monoItem = itemRepository.findById(id);
        monoItem.subscribe(item -> log.info("ITEM:: {}", item));

        Mono<ResponseEntity<Item>> response = monoItem.map(item -> new ResponseEntity<Item>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.OK));
        return response;
    }

    @PostMapping(ITEM_V1_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> saveItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @DeleteMapping(ITEM_V1_ENDPOINT + "/{id}")
    public Mono<Void> deleletItem(@PathVariable String id) {
        return itemRepository.deleteById(id);
    }

    @PutMapping(ITEM_V1_ENDPOINT + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id,
                                                 @RequestBody Item item) {
        return itemRepository.findById(id)
            .flatMap(currentItem -> {
                currentItem.setPrice(item.getPrice());
                currentItem.setDescription(item.getDescription());
                return itemRepository.save(currentItem);
            })
            .map(updatedItem -> new ResponseEntity<>(updatedItem , HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
