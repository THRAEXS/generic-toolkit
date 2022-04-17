package org.thraex.toolkit.webflux.handler;

import org.springframework.core.GenericTypeResolver;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunctions;
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
public abstract class AbstractHandler<T extends JpaEntity<T>, S extends GenericService<T, ?>> implements GenericHandler {

    protected final S service;

    private final Class<T> genericType;

    protected AbstractHandler(S service) {
        this.service = service;

        Class<T>[] classes = (Class<T>[]) GenericTypeResolver.resolveTypeArguments(getClass(), AbstractHandler.class);
        genericType = classes[0];
    }

    @Override
    public Mono<ServerResponse> one(ServerRequest request) {
        String id = request.pathVariable(ID);
        Optional<T> entity = service.repo().findById(id);

        return ServerResponse.ok().bodyValue(entity);
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(genericType)
                .map(service::save)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(genericType)
                .map(service::save)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable(ID);
        service.repo().deleteById(id);

        return ServerResponse.ok().bodyValue(true);
    }

    @Override
    public String pattern() {
        return genericType.getSimpleName().toLowerCase();
    }

    @Override
    public void routerFunction(String pattern, RequestPredicate predicate, RouterFunctions.Builder builder) {}

}
