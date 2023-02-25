package org.thraex.toolkit.webflux.handler;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thraex.toolkit.entity.JpaEntity;
import org.thraex.toolkit.mvc.service.GenericService;
import reactor.core.publisher.Mono;

/**
 * TODO: Opt query condition / sort
 *
 * @author 鬼王
 * @date 2022/04/17 16:06
 */
public abstract class AbstractListHandler<T extends JpaEntity<T>, S extends GenericService<T, ?>> extends AbstractHandler<T, S> {

    protected AbstractListHandler(S service) {
        super(service);
    }

    public Mono<ServerResponse> list(ServerRequest request) {
        return ServerResponse.ok().bodyValue(service.repo().findAll());
    }

    public Mono<ServerResponse> page(ServerRequest request) {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("sort"));
        return ServerResponse.ok().bodyValue(service.repo().findAll(pageable));
    }

}
