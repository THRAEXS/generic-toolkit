package org.thraex.toolkit.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thraex.toolkit.webflux.handler.AbstractListHandler;
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
    public static final String PAGE_PATTERN = "%s/page";

    /**
     * {@code @ConditionalOnMissingBean(name = "genericRouterFunction")} can be omitted
     *
     * @param context
     * @return
     */
    @Bean
    @ConditionalOnBean(GenericHandler.class)
    @ConditionalOnMissingBean(name = "genericRouterFunction")
    RouterFunction<ServerResponse> genericRouterFunction(ApplicationContext context) {
        Map<String, GenericHandler> beans = context.getBeansOfType(GenericHandler.class);
        return beans.values().stream().map(this::assemble).reduce(RouterFunction::and).get();
    }

    private RouterFunction<ServerResponse> assemble(GenericHandler handler) {
        String pattern = handler.pattern();
        String varPattern = String.format(PATH_VARIABLE_PATTERN, pattern);

        RouterFunctions.Builder route = route();
        if (handler instanceof AbstractListHandler<?,?>) {
            AbstractListHandler listHandler = (AbstractListHandler) handler;
            route.GET(pattern, ACCEPT_JSON, listHandler::list)
                    .GET(String.format(PAGE_PATTERN, pattern), ACCEPT_JSON, listHandler::page);
        }

        route.GET(varPattern, ACCEPT_JSON, handler::one)
                .POST(pattern, ACCEPT_JSON, handler::save)
                .PUT(pattern, ACCEPT_JSON, handler::update)
                .DELETE(varPattern, ACCEPT_JSON, handler::delete);

        handler.routerFunction(pattern, ACCEPT_JSON, route);

        return route.build();
    }

}
