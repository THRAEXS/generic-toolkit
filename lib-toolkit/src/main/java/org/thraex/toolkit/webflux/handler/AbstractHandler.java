package org.thraex.toolkit.webflux.handler;

import org.springframework.core.GenericTypeResolver;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.thraex.toolkit.entity.JpaEntity;
import org.thraex.toolkit.mvc.service.GenericService;
import reactor.core.publisher.Mono;

/**
 * TODO: saveAll
 *
 * <br>
 *
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
 * TODO: Feature
 * <p>
 *     Add {@link jakarta.validation.Validator}
 * </p>
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
        T entity = service.repo().findById(id).orElseThrow(() ->
                new EmptyResultDataAccessException(String.format("No entity with id %s exists!", id), 1));

        return ServerResponse.ok().bodyValue(entity);
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(genericType)
                .doOnSuccess(this::notNull)
                .map(service::save)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(genericType)
                .doOnNext(it -> Assert.notNull(it.getId(), "The given entity id must not be null!"))
                .doOnSuccess(this::notNull)
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
        final String simpleName = genericType.getSimpleName();
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1/$2";

        return simpleName.replaceAll(regex, replacement).toLowerCase();
    }

    @Override
    public void routerFunction(String pattern, RequestPredicate predicate, RouterFunctions.Builder builder) {}

    protected void notNull(Object value) {
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
