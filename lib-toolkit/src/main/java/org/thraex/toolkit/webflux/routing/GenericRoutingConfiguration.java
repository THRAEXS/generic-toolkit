package org.thraex.toolkit.webflux.routing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thraex.toolkit.webflux.handler.GenericHandler;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author 鬼王
 * @date 2022/04/14 21:57
 */
public class GenericRoutingConfiguration {

    public static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);

    public static final String PATH_VARIABLE_PATTERN = "%s/{id}";

    /**
     * {@code @ConditionalOnMissingBean(name = "genericRouters")} can be omitted
     *
     * @param context
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "genericRouters")
    @ConditionalOnBean(GenericHandler.class)
    RouterFunction<ServerResponse> genericRouters(ApplicationContext context) {
        Map<String, GenericHandler> beans = context.getBeansOfType(GenericHandler.class);
        RouterFunctions.Builder route = route();

        beans.values().forEach(handler -> {
            String pattern = handler.pattern();
            String varPattern = String.format(PATH_VARIABLE_PATTERN, pattern);

            route.POST(pattern, ACCEPT_JSON, handler::save)
                    .PUT(pattern, ACCEPT_JSON, handler::update)
                    .GET(varPattern, ACCEPT_JSON, handler::one)
                    .DELETE(varPattern, ACCEPT_JSON, handler::delete);
        });

        return route.build();
    }

}
