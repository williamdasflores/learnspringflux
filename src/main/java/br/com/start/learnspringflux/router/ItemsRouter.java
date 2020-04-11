package br.com.start.learnspringflux.router;

import br.com.start.learnspringflux.handler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static br.com.start.learnspringflux.constants.ItemConstants.ITEM_FUNCTIONAL_V1_ENDPOINT;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemRoute(ItemHandler itemHandler) {
        return RouterFunctions
                .route(GET(ITEM_FUNCTIONAL_V1_ENDPOINT)
                        .and(accept(MediaType.APPLICATION_JSON))
                , itemHandler::getAllItems)
                .andRoute(GET(ITEM_FUNCTIONAL_V1_ENDPOINT + "/{id}").and(accept(MediaType.APPLICATION_JSON))
                , itemHandler::getItem)
                .andRoute(POST(ITEM_FUNCTIONAL_V1_ENDPOINT).and(accept(MediaType.APPLICATION_JSON))
                , itemHandler::createItem)
                .andRoute(DELETE(ITEM_FUNCTIONAL_V1_ENDPOINT + "/{id}").and(accept(MediaType.APPLICATION_JSON))
                , itemHandler::deleteItem)
                .andRoute(PUT(ITEM_FUNCTIONAL_V1_ENDPOINT + "/{id}").and(accept(MediaType.APPLICATION_JSON))
                , itemHandler::updateItem);
    }

}
