package br.com.start.learnspringflux.handler;

import br.com.start.learnspringflux.domain.Item;
import br.com.start.learnspringflux.repository.ItemReactiveRepository;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemHandler {

    private ItemReactiveRepository repository;
    private static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public ItemHandler(ItemReactiveRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll(), Item.class).log();

    }

    public Mono<ServerResponse> getItem(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Item> itemMono = repository.findById(id);

        return itemMono.flatMap( item ->
            ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(item)))
            .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> createItem(ServerRequest request) {
        Mono<Item> itemMono = request.bodyToMono(Item.class);

        return itemMono.flatMap( item ->
                ServerResponse.ok().
                        contentType(MediaType.APPLICATION_JSON)
                        .body(repository.save(item), Item.class));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Void> deleteItem = repository.deleteById(id);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deleteItem, Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Item> itemMono = request.bodyToMono(Item.class);
        Mono<Item> updatedMono = itemMono.flatMap(item -> {
            Mono<Item> mono = repository.findById(id)
                    .flatMap(currenItem -> {
                        currenItem.setDescription(item.getDescription());
                        currenItem.setPrice(item.getPrice());
                        return repository.save(currenItem);
                    });
            return mono;
        });

        return updatedMono
                .flatMap( item -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(item)))
                .switchIfEmpty(notFound);
    }
}
