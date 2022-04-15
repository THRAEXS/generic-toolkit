package org.thraex.toolkit.webflux.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thraex.toolkit.webflux.handler.GenericHandler;
import org.thraex.toolkit.webflux.handler.HandlerModel;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author 鬼王
 * @date 2022/04/14 21:57
 */
public class GenericRouting<T extends GenericHandler<?, ?>> {

    protected static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);

    @Bean
    RouterFunction<ServerResponse> genericRouterFunction(T handler) {
        String pattern = handler.pattern();
        String varPattern = String.format("%s/{id}", pattern);

        return route()
                .POST(pattern, ACCEPT_JSON, handler::save)
                .PUT(pattern, ACCEPT_JSON, handler::update)
                .GET(varPattern, ACCEPT_JSON, handler::one)
                .DELETE(varPattern, ACCEPT_JSON, handler::delete)
                .build();
    }

}
