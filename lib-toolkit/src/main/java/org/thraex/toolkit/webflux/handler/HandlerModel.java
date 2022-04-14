package org.thraex.toolkit.webflux.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author 鬼王
 * @date 2022/04/14 22:10
 */
public interface HandlerModel {

    String ID = "id";

    Mono<ServerResponse> one(ServerRequest request);

    Mono<ServerResponse> save(ServerRequest request);

    Mono<ServerResponse> update(ServerRequest request);

    Mono<ServerResponse> delete(ServerRequest request);

    String pattern();

}
