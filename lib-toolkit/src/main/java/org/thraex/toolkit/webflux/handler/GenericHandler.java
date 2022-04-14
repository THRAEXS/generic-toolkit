package org.thraex.toolkit.webflux.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thraex.toolkit.entity.JpaEntity;
import org.thraex.toolkit.mvc.service.GenericService;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Generic type T to Type / Class
 *
 * <pre>
 *     ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
*      Type type = genericSuperclass.getActualTypeArguments()[0];
 *     ParameterizedTypeReference<T> typeReference = ParameterizedTypeReference.forType(type);
 *
 *     request.bodyToMono(typeReference);
 * </pre>
 *
 * <pre>
 *     ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
*      Type type = genericSuperclass.getActualTypeArguments()[0];
 *     Class<T> clazz = (Class) genericSuperclass.getActualTypeArguments()[0];
 *
 *     request.bodyToMono(clazz);
 * </pre>
 *
 * @author 鬼王
 * @date 2022/04/14 18:13
 */
public abstract class GenericHandler<T extends JpaEntity<T>, S extends GenericService<T, ?>> {

    protected static final String ID = "id";

    @Autowired
    protected S service;

    private final Class<T> genericType;

    protected GenericHandler() {
        Class<T>[] classes = (Class<T>[]) GenericTypeResolver.resolveTypeArguments(getClass(), GenericHandler.class);
        genericType = classes[0];
    }

    public Mono<ServerResponse> one(ServerRequest request) {
        String id = request.pathVariable(ID);
        Optional<T> entity = service.repo().findById(id);

        return ServerResponse.ok().bodyValue(entity);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(genericType)
                .map(service::save)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(genericType)
                .map(service::save)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable(ID);
        service.repo().deleteById(id);

        return ServerResponse.ok().bodyValue(true);
    }

}
