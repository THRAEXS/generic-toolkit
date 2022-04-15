package org.thraex.dmpp.generic.doc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author 鬼王
 * @date 2022/04/07 19:08
 */
public class Documentation {

    private static final String RESOURCES = "classpath:/static/docs/index.html";

    @Bean
    @ConditionalOnResource(resources = RESOURCES)
    RouterFunction<ServerResponse> docs(@Value(RESOURCES) Resource index){
        return route(GET("/"), request ->
                ok().contentType(MediaType.TEXT_HTML).bodyValue(index))
                .and(RouterFunctions.resources("/**", new ClassPathResource("static/docs/")));
    }

}

