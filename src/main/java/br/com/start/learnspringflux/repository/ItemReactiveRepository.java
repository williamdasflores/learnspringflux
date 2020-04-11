package br.com.start.learnspringflux.repository;

import br.com.start.learnspringflux.domain.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    Mono<Item> findByDescription(String description);

}
