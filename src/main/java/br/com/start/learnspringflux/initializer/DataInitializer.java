package br.com.start.learnspringflux.initializer;

import br.com.start.learnspringflux.domain.Item;
import br.com.start.learnspringflux.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ItemReactiveRepository repository;

    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }

    private void initializeData() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(repository::save)
                .thenMany(repository.findAll())
                .subscribe( item -> log.info("Initializing Item {}", item));
    }

    private List<Item> data() {
        return Arrays.asList(
                new Item(null, "LG TV", new BigDecimal("899.99")),
                new Item(null, "MackBook Air", new BigDecimal("7000.00")),
                new Item(null, "Microwave", new BigDecimal("159.90")));
    }
}
